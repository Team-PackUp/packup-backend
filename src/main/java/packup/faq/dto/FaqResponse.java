package packup.faq.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import packup.faq.domain.Faq;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FaqResponse {
    private String question;
    private String answer;
    private String faqType;

    public static FaqResponse fromEntity(Faq faq) {
        return FaqResponse.builder()
                .faqType(faq.getFaqType())
                .question(faq.getQuestion())
                .answer(faq.getAnswer())
                .build();
    }

    public static List<FaqResponse> fromEntityList(List<Faq> faqList) {
        return faqList.stream()
                .map(FaqResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
