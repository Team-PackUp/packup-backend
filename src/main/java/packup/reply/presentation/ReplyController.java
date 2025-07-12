package packup.reply.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import packup.auth.annotation.Auth;
import packup.common.dto.PageDTO;
import packup.common.dto.ResultModel;
import packup.reply.dto.ReplyRequest;
import packup.reply.dto.ReplyResponse;
import packup.reply.exception.ReplyException;
import packup.reply.service.ReplyService;

import static packup.reply.exception.ReplyExceptionType.ABNORMAL_ACCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reply")
public class ReplyController {

    private final ReplyService replyService;

    @GetMapping("/list")
    public ResultModel<PageDTO<ReplyResponse>> getReplyList(ReplyRequest replyRequest, @RequestParam Integer page) {

        return ResultModel.success(replyService.getReplyList(replyRequest, page));
    }

    @GetMapping("/view/{replySeq}")
    public ResultModel<ReplyResponse> getReply(@Auth Long memberId, @PathVariable Long replySeq) {

        if(replySeq == null) {
            throw new ReplyException(ABNORMAL_ACCESS);
        }

        return ResultModel.success(replyService.getReply(replySeq));
    }

    @PostMapping("/save")
    public ResultModel<ReplyResponse> saveReply(@Auth Long memberId, @RequestBody @Valid ReplyRequest replyRequest) {
        System.out.println(replyRequest.getFcmPushRequest().getBody());
        System.out.println(replyRequest.getFcmPushRequest().getTitle());
        return ResultModel.success(replyService.saveReply(memberId, replyRequest));
    }

    @PutMapping("/update/{replySeq}")
    public ResultModel<ReplyResponse> updateReply(@Auth Long memberId,  @PathVariable Long replySeq, @RequestBody @Valid ReplyRequest replyRequest) {
        return ResultModel.success(replyService.updateReply(memberId, replySeq, replyRequest));
    }

    @DeleteMapping("/delete/{replySeq}")
    public ResultModel<Void> deleteReply(@Auth Long memberId, @PathVariable Long replySeq) {

        if(replySeq == null) {
            throw new ReplyException(ABNORMAL_ACCESS);
        }

        replyService.deleteReply(memberId, replySeq);
        return ResultModel.success();
    }
}
