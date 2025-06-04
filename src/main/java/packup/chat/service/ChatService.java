package packup.chat.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import packup.common.dto.FileResponse;
import packup.common.dto.PageDTO;
import packup.common.util.FileUtil;
import packup.common.util.JsonUtil;
import packup.fcmpush.dto.FcmPushRequest;
import packup.fcmpush.service.FcmPushService;
import packup.user.domain.UserDetailInfo;
import packup.user.domain.UserInfo;
import packup.user.domain.repository.UserDetailInfoRepository;
import packup.user.domain.repository.UserInfoRepository;

import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static packup.chat.constant.ChatConstant.PAGE_SIZE;
import static packup.chat.constant.ChatConstant.REPLACE_IMAGE_TEXT;
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

    private final UserDetailInfoRepository userDetailInfoRepository;
    private final FcmPushService firebaseService;

    public ChatRoomResponse getChatRoom(Long memberId, Long chatRoomSeq) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomSeq)
                .orElseThrow(() -> new ChatException(NOT_FOUND_CHAT_ROOM));

        UserInfo userInfo = userInfoRepository.findById(memberId)
                .orElseThrow(() -> new ChatException(NOT_FOUND_MEMBER));

        int unreadCount;

        Optional<ChatRead> chatReadOpt = chatReadRepository.findChatReadByUserAndChatRoomSeq(userInfo, chatRoom);
        LocalDateTime lastReadTime = chatReadOpt.map(ChatRead::getUpdatedAt).orElse(LocalDateTime.of(1970, 1, 1, 0, 0));

        unreadCount = chatMessageRepository.countByChatRoomSeqAndUserNotAndCreatedAtAfter(
                chatRoom,
                userInfo,
                lastReadTime
        );

        return ChatRoomResponse.builder()
                .seq(chatRoom.getSeq())
                .userSeq(userInfo.getSeq())
                .partUserSeq(chatRoom.getPartUserSeq())
                .unReadCount(unreadCount)
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
                            partUserSeqList = JsonUtil.fromJson(partUserRaw.toString(), new TypeReference<>() {
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return ChatRoomResponse.builder()
                            .seq(((Number) row.get("seq")).longValue())
                            .partUserSeq(partUserSeqList)
                            .userSeq(row.get("user_seq") != null ? ((Number) row.get("user_seq")).longValue() : null)
                            .nickNames((String) row.get("nick_names"))
                            .unReadCount(row.get("unread_count") != null ? ((Number) row.get("unread_count")).intValue() : 0)
                            .lastMessage((String) row.get("last_message"))
                            .lastMessageDate(
                                    row.get("last_message_date") != null
                                            ? ((Timestamp) row.get("last_message_date")).toLocalDateTime()
                                            : null
                            )
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


    public ChatRoomResponse inviteChatRoom(Long chatRoomSeq, Long newPartUserSeq) {

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomSeq)
                .orElseThrow(() -> new ChatException(NOT_FOUND_CHAT_ROOM));

        if (chatRoom.getPartUserSeq().contains(newPartUserSeq)) {
            throw new ChatException(ALREADY_PARTICIPATION);
        }

        chatRoom.getPartUserSeq().add(newPartUserSeq);

        chatRoom = chatRoomRepository.save(
                ChatRoom.builder()
                        .partUserSeq(chatRoom.getPartUserSeq())
                        .updatedAt(LocalDateTime.now())
                        .build()
        );

        return ChatRoomResponse.builder()
                .seq(chatRoom.getSeq())
                .userSeq(chatRoom.getUser().getSeq())
                .partUserSeq(chatRoom.getPartUserSeq())
                .createdAt(chatRoom.getCreatedAt())
                .updatedAt(chatRoom.getUpdatedAt())
                .build();
    }

    public PageDTO<ChatMessageResponse> getChatMessageList(Long memberId, Long chatRoomSeq, int page) {

        userInfoRepository.findById(memberId)
                .orElseThrow(() -> new ChatException(NOT_FOUND_MEMBER));

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomSeq)
                .orElseThrow(() -> new ChatException(NOT_FOUND_CHAT_ROOM));

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<ChatMessage> chatMessageListPage = chatMessageRepository.findByChatRoomSeqOrderByCreatedAtDesc(chatRoom, pageable);

        List<ChatMessageResponse> chatMessages = chatMessageListPage.getContent().stream()
                .map(chatMessageList -> ChatMessageResponse.builder()
                        .seq(chatMessageList.getSeq())
                        .userSeq(chatMessageList.getUser().getSeq())
                        .message(chatMessageList.getMessage())
                        .chatRoomSeq(chatMessageList.getChatRoomSeq().getSeq())
                        .createdAt(chatMessageList.getCreatedAt())
                        .fileFlag(chatMessageList.getFileFlag())
                        .build())
                .collect(Collectors.toList());

        return PageDTO.<ChatMessageResponse>builder()
                .objectList(chatMessages)
                .totalPage(chatMessageListPage.getTotalPages())
                .build();
    }

    @Transactional
    public ChatMessageResponse saveChatMessage(Long memberId, ChatMessageRequest chatMessageDTO) {

        if(chatMessageDTO.getMessage().isEmpty()) {
            throw new ChatException(ABNORMAL_ACCESS);
        }

        UserInfo userInfo = userInfoRepository.findById(memberId)
                .orElseThrow(() -> new ChatException(NOT_FOUND_MEMBER));

        ChatRoom chatRoom = chatRoomRepository.findById(chatMessageDTO.getChatRoomSeq())
                .orElseThrow(() -> new ChatException(NOT_FOUND_CHAT_ROOM));

        ChatMessage newChatMessage = new ChatMessage();
        newChatMessage.setChatRoomSeq(chatRoom);
        newChatMessage.setMessage(chatMessageDTO.getMessage());
        newChatMessage.setUser(userInfo);

        if(chatMessageDTO.isFileFlag()) {
            newChatMessage.setFileFlag(true);
        }

        // 새로운 채팅 추가
        chatMessageRepository.save(newChatMessage);
        
        // 채팅방 수정일자 반영
        updateChatRoom(chatRoom.seq());

        // 채팅 읽은 회원 기록

        return ChatMessageResponse
                .builder()
                .seq(newChatMessage.seq())
                .userSeq(userInfo.getSeq())
                .message(newChatMessage.getMessage())
                .chatRoomSeq(chatRoom.seq())
                .createdAt(newChatMessage.getCreatedAt())
                .fileFlag(newChatMessage.getFileFlag())
                .build();
    }

    public void updateChatRoom(Long chatRoomSeq) {

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomSeq)
                .orElseThrow(() -> new ChatException(NOT_FOUND_CHAT_ROOM));

        chatRoom.setUpdatedAt(LocalDateTime.now());

//        chatRoomRepository.save(chatRoom);
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
                .orElseGet(() -> ChatRead.builder()
                        .chatRoomSeq(chatRoom)
                        .user(userInfo)
                        .lastReadMessageSeq(chatMessage)
                        .build()
                );
        chatRead.setLastReadMessageSeq(chatMessage);


        chatReadRepository.save(chatRead);
    }

    public List<Long> getPartUserInRoom(Long chatRoomSeq) {
        ChatRoom chatRoomPartUser = chatRoomRepository.findById(chatRoomSeq).orElseThrow();
        return chatRoomPartUser.getPartUserSeq();
    }

    public FileResponse saveFile(Long memberId, String type, MultipartFile file) throws IOException {

        UserInfo userInfo = userInfoRepository.findById(memberId)
                .orElseThrow(() -> new ChatException(NOT_FOUND_MEMBER));

        FileResponse imageDTO = fileUtil.saveImage(type, file);

        ChatMessageFile chatMessageFile = new ChatMessageFile();
        chatMessageFile.setChatFilePath(imageDTO.getPath());
        chatMessageFile.setUser(userInfo);
        chatMessageFile.setEncodedName(imageDTO.getEncodedName());
        chatMessageFile.setRealName(imageDTO.getRealName());


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
    public void chatSendFcmPush(ChatMessageResponse chatMessageResponse, List<Long> targetFcmUserSeq) {

        // 닉네임
        UserDetailInfo userDetailInfo = userInfoRepository.findById(chatMessageResponse.getUserSeq())
                .flatMap(userDetailInfoRepository::findByUser)
                .orElseThrow(() -> new ChatException(FAIL_TO_PUSH_FCM));

        List<UserInfo> targetFcmUserList = userInfoRepository.findAllBySeqIn(targetFcmUserSeq);
        if(targetFcmUserList.size() > 0) {

            String message = chatMessageResponse.getMessage();

            if(chatMessageResponse.isFileFlag()) {
                message = REPLACE_IMAGE_TEXT;
            }

            FcmPushRequest fcmPushRequest = FcmPushRequest
                    .builder()
                    .userSeqList(targetFcmUserSeq)
                    .title(userDetailInfo.getNickname())
                    .body(message)
                    .build();

            firebaseService.requestFcmPush(fcmPushRequest);
        }
    }
}

