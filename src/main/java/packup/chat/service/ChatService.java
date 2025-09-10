package packup.chat.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import packup.chat.domain.ChatMessage;
import packup.chat.domain.ChatMessageFile;
import packup.chat.domain.ChatRoom;
import packup.chat.domain.repository.ChatMessageFileRepository;
import packup.chat.domain.repository.ChatMessageRepository;
import packup.chat.domain.repository.ChatReadRepository;
import packup.chat.domain.repository.ChatRoomRepository;
import packup.chat.dto.*;
import packup.chat.exception.ChatException;
import packup.common.dto.FileResponse;
import packup.common.dto.PageDTO;
import packup.common.enums.YnType;
import packup.common.util.FileUtil;
import packup.common.util.JsonUtil;
import packup.common.util.ValidationUtil;
import packup.fcmpush.dto.FcmPushRequest;
import packup.fcmpush.enums.DeepLinkType;
import packup.fcmpush.presentation.DeepLinkGenerator;
import packup.fcmpush.service.FcmPushService;
import packup.user.domain.UserInfo;
import packup.user.domain.repository.UserDetailInfoRepository;
import packup.user.domain.repository.UserInfoRepository;
import packup.user.dto.UserInfoResponse;
import packup.user.dto.UserProfileImageResponse;
import packup.user.dto.UserPushTarget;

import java.io.IOException;
import java.sql.Timestamp;
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
    private final UserDetailInfoRepository userDetailInfoRepository;
    private final ChatMessageFileRepository chatMessageFileRepository;
    private final ChatReadRepository chatReadRepository;

    private final FileUtil fileUtil;

    private final FcmPushService firebaseService;

    private final SimpMessagingTemplate messagingTemplate;

    private final ApplicationEventPublisher publisher;

    private final ValidationUtil validationUtil;

    public PageDTO<ChatRoomResponse> getChatRoomList(Long memberId, int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<Map<String, Object>> chatRoomListPage =
                chatRoomRepository.findChatRoomListWithUnreadCount(memberId, pageable);

        List<ChatRoomResponse> chatRooms = chatRoomListPage.getContent().stream()
                .map(row -> {
                    // 참여자 리스트(JSON 컬럼)
                    List<Long> partUserSeqList = new ArrayList<>();
                    try {
                        Object partUserRaw = row.get("part_user_seq");
                        if (partUserRaw != null) {
                            partUserSeqList = JsonUtil.fromJson(partUserRaw.toString(), new TypeReference<>() {});
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // 마지막 메시지 파일 여부
                    YnType fileFlag = Optional.ofNullable(row.get("last_message_file_flag"))
                            .map(Object::toString)
                            .map(YnType::valueOf)
                            .orElse(YnType.N);

                    // 채팅방 생성자
                    Long ownerSeq = ((Number) row.get("user_seq")).longValue();

                    // 채팅방 생성자의 프로필 이미지 조회
                    String profileImagePath = userDetailInfoRepository.findProfileImageBySeq(ownerSeq)
                            .map(UserProfileImageResponse::getProfileImagePath)
                            .orElse(null);

                    return ChatRoomResponse.builder()
                            .seq(((Number) row.get("seq")).longValue())
                            .userSeq(ownerSeq)
                            .partUserSeq(partUserSeqList)
                            .profileImagePath(profileImagePath)
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

        chatRoomRepository.findById(chatRoomSeq)
                .orElseThrow(() -> new ChatException(NOT_FOUND_CHAT_ROOM));

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<ChatMessageResponse> chatMessageListPage = chatMessageRepository.findMessages(chatRoomSeq, pageable);

        return PageDTO.<ChatMessageResponse>builder()
                .objectList(chatMessageListPage.getContent())
                .totalPage(chatMessageListPage.getTotalPages())
                .build();
    }


    @Transactional
    public ChatMessageResponse saveChatMessage(Long memberId, ChatMessageRequest chatMessageDTO) {

        UserProfileImageResponse profileImage = userDetailInfoRepository.findProfileImageBySeq(memberId)
                .orElseThrow(() -> new ChatException(NOT_FOUND_MEMBER));

        ChatRoom chatRoomRef = chatRoomRepository.getReferenceById(chatMessageDTO.getChatRoomSeq());

        ChatMessage newChatMessage = ChatMessage.of(
                chatRoomRef,
                profileImage.getSeq(),
                chatMessageDTO.getMessage(),
                chatMessageDTO.getFileFlag()
        );

        chatMessageRepository.save(newChatMessage);

//        chatRoomRef.updateChatLastDate();
        chatRoomRepository.touch(chatMessageDTO.getChatRoomSeq());

        // 응답 구성 시 연관 접근 대신 이미 로드된 userInfo 사용
        return ChatMessageResponse.builder()
                .seq(newChatMessage.seq())
                .userSeq(profileImage.getSeq())
                .message(newChatMessage.getMessage())
                .profileImagePath(profileImage.getProfileImagePath())
                .chatRoomSeq(chatRoomRef.seq())
                .createdAt(newChatMessage.getCreatedAt())
                .fileFlag(newChatMessage.getFileFlag())
                .build();
    }


    @Transactional
    public void readChatMessage(long memberId, ReadMessageRequest readMessageRequest) {

//        userInfoRepository.getReferenceById(memberId);
//        chatRoomRepository.getReferenceById(readMessageRequest.getChatRoomSeq());
//        chatMessageRepository.getReferenceById(readMessageRequest.getLastReadMessageSeq());
        chatReadRepository.upsertRead(readMessageRequest.getChatRoomSeq(), memberId, readMessageRequest.getLastReadMessageSeq());
    }

    public List<Long> getPartUserInRoom(Long chatRoomSeq) {
        var raw = chatRoomRepository.findParticipantSeq(chatRoomSeq);

        return validationUtil.validateActiveUser(raw);
    }

    @Transactional
    public FileResponse saveFile(Long memberId, String type, MultipartFile file) throws IOException {

        UserInfo userInfo = userInfoRepository.findById(memberId)
                .orElseThrow(() -> new ChatException(NOT_FOUND_MEMBER));

        FileResponse imageDTO = fileUtil.saveImage(type, file);

        ChatMessageFile chatMessageFile = ChatMessageFile.of(
                imageDTO.getPath(),
                userInfo.getSeq(),
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
        System.out.println("채팅방 리프래시");
        List<Long> targets = participants.stream()
                .filter(u -> !u.equals(senderSeq))
                .collect(Collectors.toList());

        // 배치로 스냅샷 생성 (쿼리 소수 회)
        Map<Long, ChatRoomResponse> snapshots = getRoomForRefresh(roomId, participants);

        System.out.println(participants);

        publisher.publishEvent(new RoomChangedEvent(roomId, participants, snapshots));
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

    private Map<Long, ChatRoomResponse> getRoomForRefresh(Long roomId, List<Long> userSeqs) {
        // 1) 채팅방 1회
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ChatException(NOT_FOUND_CHAT_ROOM));

        // 2) 마지막 메시지 1회
        Optional<LastMessageProjection> last = chatMessageRepository.findTop1ByChatRoomSeqOrderByCreatedAtDesc(roomId);

        // 3) 사용자별 미읽음 수 1회 (전체 대상 그룹화)
        Long[] arr = userSeqs.toArray(new Long[0]);
        List<UnreadMessageProjection> unreadRows = chatReadRepository.countUnreadByUsers(roomId, arr);
        Map<Long, Integer> unreadMap = unreadRows.stream()
                .collect(Collectors.toMap(UnreadMessageProjection::getUserSeq, UnreadMessageProjection::getUnread));

        Map<Long, ChatRoomResponse> out = new HashMap<>();
        for (Long uid : userSeqs) {
            int unread = unreadMap.getOrDefault(uid, 0);

            out.put(uid, ChatRoomResponse.builder()
                    .seq(room.getSeq())
                    .partUserSeq(room.getPartUserSeq())
                    .unReadCount(unread)
                    .lastMessage(last.map(LastMessageProjection::getMessage).orElse(null))
                    .lastMessageDate(last.map(LastMessageProjection::getCreatedAt).orElse(null))
                    .fileFlag(YnType.valueOf(last.map(LastMessageProjection::getFileFlag).orElse(null)))
                    .title(room.getTitle())
                    .createdAt(room.getCreatedAt())
                    .updatedAt(room.getUpdatedAt())
                    .build());
        }
        return out;
    }

}

