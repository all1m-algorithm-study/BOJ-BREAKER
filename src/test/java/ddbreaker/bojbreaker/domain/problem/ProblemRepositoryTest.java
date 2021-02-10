package ddbreaker.bojbreaker.domain.problem;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ProblemRepositoryTest {

    ProblemRepository problemRepository;

    @Autowired
    public ProblemRepositoryTest(ProblemRepository problemRepository) {
        this.problemRepository = problemRepository;
    }

    @AfterEach
    public void cleanup() {
        problemRepository.deleteAll();
    }

    @Test
    public void 문제저장_불러오기() {
        //given
        Long problemId = 1000L;
        String title = "A+B";
        SolvedAcTier tier = SolvedAcTier.BRONZE5;
        Long acTries = 112017L;
        double avgTries = 2.2773;

        problemRepository.save(Problem.builder()
                        .problemId(problemId)
                        .title(title)
                        .tier(tier)
                        .acTries(acTries)
                        .avgTries(avgTries)
                        .build());

        //when
        Optional<Problem> p = problemRepository.findByProblemId(problemId);

        //then
        Problem problem = p.get();
        assertThat(problem.getProblemId()).isEqualTo(problemId);
        assertThat(problem.getTitle()).isEqualTo(title);
        assertThat(problem.getTier()).isEqualTo(tier);
        assertThat(problem.getAcTries()).isEqualTo(acTries);
        assertThat(problem.getAvgTries()).isEqualTo(avgTries);
        System.out.println(problem.getId());
    }
}
