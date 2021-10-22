package ddbreaker.bojbreaker.service.problem;

import ddbreaker.bojbreaker.domain.problem.Problem;
import ddbreaker.bojbreaker.domain.problem.ProblemRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ProblemServiceTest {

    ProblemService problemService;
    ProblemRepository problemRepository;

    @Autowired
    public ProblemServiceTest(ProblemService problemService, ProblemRepository problemRepository) {
        this.problemService = problemService;
        this.problemRepository = problemRepository;
    }

    @AfterEach
    public void cleanup() {
        problemRepository.deleteAll();
    }

}
