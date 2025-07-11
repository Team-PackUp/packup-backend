package packup.alert.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packup.alert.domain.Alert;
import packup.alert.domain.repository.AlertRepository;
import packup.alert.dto.AlertResponse;
import packup.alert.enums.AlertType;
import packup.common.dto.PageDTO;
import packup.common.enums.YnType;

import java.util.List;

import static packup.alert.constant.AlertConstant.PAGE_SIZE;


@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository alertLogRepository;

    public Long count(Long memberId, Limit limitCount) {
        return alertLogRepository.countAllByUserSeqAndReadFlag(memberId, YnType.N, limitCount);
    }

    @Transactional
    public PageDTO<AlertResponse> alertCenter(Long memberId) {
        Pageable pageable = PageRequest.of(0, PAGE_SIZE);

        Page<Alert> alertLogPage =
                alertLogRepository.findAllByUserSeqAndReadFlagOrderByCreatedAtDesc(
                        memberId, YnType.N, pageable);

        markRead(alertLogPage);

        List<AlertResponse> alertLogResponses = alertLogPage.stream()
                .map(a -> AlertResponse.builder()
                        .seq(a.seq())
                        .alertType(AlertType.fromCode(a.getAlertType()).text())
                        .createdAt(a.getCreatedAt())
                        .build())
                .toList();

        return PageDTO.<AlertResponse>builder()
                .objectList(alertLogResponses)
                .totalPage(alertLogPage.getTotalPages())
                .totalElements(alertLogPage.getTotalElements())
                .curPage(alertLogPage.getNumber())
                .build();
    }



    private void markRead(Page<Alert> alertLogPage) {
        alertLogPage.getContent().forEach(Alert::markRead);
    }
}
