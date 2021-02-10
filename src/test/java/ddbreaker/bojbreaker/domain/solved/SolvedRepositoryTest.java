package ddbreaker.bojbreaker.domain.solved;

import ddbreaker.bojbreaker.domain.problem.Problem;
import ddbreaker.bojbreaker.domain.problem.ProblemRepository;
import ddbreaker.bojbreaker.domain.problem.SolvedAcTier;
import ddbreaker.bojbreaker.domain.school.School;
import ddbreaker.bojbreaker.domain.school.SchoolRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class SolvedRepositoryTest {

    SolvedRepository solvedRepository;
    ProblemRepository problemRepository;
    SchoolRepository schoolRepository;

    @Autowired
    public SolvedRepositoryTest(SolvedRepository solvedRepository) {
        this.solvedRepository = solvedRepository;
    }

    @AfterEach
    public void cleanup() {
        solvedRepository.deleteAll();
    }

    @Test
    public void 학교문제_관계저장_불러오기() {
        //given
        Long problemId = 1000L;
        String title = "A+B";
        SolvedAcTier tier = SolvedAcTier.BRONZE5;
        Long acTries = 112017L;
        double avgTries = 2.2773;
        Problem problem = Problem.builder()
                .problemId(problemId)
                .title(title)
                .tier(tier)
                .acTries(acTries)
                .avgTries(avgTries)
                .build();

        Long schoolId = 302L;
        Long solvedCount = 1L;
        Long lastCrawledSubmitId = 0L;
        School school = School.builder()
                .schoolId(schoolId)
                .solvedCount(solvedCount)
                .lastCrawledSubmitId(lastCrawledSubmitId)
                .build();

        solvedRepository.save(Solved.builder()
        .problem(problem)
        .school(school)
        .solvedDate(LocalDateTime.of(2021, 02, 10, 10, 10))
        .solvedUser("kir3i")
        .build());

        //when
        Solved solved = solvedRepository.findByProblemIdAndSchoolId(problemId, schoolId);

        //then
        assertThat(solved.getProblem().getProblemId()).isEqualTo(problemId);
        assertThat(solved.getProblem().getTitle()).isEqualTo(title);
        assertThat(solved.getSchool().getSchoolId()).isEqualTo(schoolId);
    }
}
