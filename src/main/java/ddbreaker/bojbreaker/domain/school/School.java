package ddbreaker.bojbreaker.domain.school;

import ddbreaker.bojbreaker.domain.problem.Problem;
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
public class School {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long schoolId;      // 학교 번호

    @Column(nullable = false)
    private Long solvedCount;       // 푼 문제 수

    private Long lastCrawledSubmitId;       // 마지막 채점 번호

    @OneToMany(mappedBy = "school")
    private Set<Solved> solvedSet = new HashSet<>();

    @Builder
    public School(Long schoolId, Long solvedCount, Long lastCrawledSubmitId) {
        this.schoolId = schoolId;
        this.solvedCount = solvedCount;
        this.lastCrawledSubmitId = lastCrawledSubmitId;
    }

    public void update(Long solvedCount, Long lastCrawledSubmitId) {
        this.solvedCount = solvedCount;
        this.lastCrawledSubmitId = lastCrawledSubmitId;
    }
}
