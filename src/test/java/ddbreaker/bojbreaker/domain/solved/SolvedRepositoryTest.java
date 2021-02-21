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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class SolvedRepositoryTest {

    SolvedRepository solvedRepository;
    ProblemRepository problemRepository;
    SchoolRepository schoolRepository;

    @Autowired
    public SolvedRepositoryTest(SolvedRepository solvedRepository, ProblemRepository problemRepository, SchoolRepository schoolRepository) {
        this.solvedRepository = solvedRepository;
        this.problemRepository = problemRepository;
        this.schoolRepository = schoolRepository;
    }

    @AfterEach
    public void cleanup() {
        problemRepository.deleteAll();
        schoolRepository.deleteAll();
        solvedRepository.deleteAll();
    }

    @Test
    @Transactional
    public void 학교문제_관계저장_불러오기() throws Exception {
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
        problemRepository.save(problem);

        Long schoolId = 302L;
        Long solvedCount = 1L;
        Long lastCrawledSubmitId = 0L;
        School school = School.builder()
                .schoolId(schoolId)
                .solvedCount(solvedCount)
                .lastCrawledSubmitId(lastCrawledSubmitId)
                .build();
        schoolRepository.save(school);

        LocalDateTime time = LocalDateTime.of(2021, 02, 10, 10, 10);
        String user = "kir3i";

        // when
        Solved solved = Solved.builder()
                .problem(problem)
                .school(school)
                .solvedDate(time)
                .solvedUser(user)
                .build();
        solvedRepository.save(solved);
        school.getSolvedSet().add(solved);
        problem.getSolvedSet().add(solved);

        // then1 : School -> Solved 참조 여부
        School school1 = schoolRepository.findBySchoolId(schoolId).get();
        assertThat(school1.getSolvedSet().size()).isGreaterThan(0);
        assertThat(school1.getSolvedSet().toArray()[0]).isEqualTo(solved);

        // then2 : Problem -> Solved 참조 여부
        Problem problem1 = problemRepository.findByProblemId(problemId).get();
        assertThat(problem1.getSolvedSet().size()).isGreaterThan(0);
        assertThat(problem1.getSolvedSet().toArray()[0]).isEqualTo(solved);

        // then3 : Solved -> Problem / Solved -> School 참조 여부
        // findByProblemIdAndSchoolId에서 에러난다.
        Solved solved1 = solvedRepository.findByProblemIdAndSchoolId(problemId, schoolId);
        assertThat(solved1.getProblem().getProblemId()).isEqualTo(problemId);
        assertThat(solved1.getProblem().getTitle()).isEqualTo(title);
        assertThat(solved1.getSchool().getSchoolId()).isEqualTo(schoolId);
        System.out.println(solved1.getSchool().getSchoolId() + " " + schoolId);
        assertThat(solved1.getSolvedDate()).isEqualTo(time);
        assertThat(solved1.getSolvedUser()).isEqualTo(user);

        // then4 : dummy Solved 객체의 포함 여부
        // Solved.java 15번째 줄 참고 (@EqualsAndHashCode(exclude = {"id", "solvedUser", "solvedDate"}))
        System.out.println("build dummy");
        Thread.sleep(300);
        Solved solved_dummy = Solved.builder()
                .problem(problem)
                .school(school)
                .solvedUser("iknoom")
                .build();
        assertThat(school1.getSolvedSet().contains(solved_dummy)).isTrue();
        assertThat(problem1.getSolvedSet().contains(solved_dummy)).isTrue();
    }
}
