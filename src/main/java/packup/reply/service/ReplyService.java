package packup.reply.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packup.auth.exception.AuthException;
import packup.auth.exception.AuthExceptionType;
import packup.common.domain.repository.CommonCodeRepository;
import packup.common.dto.PageDTO;
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

import static packup.reply.constant.ReplyConstant.PAGE_SIZE;
import static packup.reply.exception.ReplyExceptionType.*;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final CommonCodeRepository commonCodeRepository;
    private final UserInfoRepository userInfoRepository;
    private final TourInfoRepository tourInfoRepository;
    private final GuideInfoRepository guideInfoRepository;

    public PageDTO<ReplyResponse> getReplyList(ReplyRequest replyRequest, int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);

        TargetType targetType = replyRequest.getTargetType();
        Long targetSeq = replyRequest.getTargetSeq();

        if(validationExistContent(targetType, targetSeq)) {
            throw new ReplyException(getNotFoundErrorByTargetType(targetType));
        }

        String targetTypeCode = commonCodeRepository.findByCodeName(targetType.toString())
                .orElseThrow(() -> new AuthException(AuthExceptionType.INVALID_OAUTH_TYPE_CODE))
                .getCodeId();

        Page<Reply> replyList = replyRepository
                .findAllByTargetSeqAndTargetTypeAndDeleteFlagOrderByCreatedAtDesc(targetSeq, targetTypeCode, YnType.N, pageable);

        return PageDTO.<ReplyResponse>builder()
                .objectList(
                        replyList.stream()
                                .map(ReplyResponse::fromEntity)
                                .toList()
                )
                .totalPage(replyList.getTotalPages())
                .build();
    }

    public ReplyResponse getReply(Long memberId, Long replySeq) {
        Reply reply = getOwnedActiveReply(memberId, replySeq);

        return ReplyResponse.fromEntity(reply);
    }

    public ReplyResponse saveReply(Long memberId, ReplyRequest replyRequest) {
        TargetType targetType = replyRequest.getTargetType();
        Long targetSeq = replyRequest.getTargetSeq();
        String content = replyRequest.getContent();
        int point = replyRequest.getPoint();

        if(content == null || content.isBlank() || point < 1) {
            throw new ReplyException(ABNORMAL_ACCESS);
        }

        if(validationExistContent(targetType, targetSeq)) {
            throw new ReplyException(getNotFoundErrorByTargetType(targetType));
        }
        
        // 해당 회원이 댓글 등록 가능한지 확인 로직 추가

        UserInfo user = userInfoRepository.findById(memberId)
                .orElseThrow(() -> new ReplyException(NOT_FOUND_MEMBER));

        String commonCode = commonCodeRepository.findByCodeName(targetType.name())
                .orElseThrow(() -> new ReplyException(INVALID_REPLY_TYPE))
                .getCodeId();

        Reply newReply = Reply.of(
                user, targetSeq, commonCode, content, point
        );

        replyRepository.save(newReply);

        return ReplyResponse.fromEntity(newReply);
    }

    @Transactional
    public ReplyResponse updateReply(Long memberId, Long replySeq, ReplyRequest replyRequest) {

        if(replySeq == null) {
            throw new ReplyException(ABNORMAL_ACCESS);
        }

        Reply reply = getOwnedActiveReply(memberId, replySeq)
                .updateContent(replyRequest.getContent(), replyRequest.getPoint());

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
