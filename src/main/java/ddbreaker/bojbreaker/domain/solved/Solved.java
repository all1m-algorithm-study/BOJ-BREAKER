package ddbreaker.bojbreaker.domain.solved;

import ddbreaker.bojbreaker.domain.problem.Problem;
import ddbreaker.bojbreaker.domain.group.Group;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"id", "solvedBy", "timestamp"})
@Entity
@Table(name = "solved")
public class Solved {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;    // 푼 시간

    @Column(name = "solved_by")
    private String solvedBy;            // 푼 유저 아이디

    @Builder
    public Solved(Problem problem, Group group, String solvedBy, LocalDateTime timestamp) {
        this.problem = problem;
        this.group = group;
        this.solvedBy = solvedBy;
        this.timestamp = timestamp;
    }
}
