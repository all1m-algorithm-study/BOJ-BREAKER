package ddbreaker.bojbreaker.domain.SolvedLogs;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class SolvedLogs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long problemId;
    private String solver;
    private LocalDateTime solvedDate;

    @Builder
    public SolvedLogs(Long problemId, String solver, LocalDateTime solvedDate) {
        this.problemId = problemId;
        this.solver = solver;
        this.solvedDate = solvedDate;
    }
}
