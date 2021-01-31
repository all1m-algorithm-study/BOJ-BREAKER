package ddbreaker.bojbreaker.domain.school;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity
public class School {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long solvedCount;
    private Long lastCrawledSubmitId;
    private Long lastCrawledproblemId;

    @Builder
    public School(Long solvedCount, Long lastCrawledSubmitId, Long lastCrawledproblemId) {
        this.solvedCount = solvedCount;
        this.lastCrawledSubmitId = lastCrawledSubmitId;
        this.lastCrawledproblemId = lastCrawledproblemId;
    }
}
