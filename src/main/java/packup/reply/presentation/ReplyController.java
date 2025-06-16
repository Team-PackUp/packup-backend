package packup.reply.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import packup.auth.annotation.Auth;
import packup.common.dto.ResultModel;
import packup.reply.dto.ReplyRequest;
import packup.reply.dto.ReplyResponse;
import packup.reply.service.ReplyService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reply")
public class ReplyController {

    ReplyService replyService;

    @GetMapping("/list")
    public ResultModel<List<ReplyResponse>> getReplyList(ReplyRequest replyRequest) {

        return ResultModel.success(replyService.getReplyList(replyRequest));
    }

    @PostMapping("/save")
    public ResultModel<ReplyResponse> saveReply(@Auth Long memberId, ReplyRequest replyRequest) {
        return ResultModel.success(replyService.saveReply(memberId, replyRequest));
    }
}
