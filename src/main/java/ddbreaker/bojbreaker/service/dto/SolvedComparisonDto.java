package ddbreaker.bojbreaker.service.dto;

import ddbreaker.bojbreaker.domain.problem.Problem;
import ddbreaker.bojbreaker.domain.school.School;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SolvedComparisonDto {
    private Problem problem;
    private School school;

    @Builder
    public SolvedComparisonDto(Problem problem, School school) {
        this.problem = problem;
        this.school = school;
    }
}
