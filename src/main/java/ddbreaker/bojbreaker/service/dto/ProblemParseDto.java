package ddbreaker.bojbreaker.service.dto;

import ddbreaker.bojbreaker.domain.problem.Problem;
import ddbreaker.bojbreaker.domain.problem.SolvedAcTier;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProblemParseDto {

    private Long id;            // 문제 번호
    private String title;       // 문제 이름
    private Long acTries;       // 맞은 사람 수
    private double avgTries;    // 평균 시도 횟수
    private SolvedAcTier tier;  // solved.ac 티어

    @Builder
    public ProblemParseDto(Long id, String title, Long acTries, double avgTries, SolvedAcTier tier) {
        this.id = id;
        this.title = title;
        this.acTries = acTries;
        this.avgTries = avgTries;
        this.tier = tier;
    }

    public Problem toEntity() {
        return Problem.builder()
                .id(id)
                .title(title)
                .acTries(acTries)
                .avgTries(avgTries)
                .tier(tier)
                .build();
    }
}
