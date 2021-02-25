package ddbreaker.bojbreaker.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@NoArgsConstructor
public class ProblemListResponseDto {

    private List<ProblemResponseDto> appearedProblems;
    private int totalProblems;
    private int totalPages;

    @Builder
    public ProblemListResponseDto(List<ProblemResponseDto> appearedProblems, int totalProblems, int totalPages) {
        this.appearedProblems = appearedProblems;
        this.totalProblems = totalProblems;
        this.totalPages = totalPages;
    }
}
