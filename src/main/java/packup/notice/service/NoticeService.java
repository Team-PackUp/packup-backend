package packup.notice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import packup.common.dto.PageDTO;
import packup.common.enums.YnType;
import packup.notice.domain.Notice;
import packup.notice.domain.repository.NoticeRepository;
import packup.notice.dto.NoticeResponse;
import packup.notice.exception.NoticeException;
import packup.recommend.annotation.RecommendTrace;
import packup.recommend.enums.ActionType;

import java.util.List;
import java.util.stream.Collectors;

import static packup.notice.constant.NoticeConstant.PAGE_SIZE;
import static packup.notice.exception.NoticeExceptionType.NOT_FOUND_NOTICE;


@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public PageDTO<NoticeResponse> getNoticeList(int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);

        Page<Notice> responseNoticePage = noticeRepository.findAllByDeleteFlagOrderByCreatedAtDesc(YnType.N, pageable);

        List<NoticeResponse> noticeResponses = responseNoticePage.getContent().stream()
                .map(notice -> NoticeResponse.builder()
                        .seq(notice.getSeq())
                        .title(notice.getTitle())
                        .content(notice.getContent())
                        .createdAt(notice.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        return PageDTO.<NoticeResponse>builder()
                .objectList(noticeResponses)
                .totalPage(responseNoticePage.getTotalPages())
                .build();
    }

    @RecommendTrace(actionType = ActionType.CLICK)
    public NoticeResponse getNoticeView(Long memberId, Long noticeSeq) {

        Notice responseNotice = noticeRepository.findById(noticeSeq)
                .orElseThrow(() -> new NoticeException(NOT_FOUND_NOTICE));

        return NoticeResponse.builder()
                .seq(responseNotice.getSeq())
                .title(responseNotice.getTitle())
                .content(responseNotice.getContent())
                .createdAt(responseNotice.getCreatedAt())
                .build();
    }

}
