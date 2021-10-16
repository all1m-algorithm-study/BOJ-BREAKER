package ddbreaker.bojbreaker.domain.group;

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
@Table(name = "group")
public class Group implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="code", nullable = false, unique = true)
    private Long code;              // 그룹 번호

    @Column(name = "name", nullable = false)
    private String name;            // 그룹 이름

    @Column(name = "rank", nullable = false)
    private Long rank;            // 그룹 랭킹

    @Column(name = "score", nullable = false)
    private Long score;             // 푼 문제 수

    @Column(name = "last_submit_code", nullable = false)
    private Long lastSubmitCode;    // 마지막 채점 번호

    @OneToMany(mappedBy = "group")
    private Set<Solved> solvedSet = new HashSet<>();

    @Builder
    public Group(Long code, String name, Long rank, Long score, Long lastSubmitCode) {
        this.code = code;
        this.name = name;
        this.rank = rank;
        this.score = score;
        this.lastSubmitCode = lastSubmitCode;
    }

    public void update(Long score, Long lastSubmitCode) {
        this.score = score;
        this.lastSubmitCode = lastSubmitCode;
    }
}
