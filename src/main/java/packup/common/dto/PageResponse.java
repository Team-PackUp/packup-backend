package packup.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PageResponse<T> {
    private List<T> content;
    private int page;              // 1-based 사용자 기준 페이지 번호
    private int number;            // 내부처리용 (0-based)
    private int size;              // 한 페이지 크기
    private long totalElements;    // 전체 항목 수
    private int totalPages;        // 전체 페이지 수
    private boolean last;          // 마지막 페이지 여부
    private boolean first;         // 첫 페이지 여부
    private boolean empty;         // 현재 페이지가 비어있는지 여부
}