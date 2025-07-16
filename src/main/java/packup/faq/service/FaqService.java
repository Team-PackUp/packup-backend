package packup.faq.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import packup.common.domain.CommonCode;
import packup.common.domain.repository.CommonCodeRepository;
import packup.common.enums.YnType;
import packup.faq.domain.Faq;
import packup.faq.domain.repository.FaqRepository;
import packup.faq.dto.FaqCategoryResponse;
import packup.faq.dto.FaqResponse;
import packup.faq.enums.FaqEnum;
import packup.faq.exception.FaqException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static packup.faq.exception.FaqExceptionType.FAIL_TO_GET_FAQ;


@Service
@RequiredArgsConstructor
public class FaqService {

    private final FaqRepository faqRepository;
    private final CommonCodeRepository commonCodeRepository;

    public List<FaqCategoryResponse> getFaqCategory() {
        CommonCode faqParentCode = commonCodeRepository.findByCodeNameAndDeleteFlag(
                FaqEnum.FAQ.toString(),
                YnType.N
        );

        List<CommonCode> faqChildList = commonCodeRepository.findByParentCodeIdAndDeleteFlag(
                faqParentCode.getCodeId(),
                YnType.N
        );

        return FaqCategoryResponse.fromEntityList(faqChildList);
    }


    public List<FaqResponse> getFaqList() {
        Optional<List<Faq>> faqOptional = faqRepository.findAllByDeleteFlag(YnType.N);
        return FaqResponse.fromEntityList(faqOptional.orElse(Collections.emptyList()));
    }
}
