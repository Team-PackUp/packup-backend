package packup.reply.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packup.common.domain.repository.CommonCodeRepository;
import packup.common.enums.YnType;
import packup.guide.domain.repository.GuideInfoRepository;
import packup.reply.domain.Reply;
import packup.reply.domain.repository.ReplyRepository;
import packup.reply.dto.ReplyRequest;
import packup.reply.dto.ReplyResponse;
import packup.reply.enums.TargetType;
import packup.reply.exception.ReplyException;
import packup.reply.exception.ReplyExceptionType;
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

        if(validationExistContent(targetType, targetSeq)) {
            throw new ReplyException(getNotFoundErrorByTargetType(targetType));
        }

        List<Reply> replyList = replyRepository
                .findAllByTargetSeqAndTargetTypeAndDeleteFlagOrderByCreatedAtDesc(targetSeq, targetType, YnType.N)
                .orElse(Collections.emptyList());

        return replyList.stream()
                .map(ReplyResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public ReplyResponse getReply(Long memberId, Long replySeq) {
        Reply reply = getOwnedActiveReply(memberId, replySeq);

        return ReplyResponse.fromEntity(reply);
    }

    public ReplyResponse saveReply(Long memberId, ReplyRequest replyRequest) {
        TargetType targetType = replyRequest.getTargetType();
        Long targetSeq = replyRequest.getTargetSeq();
        String content = replyRequest.getContent();

        if(content == null) {
            throw new ReplyException(ABNORMAL_ACCESS);
        }

        if(validationExistContent(targetType, targetSeq)) {
            throw new ReplyException(getNotFoundErrorByTargetType(targetType));
        }

        UserInfo user = userInfoRepository.findById(memberId)
                .orElseThrow(() -> new ReplyException(NOT_FOUND_MEMBER));

        String commonCode = commonCodeRepository.findByCodeName(targetType.name())
                .orElseThrow(() -> new ReplyException(INVALID_REPLY_TYPE))
                .getCodeId();

        Reply newReply = Reply.of(
                user, targetSeq, commonCode, content
        );

        replyRepository.save(newReply);

        return ReplyResponse.fromEntity(newReply);
    }

    @Transactional
    public ReplyResponse updateReply(Long memberId, ReplyRequest replyRequest) {
        Long replySeq = replyRequest.getSeq();

        if(replySeq == null) {
            throw new ReplyException(ABNORMAL_ACCESS);
        }

        Reply reply = getOwnedActiveReply(memberId, replySeq)
                .updateContent(replyRequest.getContent());

        return ReplyResponse.fromEntity(reply);
    }

    @Transactional
    public void deleteReply(Long memberId, Long replySeq) {
        getOwnedActiveReply(memberId, replySeq).deleteContent();
    }

    private Reply getOwnedActiveReply(Long memberId, Long replySeq) {
        UserInfo user = userInfoRepository.findById(memberId)
                .orElseThrow(() -> new ReplyException(NOT_FOUND_MEMBER));

        return replyRepository.findFirstBySeqAndUserAndDeleteFlag(replySeq, user, YnType.N)
                .orElseThrow(() -> new ReplyException(NOT_FOUND_REPLY));
    }

    private boolean validationExistContent(TargetType targetType,Long targetSeq)  {

        if (targetType == null || targetSeq == null) {
            return true;
        }

        try {
            switch (targetType) {
                case REPLY_TOUR -> tourInfoRepository.findById(targetSeq)
                        .orElseThrow();
                case REPLY_GUIDE -> guideInfoRepository.findById(targetSeq)
                        .orElseThrow();
                default -> {
                    return true;
                }
            }
        } catch (Exception e) {
            return true;
        }

        return false;
    }

    private ReplyExceptionType getNotFoundErrorByTargetType(TargetType targetType) {
        return switch (targetType) {
            case REPLY_TOUR -> NOT_FOUND_TOUR;
            case REPLY_GUIDE -> NOT_FOUND_GUIDE;
        };
    }

}
