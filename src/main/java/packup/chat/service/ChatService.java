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
import packup.chat.dto.ChatMessageDTO;
import packup.chat.dto.ChatRoomDTO;
import packup.chat.exception.ChatException;
import packup.common.dto.FileDTO;
import packup.common.dto.PageDTO;
import packup.common.util.FileUtil;
import packup.user.domain.UserInfo;
import packup.user.domain.repository.UserInfoRepository;

import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static packup.chat.constant.ChatConstant.PAGE_SIZE;
import static packup.chat.exception.ChatExceptionType.*;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserInfoRepository userInfoRepository;
    private final ChatMessageFileRepository chatMessageFileRepository;

    public ChatRoomDTO getChatRoom(Long chatRoomSeq) {

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomSeq)
                .orElseThrow(() -> new ChatException(NOT_FOUND_CHAT_ROOM));

        UserInfo userInfo = userInfoRepository.findById(chatRoom.getUserSeq().getSeq())
                .orElseThrow(() -> new ChatException(NOT_FOUND_MEMBER));

        return ChatRoomDTO.builder()
                .seq(chatRoom.getSeq())
                .userSeq(userInfo.getSeq())
                .partUserSeq(chatRoom.getPartUserSeq())
                .createdAt(chatRoom.getCreatedAt())
                .updatedAt(chatRoom.getUpdatedAt())
                .build();
    }


    public PageDTO<ChatRoomDTO> getChatRoomList(Long memberId, int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<ChatRoom> chatRoomListPage = chatRoomRepository.findByPartUserSeqContains(memberId, pageable);

        List<ChatRoomDTO> chatRooms = chatRoomListPage.getContent().stream()
                .map(chatRoom -> ChatRoomDTO.builder()
                        .seq(chatRoom.getSeq())
                        .partUserSeq(chatRoom.getPartUserSeq())
                        .createdAt(chatRoom.getCreatedAt())
                        .updatedAt(chatRoom.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());

        return PageDTO.<ChatRoomDTO>builder()
                .objectList(chatRooms)
                .totalPage(chatRoomListPage.getTotalPages())
                .build();
    }



    public ChatRoomDTO createChatRoom(List<Long> partUserSeq, Long userSeq) {

        UserInfo userInfo = userInfoRepository.findById(userSeq)
                .orElseThrow(() -> new ChatException(NOT_FOUND_MEMBER));

        ChatRoom chatRoom = ChatRoom.builder()
                .partUserSeq(partUserSeq)
                .userSeq(userInfo)
                .build();

        ChatRoom newChatRoom = chatRoomRepository.save(chatRoom);

        return ChatRoomDTO.builder()
                .seq(newChatRoom.getSeq())
                .userSeq(newChatRoom.getUserSeq().getSeq())
                .partUserSeq(newChatRoom.getPartUserSeq())
                .createdAt(newChatRoom.getCreatedAt())
                .updatedAt(newChatRoom.getUpdatedAt())
                .build();
    }

    public ChatRoomDTO inviteChatRoom(Long chatRoomSeq, Long newPartUserSeq) {

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

        return ChatRoomDTO.builder()
                .seq(chatRoom.getSeq())
                .userSeq(chatRoom.getUserSeq().getSeq())
                .partUserSeq(chatRoom.getPartUserSeq())
                .createdAt(chatRoom.getCreatedAt())
                .updatedAt(chatRoom.getUpdatedAt())
                .build();
    }

    public PageDTO<ChatMessageDTO> getChatMessageList(Long memberId, Long chatRoomSeq, int page) {

        UserInfo userInfo = userInfoRepository.findById(memberId)
                .orElseThrow(() -> new ChatException(NOT_FOUND_MEMBER));

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomSeq)
                .orElseThrow(() -> new ChatException(NOT_FOUND_CHAT_ROOM));

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<ChatMessage> chatMessageListPage = chatMessageRepository.findByChatRoomSeqOrderByCreatedAtDesc(chatRoom, pageable);

        List<ChatMessageDTO> chatMessages = chatMessageListPage.getContent().stream()
                .map(chatMessageList -> ChatMessageDTO.builder()
                        .seq(chatMessageList.getSeq())
                        .userSeq(userInfo.getSeq())
                        .message(chatMessageList.getMessage())
                        .chatRoomSeq(chatMessageList.getChatRoomSeq().getSeq())
                        .createdAt(chatMessageList.getCreatedAt())
                        .fileFlag(chatMessageList.getFileFlag())
                        .build())
                .collect(Collectors.toList());

        return PageDTO.<ChatMessageDTO>builder()
                .objectList(chatMessages)
                .totalPage(chatMessageListPage.getTotalPages())
                .build();
    }

    @Transactional
    public ChatMessageDTO saveChatMessage(Long memberId, ChatMessageDTO chatMessageDTO) {

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
        newChatMessage.setUserSeq(userInfo);

        if(chatMessageDTO.isFileFlag()) {
            newChatMessage.setFileFlag(true);
        }

        chatMessageRepository.save(newChatMessage);
        updateChatRoom(chatRoom.seq());

        return ChatMessageDTO
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

    public FileDTO saveFile(Long memberId, String type, MultipartFile file) throws IOException {

        UserInfo userInfo = userInfoRepository.findById(memberId)
                .orElseThrow(() -> new ChatException(NOT_FOUND_MEMBER));

        FileDTO imageDTO = FileUtil.saveImage(type, file);

        ChatMessageFile chatMessageFile = new ChatMessageFile();
        chatMessageFile.setChatFilePath(imageDTO.getPath());
        chatMessageFile.setUserSeq(userInfo);
        chatMessageFile.setEncodedName(imageDTO.getEncodedName());
        chatMessageFile.setRealName(imageDTO.getRealName());


        chatMessageFileRepository.save(chatMessageFile);

        return FileDTO
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
}

