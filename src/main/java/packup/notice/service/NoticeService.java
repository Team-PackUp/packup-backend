package packup.notice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import packup.common.dto.PageDTO;
import packup.notice.domain.Notice;
import packup.notice.domain.repository.NoticeRepository;
import packup.notice.dto.NoticeResponse;

import java.util.List;
import java.util.stream.Collectors;

import static packup.notice.constant.NoticeConstant.PAGE_SIZE;


@Service
public class NoticeService {

    private NoticeRepository noticeRepository;

    public PageDTO<NoticeResponse> getNoticeList(int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);

        Page<Notice> responseNoticePage = noticeRepository.findAllBySeqOrderByCreatedAtDesc(pageable);

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

}
