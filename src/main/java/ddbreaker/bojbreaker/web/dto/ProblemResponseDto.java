package ddbreaker.bojbreaker.web.dto;

import ddbreaker.bojbreaker.domain.problem.SolvedAcTier;
import lombok.Getter;

@Getter
public class ProblemResponseDto {

    private Long code;
    private String title;
    private SolvedAcTier tier;
    private Long acCnt;
    private double acRate;

    public ProblemResponseDto(Long code, String title, SolvedAcTier tier, Long acCnt, double acRate) {
        this.code = code;
        this.title = title;
        this.tier = tier;
        this.acCnt = acCnt;
        this.acRate = acRate;
    }
}
