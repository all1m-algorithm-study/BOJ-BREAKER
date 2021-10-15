package ddbreaker.bojbreaker.service.school;

import ddbreaker.bojbreaker.domain.problem.Problem;
import ddbreaker.bojbreaker.domain.problem.ProblemRepository;
import ddbreaker.bojbreaker.domain.problem.SolvedAcTier;
import ddbreaker.bojbreaker.domain.school.School;
import ddbreaker.bojbreaker.domain.school.SchoolRepository;
import ddbreaker.bojbreaker.domain.solved.Solved;
import ddbreaker.bojbreaker.domain.solved.SolvedRepository;
import ddbreaker.bojbreaker.web.dto.ProblemListRequestDto;
import ddbreaker.bojbreaker.web.dto.ProblemListResponseDto;
import ddbreaker.bojbreaker.web.dto.ProblemResponseDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public void 안푼_문제_조회_쿼리_테스트() {
        // given
        // problem
        for (Long i = 0L; i < 2500L; i++) {
            problemRepository.save(Problem.builder()
                    .problemId(i)
                    .title("dummy:" + i)
                    .tier(SolvedAcTier.BRONZE5)
                    .build());
        }
        // school
        Long schoolId = 302L;
        School school = School.builder()
                .schoolId(schoolId)
                .schoolName("서울시립대")
                .lastCrawledSubmitId(0L)
                .solvedCount(0L)
                .build();
        schoolRepository.save(school);
        // solved
        for (Long i = 0L; i < 500L; i++) {
            Problem problem = problemRepository.findByProblemId(i).get();
            Solved solved = Solved.builder()
                    .problem(problem)
                    .school(school)
                    .build();
            school.getSolvedSet().add(solved);
            problem.getSolvedSet().add(solved);
            solvedRepository.save(solved);
        }
        // filter
        List<String> tierFilter = List.of("BRONZE5");
        ProblemListRequestDto requestDto = ProblemListRequestDto.builder()
                .tierFilter(tierFilter)
                .direction("desc")
                .page(1)
                .sortedBy("")
                .build();

        //when
        ProblemListResponseDto unsolvedProblems = schoolService.findUnsolvedProblems(schoolId, requestDto);

        //then
        assertThat(unsolvedProblems.getAppearedProblems().size()).isGreaterThan(0);
        assertThat(unsolvedProblems.getTotalProblems()).isEqualTo(2000);
        System.out.println(unsolvedProblems.getAppearedProblems().size());
        System.out.println(unsolvedProblems.getTotalPages());
        for(ProblemResponseDto dto: unsolvedProblems.getAppearedProblems())
            System.out.println(dto.getProblemId() + " " +
                                    dto.getTitle() + " " +
                                    dto.getTier() + " " +
                                    dto.getAcTries() + " " +
                                    dto.getAvgTries());
    }

    @Test
    public void 학교_새로_맞은문제_갱신() throws Exception {

    }
}