package packup.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class PageDTO<T> {
    private List<T> objectList;
    private int totalPage;
    private Long totalElements;
    private int curPage;

}
