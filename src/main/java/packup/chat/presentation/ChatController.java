package packup.chat.presentation;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import packup.chat.dto.ChatInviteRequestDTO;
import packup.chat.dto.ChatRoomDTO;
import packup.chat.service.ChatService;
import packup.config.security.provider.JwtTokenProvider;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    private final JwtTokenProvider jwtTokenProvider;


    @GetMapping("/room/list")
    public List<ChatRoomDTO> getChatRoomList(HttpServletRequest request) {
        System.out.println("test");
//        String token = jwtTokenProvider.resolveToken(request);
        long userSeq = Integer.parseInt("1");

        return chatService.getChatRoomList(userSeq);
    }

    @PostMapping("/room/create")
    public ChatRoomDTO createChatRoom(HttpServletRequest request, @RequestBody List<Long> partUserSeq) {

        String token = jwtTokenProvider.resolveToken(request);
        long userSeq = Integer.parseInt(jwtTokenProvider.getUsername(token));


        return chatService.createChatRoom(partUserSeq, userSeq);
    }

    @PostMapping("/room/invite")
    public ChatRoomDTO inviteChatRoom(@RequestBody ChatInviteRequestDTO inviteRequest) {

        ChatRoomDTO chatRoomDTO = chatService.inviteChatRoom(inviteRequest.getChatRoomSeq(), inviteRequest.getNewPartUserSeq());

        return chatRoomDTO;
    }

}
