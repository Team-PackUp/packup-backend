package packup.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import packup.chat.domain.ChatMessage;
import packup.chat.domain.ChatMessageFile;
import packup.chat.domain.ChatRoom;
import packup.chat.domain.repository.ChatMessageFileRepository;
import packup.chat.domain.repository.ChatMessageRepository;
import packup.chat.domain.repository.ChatRoomRepository;
import packup.chat.dto.ChatMessageRequest;
import packup.chat.dto.ChatMessageResponse;
import packup.chat.dto.ChatRoomResponse;
import packup.chat.exception.ChatException;
import packup.common.dto.FileResponse;
import packup.common.dto.PageDTO;
import packup.common.util.FileUtil;
import packup.fcmpush.dto.FcmPushRequest;
import packup.fcmpush.service.FcmPushService;
import packup.user.domain.UserDetailInfo;
import packup.user.domain.UserInfo;
import packup.user.domain.repository.UserDetailInfoRepository;
import packup.user.domain.repository.UserInfoRepository;

import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
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

    private final FileUtil fileUtil;

    private final UserDetailInfoRepository userDetailInfoRepository;
    private final FcmPushService firebaseService;

    public ChatRoomResponse getChatRoom(Long chatRoomSeq) {

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomSeq)
                .orElseThrow(() -> new ChatException(NOT_FOUND_CHAT_ROOM));

        UserInfo userInfo = userInfoRepository.findById(chatRoom.getUser().getSeq())
                .orElseThrow(() -> new ChatException(NOT_FOUND_MEMBER));

        return ChatRoomResponse.builder()
                .seq(chatRoom.getSeq())
                .userSeq(userInfo.getSeq())
                .partUserSeq(chatRoom.getPartUserSeq())
                .createdAt(chatRoom.getCreatedAt())
                .updatedAt(chatRoom.getUpdatedAt())
                .build();
    }


    public PageDTO<ChatRoomResponse> getChatRoomList(Long memberId, int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<ChatRoom> chatRoomListPage = chatRoomRepository.findByPartUserSeqContains(memberId, pageable);

        List<ChatRoomResponse> chatRooms = chatRoomListPage.getContent().stream()
                .map(chatRoom -> ChatRoomResponse.builder()
                        .seq(chatRoom.getSeq())
                        .partUserSeq(chatRoom.getPartUserSeq())
                        .createdAt(chatRoom.getCreatedAt())
                        .updatedAt(chatRoom.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());

        return PageDTO.<ChatRoomResponse>builder()
                .objectList(chatRooms)
                .totalPage(chatRoomListPage.getTotalPages())
                .build();
    }



    public ChatRoomResponse createChatRoom(List<Long> partUserSeq, Long userSeq) {

        UserInfo userInfo = userInfoRepository.findById(userSeq)
                .orElseThrow(() -> new ChatException(NOT_FOUND_MEMBER));

        ChatRoom chatRoom = ChatRoom.builder()
                .partUserSeq(partUserSeq)
                .user(userInfo)
                .build();

        ChatRoom newChatRoom = chatRoomRepository.save(chatRoom);

        return ChatRoomResponse.builder()
                .seq(newChatRoom.getSeq())
                .userSeq(newChatRoom.getUser().getSeq())
                .partUserSeq(newChatRoom.getPartUserSeq())
                .createdAt(newChatRoom.getCreatedAt())
                .updatedAt(newChatRoom.getUpdatedAt())
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

        chatMessageRepository.save(newChatMessage);
        updateChatRoom(chatRoom.seq());

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

        chatRoomRepository.save(chatRoom);
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

            List<Long> targetUserSeqList =  targetFcmUserList.stream()
                    .map(UserInfo::seq)
                    .collect(Collectors.toList());

            FcmPushRequest fcmPushRequest = FcmPushRequest
                    .builder()
                    .userSeqList(targetUserSeqList)
                    .title(userDetailInfo.getNickname())
                    .body(message)
                    .build();

            firebaseService.requestFcmPush(fcmPushRequest);
        }
    }
}

