package ddbreaker.bojbreaker.web.dto;

import ddbreaker.bojbreaker.domain.problem.SolvedAcTier;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class ProblemListRequestDto {

    private Long schoolId;
    private List<SolvedAcTier> tierFilter;
    private String sortedBy;
    private String direction;
    private int page;

    @Builder
    public ProblemListRequestDto(Long schoolId, List<String> tierFilter, String sortedBy, String direction, int page) {
        this.schoolId = schoolId;
        this.tierFilter = tierFilter.stream()
                .map(tier -> SolvedAcTier.valueOf(tier))
                .collect(Collectors.toList());
        this.sortedBy = sortedBy;
        this.direction = direction;
        this.page = page;
    }
}
