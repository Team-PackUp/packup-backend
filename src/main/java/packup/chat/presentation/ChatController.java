package packup.chat.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import packup.auth.annotation.Auth;
import packup.chat.dto.ChatMessageResponse;
import packup.chat.dto.ChatRoomResponse;
import packup.chat.dto.InviteRequest;
import packup.chat.exception.ChatException;
import packup.chat.service.ChatService;
import packup.common.dto.FileResponse;
import packup.common.dto.PageDTO;
import packup.common.dto.ResultModel;

import java.io.IOException;

import static packup.chat.exception.ChatExceptionType.ABNORMAL_ACCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/room/list")
    public ResultModel<PageDTO<ChatRoomResponse>> getChatRoomList(@Auth Long memberId, @RequestParam Integer page) {

        if(page == null) {
            throw new ChatException(ABNORMAL_ACCESS);
        }

        return ResultModel.success(chatService.getChatRoomList(memberId, page));
    }

    @PostMapping("/room/invite")
    public ResultModel<ChatRoomResponse> inviteChatRoom(@RequestBody InviteRequest inviteRequest) {

        ChatRoomResponse chatRoomDTO = chatService.inviteChatRoom(
                inviteRequest.getChatRoomSeq(),
                inviteRequest.getNewPartUserSeq()
        );

        return ResultModel.success(chatRoomDTO);
    }

    @GetMapping("/message/list/{chatRoomSeq}")
    public ResultModel<PageDTO<ChatMessageResponse>> getChatMessageList(@Auth Long memberId, @PathVariable Long chatRoomSeq, @RequestParam Integer page) {

        if(chatRoomSeq == null || page == null) {
            throw new ChatException(ABNORMAL_ACCESS);
        }

        return ResultModel.success(chatService.getChatMessageList(memberId, chatRoomSeq, page));
    }

    @PostMapping("/message/save/file")
    public ResultModel<FileResponse> saveFile(@Auth Long memberId, @RequestParam("file") MultipartFile file) throws IOException {

        if(file == null || file.isEmpty()) {
            throw new ChatException(ABNORMAL_ACCESS);
        }

        return ResultModel.success(chatService.saveFile(memberId, "chat", file));
    }
}
