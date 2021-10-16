package ddbreaker.bojbreaker.domain.problem;

import ddbreaker.bojbreaker.domain.solved.Solved;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "problem")
public class Problem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="code", nullable = false, unique = true)
    private Long code;          //문제 번호

    @Column(name = "title", nullable = false)
    private String title;       //문제 이름

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "tier", nullable = false)
    private SolvedAcTier tier;  // solved.ac 티어

    @Column(name = "ac_cnt")
    private Long acCnt;         // 맞은 사람 수

    @Column(name = "ac_rate")
    private double acRate;      // 평균 시도 횟수

    @OneToMany(mappedBy = "problem")
    private Set<Solved> solvedSet = new HashSet<>();

    @Builder
    public Problem(Long code, String title, SolvedAcTier tier, Long acCnt, double acRate) {
        this.code = code;
        this.title = title;
        this.tier = tier;
        this.acCnt = acCnt;
        this.acRate = acRate;
    }

    public Problem update(String title, SolvedAcTier tier, Long acCnt, double acRate) {
        this.title = title;
        this.tier = tier;
        this.acCnt = acCnt;
        this.acRate = acRate;
        return this;
    }
}
