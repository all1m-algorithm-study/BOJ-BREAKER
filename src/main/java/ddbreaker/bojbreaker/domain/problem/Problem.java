package ddbreaker.bojbreaker.domain.problem;

import ddbreaker.bojbreaker.domain.solved.Solved;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@Entity
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long problemId;     //문제 번호

    @Column(nullable = false)
    private String title;       //문제 이름

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SolvedAcTier tier;

    private Long acTries;       // 맞은 사람 수
    private double avgTries;    // 평균 시도 횟수

    @OneToMany(mappedBy = "problem")
    private Set<Solved> solvedSet = new HashSet<>();

    @Builder
    public Problem(Long problemId, String title, SolvedAcTier tier, Long acTries, double avgTries) {
        this.problemId = problemId;
        this.title = title;
        this.tier = tier;
        this.acTries = acTries;
        this.avgTries = avgTries;
    }

    public Problem update(String title, SolvedAcTier tier, Long acTries, double avgTries) {
        this.title = title;
        this.tier = tier;
        this.acTries = acTries;
        this.avgTries = avgTries;

        return this;
    }
}
