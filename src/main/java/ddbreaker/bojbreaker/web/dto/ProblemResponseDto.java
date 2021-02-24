package ddbreaker.bojbreaker.web.dto;

import ddbreaker.bojbreaker.domain.problem.SolvedAcTier;
import lombok.Getter;

@Getter
public class ProblemResponseDto {

    private Long problemId;
    private String title;
    private SolvedAcTier tier;
    private Long acTries;
    private double avgTries;

    public ProblemResponseDto(Long problemId, String title, SolvedAcTier tier, Long acTries, double avgTries) {
        this.problemId = problemId;
        this.title = title;
        this.tier = tier;
        this.acTries = acTries;
        this.avgTries = avgTries;
    }
}
