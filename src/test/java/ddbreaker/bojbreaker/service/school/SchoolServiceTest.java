package ddbreaker.bojbreaker.service.school;

import ddbreaker.bojbreaker.domain.problem.Problem;
import ddbreaker.bojbreaker.domain.problem.ProblemRepository;
import ddbreaker.bojbreaker.domain.problem.SolvedAcTier;
import ddbreaker.bojbreaker.domain.school.School;
import ddbreaker.bojbreaker.domain.school.SchoolRepository;
import ddbreaker.bojbreaker.domain.solved.Solved;
import ddbreaker.bojbreaker.domain.solved.SolvedRepository;
import ddbreaker.bojbreaker.service.Crawler;
import ddbreaker.bojbreaker.service.problem.ProblemService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class SchoolServiceTest {

    SolvedRepository solvedRepository;
    ProblemRepository problemRepository;
    SchoolRepository schoolRepository;
    SchoolService schoolService;

    @Autowired
    public SchoolServiceTest(SolvedRepository solvedRepository, ProblemRepository problemRepository, SchoolRepository schoolRepository, SchoolService schoolService) {
        this.solvedRepository = solvedRepository;
        this.problemRepository = problemRepository;
        this.schoolRepository = schoolRepository;
        this.schoolService = schoolService;
    }

    @AfterEach
    public void cleanup() {
        // 아래 순서 안지키면 에러남 (꼭 solved 먼저 제거)
        solvedRepository.deleteAll();
        schoolRepository.deleteAll();
        problemRepository.deleteAll();
    }

    @Test
    @Transactional
    public void 학교_등록() throws Exception{
        // given
        Long schoolId = 302L;
        for (Long i = 0L; i < 25000L; i++) {
            problemRepository.save(Problem.builder()
                    .problemId(i)
                    .title("dummy:" + i)
                    .tier(SolvedAcTier.BRONZE5)
                    .build());
        }

        // when
        schoolService.register(schoolId);

        // then
        School school = schoolRepository.findBySchoolId(schoolId)
                .orElseThrow(() -> new IllegalArgumentException("[Id:" + schoolId + "] Id에 해당하는 학교가 없습니다."));
        assertThat(school.getSchoolId()).isEqualTo(schoolId);
        assertThat(school.getLastCrawledSubmitId()).isGreaterThan(0L);
        assertThat(school.getSolvedCount()).isGreaterThan(0L);
        assertThat(school.getSolvedSet().size()).isGreaterThan(0);

        System.out.println(school.getSolvedCount());
        System.out.println(school.getSolvedSet().size());
        for (Solved solved : school.getSolvedSet()) {
            System.out.println(solved.getProblem().getProblemId());
            System.out.println(solved.getProblem().getTitle());
            System.out.println(solved.getProblem().getTier());
            System.out.println();
        }
    }

    public void 학교_새로_맞은문제_갱신() {

    }
}