package ddbreaker.bojbreaker.domain.problem;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Problem {

    @Id
    private Long id;            //문제 번호

    @Column(nullable = false)
    private String title;       //문제 이름

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SolvedAcTier tier;

    private Long acTries;       // 맞은 사람 수
    private double avgTries;    // 평균 시도 횟수

    @Builder
    public Problem(Long id, String title, SolvedAcTier tier, Long acTries, double avgTries) {
        this.id = id;
        this.title = title;
        this.tier = tier;
        this.acTries = acTries;
        this.avgTries = avgTries;
    }

    public void update(String title, Long acTries, double avgTries) {
        this.title = title;
        this.acTries = acTries;
        this.avgTries = avgTries;
    }
}
