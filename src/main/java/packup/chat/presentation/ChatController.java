package packup.chat.presentation;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import packup.chat.dto.ChatInviteRequestDTO;
import packup.chat.dto.ChatMessageDTO;
import packup.chat.dto.ChatRoomDTO;
import packup.chat.service.ChatService;
import packup.common.dto.ResultModel;
import packup.config.security.provider.JwtTokenProvider;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    private final JwtTokenProvider jwtTokenProvider;


    @GetMapping("/room/list")
    public ResultModel<List<ChatRoomDTO>> getChatRoomList(HttpServletRequest request) {
//        String token = jwtTokenProvider.resolveToken(request);
        long userSeq = Integer.parseInt("2");

        return ResultModel.success(chatService.getChatRoomList(userSeq));
    }

    @PostMapping("/room/create")
    public ResultModel<ChatRoomDTO> createChatRoom(HttpServletRequest request, @RequestBody List<Long> partUserSeq) {

        String token = jwtTokenProvider.resolveToken(request);
        long userSeq = Integer.parseInt(jwtTokenProvider.getUsername(token));


        return ResultModel.success(chatService.createChatRoom(partUserSeq, userSeq));
    }

    @PostMapping("/room/invite")
    public ResultModel<ChatRoomDTO> inviteChatRoom(@RequestBody ChatInviteRequestDTO inviteRequest) {

        ChatRoomDTO chatRoomDTO = chatService.inviteChatRoom(inviteRequest.getChatRoomSeq(), inviteRequest.getNewPartUserSeq());

        return ResultModel.success(chatRoomDTO);
    }

    @GetMapping("/message/list/{chatRoomSeq}")
    public ResultModel<List<ChatMessageDTO>> getChatMessageList(@PathVariable Long chatRoomSeq) {

        return ResultModel.success(chatService.getChatMessageList(chatRoomSeq));
    }
}
