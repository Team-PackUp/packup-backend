package packup.reply.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import packup.common.domain.repository.CommonCodeRepository;
import packup.common.enums.YnType;
import packup.guide.domain.repository.GuideInfoRepository;
import packup.reply.domain.Reply;
import packup.reply.domain.repository.ReplyRepository;
import packup.reply.dto.ReplyRequest;
import packup.reply.dto.ReplyResponse;
import packup.reply.enums.TargetType;
import packup.reply.exception.ReplyException;
import packup.tour.domain.repositoroy.TourInfoRepository;
import packup.user.domain.UserInfo;
import packup.user.domain.repository.UserInfoRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static packup.reply.exception.ReplyExceptionType.*;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final CommonCodeRepository commonCodeRepository;
    private final UserInfoRepository userInfoRepository;
    private final TourInfoRepository tourInfoRepository;
    private final GuideInfoRepository guideInfoRepository;

    public List<ReplyResponse> getReplyList(ReplyRequest replyRequest) {
        TargetType targetType = replyRequest.getTargetType();
        Long targetSeq = replyRequest.getTargetSeq();

        if(targetType == null || targetSeq == null) {
            throw new ReplyException(ABNORMAL_ACCESS);
        }

        List<Reply> replyList = replyRepository
                .findAllByTargetSeqAndTargetTypeAndDeleteFlagOrderByCreatedAtDesc(targetSeq, targetType, YnType.N)
                .orElse(Collections.emptyList());

        return replyList.stream()
                .map(reply -> ReplyResponse.builder()
                        .targetSeq(reply.getSeq())
                        .targetType(reply.getTargetType())
                        .content(reply.getContent())
                        .createdAt(reply.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    public ReplyResponse saveReply(Long memberId, ReplyRequest replyRequest) {
        TargetType targetType = replyRequest.getTargetType();
        Long targetSeq = replyRequest.getTargetSeq();
        String content = replyRequest.getContent();

        if(targetType == null || targetSeq == null || content == null) {
            throw new ReplyException(ABNORMAL_ACCESS);
        }

        UserInfo user = userInfoRepository.findById(memberId)
                .orElseThrow(() -> new ReplyException(NOT_FOUND_MEMBER));

        switch (targetType) {
            case REPLY_TOUR -> {
                tourInfoRepository.findById(targetSeq)
                        .orElseThrow(() -> new ReplyException(NOT_FOUND_TOUR));
            }
            case REPLY_GUIDE -> {
                guideInfoRepository.findById(targetSeq)
                        .orElseThrow(() -> new ReplyException(NOT_FOUND_GUIDE));
            }
        }

        String commonCode = commonCodeRepository.findByCodeName(targetType.name())
                .orElseThrow(() -> new ReplyException(INVALID_REPLY_TYPE))
                .getCodeId();


        Reply newReply = Reply.of(
                user, targetSeq, commonCode, content
        );

        replyRepository.save(newReply);

        return ReplyResponse.builder()
                .seq(newReply.seq())
                .targetType(newReply.getTargetType())
                .targetSeq(newReply.getTargetSeq())
                .content(newReply.getContent())
                .createdAt(newReply.createdDate())
                .build();
    }

}
