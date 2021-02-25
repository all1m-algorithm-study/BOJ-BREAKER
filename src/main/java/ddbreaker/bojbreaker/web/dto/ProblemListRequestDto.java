package ddbreaker.bojbreaker.web.dto;

import ddbreaker.bojbreaker.domain.problem.SolvedAcTier;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class ProblemListRequestDto {

    private List<SolvedAcTier> tierFilter;
    private String sortedBy;
    private String direction;
    private int page;

    @Builder
    public ProblemListRequestDto(List<String> tierFilter, String sortedBy, String direction, int page) {
        this.tierFilter = tierFilter.stream()
                .map(tier -> {
                    try {
                        return SolvedAcTier.valueOf(tier);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }).filter(Objects::nonNull)
                .collect(Collectors.toList());
        this.sortedBy = sortedBy;
        this.direction = direction;
        this.page = page;
    }
}
