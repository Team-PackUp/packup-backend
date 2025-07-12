package packup.common.dto;

import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@Builder
public class PageDTO<T> {

    private List<T> objectList;
    private int    totalPage;      // page.getTotalPages()
    private long   totalElements;  // page.getTotalElements()
    private int    curPage;        // page.getNumber()  (0‑based)

    public static <T> PageDTO<T> of(Page<T> page) {
        return PageDTO.<T>builder()
                .objectList(page.getContent())
                .totalPage(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .curPage(page.getNumber())
                .build();
    }
}
