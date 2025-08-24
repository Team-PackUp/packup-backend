package packup.chat.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import packup.chat.domain.ChatMessage;
import packup.chat.domain.ChatMessageFile;
import packup.chat.domain.ChatRead;
import packup.chat.domain.ChatRoom;
import packup.chat.domain.repository.ChatMessageFileRepository;
import packup.chat.domain.repository.ChatMessageRepository;
import packup.chat.domain.repository.ChatReadRepository;
import packup.chat.domain.repository.ChatRoomRepository;
import packup.chat.dto.ChatMessageRequest;
import packup.chat.dto.ChatMessageResponse;
import packup.chat.dto.ChatRoomResponse;
import packup.chat.dto.ReadMessageRequest;
import packup.chat.exception.ChatException;
import packup.chat.dto.RoomChangedEvent;
import packup.common.dto.FileResponse;
import packup.common.dto.PageDTO;
import packup.common.enums.YnType;
import packup.common.util.FileUtil;
import packup.common.util.JsonUtil;
import packup.fcmpush.dto.FcmPushRequest;
import packup.fcmpush.enums.DeepLinkType;
import packup.fcmpush.presentation.DeepLinkGenerator;
import packup.fcmpush.service.FcmPushService;
import packup.user.domain.UserDetailInfo;
import packup.user.domain.UserInfo;
import packup.user.domain.repository.UserInfoRepository;
import packup.user.dto.UserInfoResponse;
import packup.user.dto.UserPushTarget;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static packup.chat.constant.ChatConstant.*;
import static packup.chat.exception.ChatExceptionType.*;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserInfoRepository userInfoRepository;
    private final ChatMessageFileRepository chatMessageFileRepository;
    private final ChatReadRepository chatReadRepository;

    private final FileUtil fileUtil;

    private final FcmPushService firebaseService;

    private final SimpMessagingTemplate messagingTemplate;

    private final ApplicationEventPublisher publisher;

    public ChatRoomResponse getChatRoom(Long memberId, Long chatRoomSeq) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomSeq)
                .orElseThrow(() -> new ChatException(NOT_FOUND_CHAT_ROOM));

        UserInfo userInfo = userInfoRepository.findById(memberId)
                .orElseThrow(() -> new ChatException(NOT_FOUND_MEMBER));

        // 최근 읽은 시각
        LocalDateTime lastReadTime = chatReadRepository
                .findChatReadByUserAndChatRoomSeq(userInfo, chatRoom)
                .map(ChatRead::getUpdatedAt)
                .orElse(LocalDateTime.of(1970, 1, 1, 0, 0));

        // 안 읽은 메시지 수
        int unreadCount = chatMessageRepository.countByChatRoomSeqAndUserNotAndCreatedAtAfter(
                chatRoom,
                userInfo,
                lastReadTime,
                Limit.of(UNREAD_CHAT_COUNT_LIMIT)
        );

        // 마지막 채팅 정보
        Optional<ChatMessage> lastMessageOpt = chatMessageRepository
                .findTopByChatRoomSeqOrderByCreatedAtDesc(chatRoom);

        String lastMessage = lastMessageOpt.map(ChatMessage::getMessage).orElse(null);
        LocalDateTime lastMessageDate = lastMessageOpt.map(ChatMessage::getCreatedAt).orElse(null);
        YnType fileFlag = lastMessageOpt.map(ChatMessage::getFileFlag).orElse(null);

        return ChatRoomResponse.builder()
                .seq(chatRoom.getSeq())
                .user(UserInfoResponse.of(userInfo))
                .partUserSeq(chatRoom.getPartUserSeq())
                .unReadCount(unreadCount)
                .lastMessage(lastMessage)
                .lastMessageDate(lastMessageDate)
                .title(chatRoom.getTitle())
                .fileFlag(fileFlag)
                .createdAt(chatRoom.getCreatedAt())
                .updatedAt(chatRoom.getUpdatedAt())
                .build();
    }


    public PageDTO<ChatRoomResponse> getChatRoomList(Long memberId, int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<Map<String, Object>> chatRoomListPage = chatRoomRepository.findChatRoomListWithUnreadCount(memberId, pageable);

        List<ChatRoomResponse> chatRooms = chatRoomListPage.getContent().stream()
                .map(row -> {
                    List<Long> partUserSeqList = new ArrayList<>();
                    try {
                        Object partUserRaw = row.get("part_user_seq");
                        if (partUserRaw != null) {
                            partUserSeqList = JsonUtil.fromJson(partUserRaw.toString(), new TypeReference<>() {});
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    YnType fileFlag = Optional.ofNullable(row.get("last_message_file_flag"))
                            .map(Object::toString)
                            .map(YnType::valueOf)
                            .orElse(YnType.N);

                    UserInfo userInfo = userInfoRepository.findById((Long) row.get("user_seq")).orElseThrow();

                    return ChatRoomResponse.builder()
                            .seq(((Number) row.get("seq")).longValue())
                            .partUserSeq(partUserSeqList)
                            .user(UserInfoResponse.of(userInfo))
                            .title((String) row.get("title"))
                            .unReadCount(row.get("unread_count") != null
                                    ? ((Number) row.get("unread_count")).intValue() : 0)
                            .lastMessage((String) row.get("last_message"))
                            .lastMessageDate(row.get("last_message_date") != null
                                    ? ((Timestamp) row.get("last_message_date")).toLocalDateTime()
                                    : null)
                            .fileFlag(fileFlag)
                            .createdAt(((Timestamp) row.get("created_at")).toLocalDateTime())
                            .updatedAt(((Timestamp) row.get("updated_at")).toLocalDateTime())
                            .build();
                })
                .collect(Collectors.toList());

        return PageDTO.<ChatRoomResponse>builder()
                .objectList(chatRooms)
                .totalPage(chatRoomListPage.getTotalPages())
                .build();
    }

    public PageDTO<ChatMessageResponse> getChatMessageList(Long memberId, Long chatRoomSeq, int page) {

        userInfoRepository.findById(memberId)
                .orElseThrow(() -> new ChatException(NOT_FOUND_MEMBER));

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomSeq)
                .orElseThrow(() -> new ChatException(NOT_FOUND_CHAT_ROOM));

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<ChatMessage> chatMessageListPage = chatMessageRepository.findByChatRoomSeqOrderByCreatedAtDesc(chatRoom, pageable);

        List<ChatMessageResponse> chatMessages = chatMessageListPage
                .getContent()
                .stream()
                .map(msg -> ChatMessageResponse.builder()
                        .seq(msg.getSeq())
                        .userSeq(msg.getUser().getSeq())
                        .message(msg.getMessage())
                        .chatRoomSeq(msg.getChatRoomSeq().getSeq())
                        .createdAt(msg.getCreatedAt())
                        .fileFlag(msg.getFileFlag())
                        .profileImagePath(
                                Optional.ofNullable(msg.getUser().getDetailInfo())
                                        .map(UserDetailInfo::getProfileImagePath)
                                        .orElse(null)
                        )
                        .build())
                .toList();


        return PageDTO.<ChatMessageResponse>builder()
                .objectList(chatMessages)
                .totalPage(chatMessageListPage.getTotalPages())
                .build();
    }

    @Transactional
    public ChatMessageResponse saveChatMessage(Long memberId, ChatMessageRequest chatMessageDTO) {

        if (chatMessageDTO.getMessage().isEmpty()) {
            throw new ChatException(ABNORMAL_ACCESS);
        }

        UserInfo userInfo = userInfoRepository.findBySeqWithDetail(memberId)
                .orElseThrow(() -> new ChatException(NOT_FOUND_MEMBER));

        ChatRoom chatRoomRef = chatRoomRepository.getReferenceById(chatMessageDTO.getChatRoomSeq());

        ChatMessage newChatMessage = ChatMessage.of(
                chatRoomRef,
                userInfo,
                chatMessageDTO.getMessage(),
                chatMessageDTO.getFileFlag()
        );

        chatMessageRepository.save(newChatMessage);

        chatRoomRef.updateChatLastDate();

        // 응답 구성 시 연관 접근 대신 이미 로드된 userInfo 사용
        return ChatMessageResponse.builder()
                .seq(newChatMessage.seq())
                .userSeq(userInfo.getSeq())
                .message(newChatMessage.getMessage())
                .profileImagePath(userInfo.getDetailInfo() != null ? userInfo.getDetailInfo().getProfileImagePath() : null)
                .chatRoomSeq(chatRoomRef.seq())
                .createdAt(newChatMessage.getCreatedAt())
                .fileFlag(newChatMessage.getFileFlag())
                .build();
    }


    @Transactional
    public void readChatMessage(long memberId, ReadMessageRequest readMessageRequest) {

        UserInfo userInfo = userInfoRepository.findById(memberId)
                .orElseThrow(() -> new ChatException(NOT_FOUND_MEMBER));

        ChatRoom chatRoom = chatRoomRepository.findById(readMessageRequest.getChatRoomSeq())
                .orElseThrow(() -> new ChatException(NOT_FOUND_CHAT_ROOM));

        ChatMessage chatMessage = chatMessageRepository.findById(readMessageRequest.getLastReadMessageSeq())
                .orElseThrow(() -> new ChatException(NOT_FOUND_CHAT_MESSAGE));

        ChatRead chatRead = chatReadRepository.findChatReadByUserAndChatRoomSeq(userInfo, chatRoom)
                .orElseGet(() -> ChatRead.of(chatRoom, userInfo, chatMessage));

        chatRead.updateLastReadMessage(chatMessage);

        chatReadRepository.save(chatRead);
    }

    public List<Long> getPartUserInRoom(Long chatRoomSeq) {
        var raw = chatRoomRepository.findParticipantSeq(chatRoomSeq);

        return validateActiveUser(raw);
    }

    public List<Long> validateActiveUser(List<Long> chatRoomSeq) {

        if (chatRoomSeq.isEmpty()) return Collections.emptyList();

        return userInfoRepository.findBySeqInAndWithdrawFlag(chatRoomSeq, YnType.N)
                .stream()
                .map(UserInfo::getSeq)
                .toList();
    }

    public FileResponse saveFile(Long memberId, String type, MultipartFile file) throws IOException {

        UserInfo userInfo = userInfoRepository.findById(memberId)
                .orElseThrow(() -> new ChatException(NOT_FOUND_MEMBER));

        FileResponse imageDTO = fileUtil.saveImage(type, file);

        ChatMessageFile chatMessageFile = ChatMessageFile.of(
                imageDTO.getPath(),
                userInfo,
                imageDTO.getEncodedName(),
                imageDTO.getRealName()
        );

        chatMessageFileRepository.save(chatMessageFile);

        return FileResponse
                .builder()
                .seq(chatMessageFile.seq())
                .path(chatMessageFile.getChatFilePath())
                .userSeq(userInfo.getSeq())
                .realName(chatMessageFile.getRealName())
                .encodedName(chatMessageFile.getEncodedName())
                .type("chat")
                .createdAt(chatMessageFile.getCreatedAt())
                .build();
    }

    // FCM 알림
    public void chatSendFcmPush(ChatMessageResponse chatMessageResponse, List<Long> targetUserSeqs) throws FirebaseMessagingException {
        if (targetUserSeqs == null || targetUserSeqs.isEmpty()) return;

        String title = chatRoomRepository.findTitleById(chatMessageResponse.getChatRoomSeq());

        String body = YnType.Y.equals(chatMessageResponse.getFileFlag())
                ? REPLACE_IMAGE_TEXT
                : chatMessageResponse.getMessage();

        List<UserPushTarget> targets = userInfoRepository.findPushTargets(
                targetUserSeqs,
                YnType.Y,
                YnType.N
        );

        List<String> tokens = targets.stream()
                .map(UserPushTarget::fcmToken)
                .filter(t -> t != null && !t.isBlank())
                .distinct()
                .toList();


        if (tokens.isEmpty()) return;

        Map<String, Object> data = new HashMap<>();
        data.put("chatRoomSeq", chatMessageResponse.getChatRoomSeq());
        data.put("messageSeq",  chatMessageResponse.getSeq());

        FcmPushRequest req = FcmPushRequest.builder()
                .tokenList(tokens)
                .title(title)
                .body(body)
                .deepLink(DeepLinkGenerator.generate(DeepLinkType.CHAT, data))
                .build();

        firebaseService.requestFcmPush(req);
    }


    public void sendMessage(Long chatRoomSeq, ChatMessageResponse newChatMessageDTO) {
        messagingTemplate.convertAndSend("/topic/chat/room/" + chatRoomSeq, newChatMessageDTO);
    }

    public List<Long> refreshChatRoom(Long senderSeq, Long roomId, List<Long> participants) {

        var targets = participants.stream()
                .filter(u -> !u.equals(senderSeq))
                .toList();

        publisher.publishEvent(new RoomChangedEvent(roomId, targets));

        return targets;
    }


    public void createChatRoom() {
        // 생성할 채팅방 대상 투어들 조회

        // 생성

//        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomSeq);
//
//        if (chatRoom.getPartUserSeq().contains(newPartUserSeq)) {
//            throw new ChatException(ALREADY_PARTICIPATION);
//        }
//
//        chatRoom.getPartUserSeq().add(newPartUserSeq);
//
//        chatRoom = chatRoomRepository.save(
//                ChatRoom.builder()
//                        .partUserSeq(chatRoom.getPartUserSeq())
//                        .updatedAt(LocalDateTime.now())
//                        .build()
//        );
    }
}

