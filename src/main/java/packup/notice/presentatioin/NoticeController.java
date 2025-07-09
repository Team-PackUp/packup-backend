package packup.notice.presentatioin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import packup.auth.annotation.Auth;
import packup.common.dto.PageDTO;
import packup.common.dto.ResultModel;
import packup.notice.dto.NoticeResponse;
import packup.notice.exception.NoticeException;
import packup.notice.service.NoticeService;

import static packup.notice.exception.NoticeExceptionType.ABNORMAL_ACCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notice")
public class NoticeController {

    private final NoticeService noticeService;


    @GetMapping("/list")
    public ResultModel<PageDTO<NoticeResponse>> getNoticeList(@RequestParam Integer page) {

        if(page == null) {
            throw new NoticeException(ABNORMAL_ACCESS);
        }

        return ResultModel.success(noticeService.getNoticeList(page));
    }

    @GetMapping("/view/{noticeSeq}")
    public ResultModel<NoticeResponse> getNoticeView(@Auth Long memberId, @PathVariable Long noticeSeq) {

        if(noticeSeq == null) {
            throw new NoticeException(ABNORMAL_ACCESS);
        }

        return ResultModel.success(noticeService.getNoticeView(memberId, noticeSeq));
    }

}
