package ddbreaker.bojbreaker.domain.problem;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
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
        Long code = 1000L;
        String title = "A+B";
        SolvedAcTier tier = SolvedAcTier.BRONZE5;
        Long acCnt = 112017L;
        double acRate = 2.2773;

        problemRepository.save(Problem.builder()
                        .code(code)
                        .title(title)
                        .tier(tier)
                        .acCnt(acCnt)
                        .acRate(acRate)
                        .build());

        //when
        Optional<Problem> p = problemRepository.findByCode(code);

        //then
        Problem problem = p.get();
        assertThat(problem.getCode()).isEqualTo(code);
        assertThat(problem.getTitle()).isEqualTo(title);
        assertThat(problem.getTier()).isEqualTo(tier);
        assertThat(problem.getAcCnt()).isEqualTo(acCnt);
        assertThat(problem.getAcRate()).isEqualTo(acRate);
        System.out.println(problem.getId());
    }
}
