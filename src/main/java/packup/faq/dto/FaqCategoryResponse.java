package packup.faq.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import packup.common.domain.CommonCode;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@AllArgsConstructor
public class FaqCategoryResponse {
    private String codeId;
    private String codeName;

    public static FaqCategoryResponse fromEntity(CommonCode commonCode) {
        return FaqCategoryResponse.builder()
                .codeId(commonCode.getCodeId())
                .codeName(commonCode.getCodeName())
                .build();
    }

    public static List<FaqCategoryResponse> fromEntityList(List<CommonCode> CommonCodeList) {
        return CommonCodeList.stream()
                .map(FaqCategoryResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
