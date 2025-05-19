package packup.notice.presentatioin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import packup.common.dto.PageDTO;
import packup.common.dto.ResultModel;
import packup.notice.dto.NoticeResponse;
import packup.notice.service.NoticeService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notice")
public class NoticeController {

    private final NoticeService noticeService;

    public ResultModel<PageDTO<NoticeResponse>> getNoticeList(@RequestParam int page) {

        return ResultModel.success(noticeService.getNoticeList(page));
    }

}
