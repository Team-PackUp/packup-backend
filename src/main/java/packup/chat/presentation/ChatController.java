package packup.chat.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import packup.auth.annotation.Auth;
import packup.chat.dto.ChatInviteRequestDTO;
import packup.chat.dto.ChatMessageDTO;
import packup.chat.dto.ChatRoomDTO;
import packup.chat.service.ChatService;
import packup.common.dto.FileDTO;
import packup.common.dto.PageDTO;
import packup.common.dto.ResultModel;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/room/list")
    public ResultModel<PageDTO<ChatRoomDTO>> getChatRoomList(@Auth Long memberId, @RequestParam int page) {

        return ResultModel.success(chatService.getChatRoomList(memberId, page));
    }

    @PostMapping("/room/create")
    public ResultModel<ChatRoomDTO> createChatRoom(@Auth Long memberId, @RequestBody List<Long> partUserSeq) {

        return ResultModel.success(chatService.createChatRoom(partUserSeq, memberId));
    }

    @PostMapping("/room/invite")
    public ResultModel<ChatRoomDTO> inviteChatRoom(@RequestBody ChatInviteRequestDTO inviteRequest) {

        ChatRoomDTO chatRoomDTO = chatService.inviteChatRoom(
                inviteRequest.getChatRoomSeq(),
                inviteRequest.getNewPartUserSeq()
        );

        return ResultModel.success(chatRoomDTO);
    }

    @GetMapping("/message/list/{chatRoomSeq}")
    public ResultModel<PageDTO<ChatMessageDTO>> getChatMessageList(@Auth Long memberId, @PathVariable Long chatRoomSeq, @RequestParam int page) {

        return ResultModel.success(chatService.getChatMessageList(memberId, chatRoomSeq, page));
    }

    @PostMapping("/message/save/file")
    public ResultModel<FileDTO> saveFile(@Auth Long memberId, @RequestParam("file") MultipartFile file) throws IOException {

        return ResultModel.success(chatService.saveFile(memberId, "chat", file));
    }
}
