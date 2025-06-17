package packup.reply.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/view/{replySeq}")
    public ResultModel<ReplyResponse> getReply(@Auth Long memeberId, @PathVariable Long replySeq) {

        return ResultModel.success(replyService.getReply(memeberId, replySeq));
    }

    @PostMapping("/save")
    public ResultModel<ReplyResponse> saveReply(@Auth Long memberId, ReplyRequest replyRequest) {
        return ResultModel.success(replyService.saveReply(memberId, replyRequest));
    }

    @PutMapping("/update")
    public ResultModel<ReplyResponse> updateReply(@Auth Long memberId, ReplyRequest replyRequest) {
        return ResultModel.success(replyService.updateReply(memberId, replyRequest));
    }

    @PutMapping("/delete")
    public ResultModel<Void> deleteReply(@Auth Long memberId, Long replySeq) {
        replyService.deleteReply(memberId, replySeq);
        return ResultModel.success();
    }
}
