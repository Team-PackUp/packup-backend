package packup.faq.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import packup.common.dto.ResultModel;
import packup.faq.dto.FaqCategoryResponse;
import packup.faq.dto.FaqRequest;
import packup.faq.dto.FaqResponse;
import packup.faq.exception.FaqException;
import packup.faq.service.FaqService;

import java.util.List;

import static packup.faq.exception.FaqExceptionType.FAIL_TO_GET_FAQ;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/faq")
public class FaqController {

    private final FaqService faqService;

    @GetMapping("/category")
    public ResultModel<List<FaqCategoryResponse>> getFaqCategory() {

        return ResultModel.success(faqService.getFaqCategory());
    }

    @GetMapping("/list")
    public ResultModel<List<FaqResponse>> getFaqList() {

        return ResultModel.success(faqService.getFaqList());
    }

    @GetMapping("/list/category")
    public ResultModel<List<FaqResponse>> getFaqListByCategory(FaqRequest faqRequest) {
        String faqType = faqRequest.getFaqType();
        if(faqType == null) {
            throw new FaqException(FAIL_TO_GET_FAQ);
        }

        return ResultModel.success(faqService.getFaqListByCategory(faqType));
    }
}
