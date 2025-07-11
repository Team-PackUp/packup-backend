package packup.alert.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Limit;
import org.springframework.web.bind.annotation.*;
import packup.alert.domain.Alert;
import packup.alert.dto.AlertResponse;
import packup.alert.exception.AlertException;
import packup.alert.service.AlertService;
import packup.auth.annotation.Auth;
import packup.common.dto.PageDTO;
import packup.common.dto.ResultModel;

import java.util.List;

import static packup.alert.constant.AlertConstant.BELL_COUNT;
import static packup.alert.constant.AlertConstant.PAGE_SIZE;
import static packup.alert.exception.AlertExceptionType.ABNORMAL_ACCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/alert")
public class AlertController {

    private final AlertService alertService;

    @GetMapping("/count")
    public ResultModel<Long> count(@Auth Long memberId) {

        return ResultModel.success(alertService.count(memberId, Limit.of(BELL_COUNT)));
    }

    @GetMapping("/list")
    public ResultModel<PageDTO<AlertResponse>> alertCenter(@Auth Long memberId) {

        return ResultModel.success(alertService.alertCenter(memberId));
    }
//
//    @PutMapping("/mark_read/{alertSeq}")
//    public ResultModel<Alert> markRead(@Auth Long memberId, @PathVariable Long alertSeq) {
//        if(alertSeq == null) {
//            throw new AlertException(ABNORMAL_ACCESS);
//        }
//
//        return ResultModel.success(alertService.markRead(memberId, alertSeq));
//    }
}
