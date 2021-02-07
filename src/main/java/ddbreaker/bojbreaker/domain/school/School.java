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

    private Long schoolId;
    private Long solvedCount;
    private Long lastCrawledSubmitId;

    @Builder
    public School(Long schoolId, Long solvedCount, Long lastCrawledSubmitId) {
        this.schoolId = schoolId;
        this.solvedCount = solvedCount;
        this.lastCrawledSubmitId = lastCrawledSubmitId;
    }
}
