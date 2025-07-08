package packup.alert.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import packup.alert.domain.Alert;
import packup.alert.domain.repository.AlertRepository;
import packup.alert.dto.AlertResponse;
import packup.common.dto.PageDTO;
import packup.common.enums.YnType;

import java.util.List;

import static packup.notice.constant.NoticeConstant.PAGE_SIZE;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository alertLogRepository;

    public PageDTO<AlertResponse> alertCenter(Long memberId, Integer page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);

        Page<Alert> alertLogPage = alertLogRepository.findAllByUserSeqAndReadFlagOrderByCreatedAtDesc(memberId, YnType.N, pageable);

        List<AlertResponse> alertLogResponses = alertLogPage.getContent().stream().map(
                alertLog -> AlertResponse.builder()
                        .seq(alertLog.seq())
                        .alertType(alertLog.getAlertType())
                        .createdAt(alertLog.getCreatedAt())
                        .build()).toList();


        return PageDTO.<AlertResponse>builder()
                .objectList(alertLogResponses)
                .totalPage(alertLogPage.getTotalPages())
                .build();

    }

}
