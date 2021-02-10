package ddbreaker.bojbreaker.domain.solved;

import ddbreaker.bojbreaker.domain.problem.Problem;
import ddbreaker.bojbreaker.domain.school.School;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class Solved {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;

    private String solvedUser;      // 푼 유저 아이디
    private LocalDateTime solvedDate;       // 푼 시간

    @Builder
    public Solved(Problem problem, School school, String solvedUser, LocalDateTime solvedDate) {
        this.problem = problem;
        this.school = school;
        this.solvedUser = solvedUser;
        this.solvedDate = solvedDate;
    }
}
