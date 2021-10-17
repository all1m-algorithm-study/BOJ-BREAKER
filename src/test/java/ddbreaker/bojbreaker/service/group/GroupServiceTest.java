package ddbreaker.bojbreaker.service.group;

import ddbreaker.bojbreaker.domain.problem.Problem;
import ddbreaker.bojbreaker.domain.problem.ProblemRepository;
import ddbreaker.bojbreaker.domain.problem.SolvedAcTier;
import ddbreaker.bojbreaker.domain.group.Group;
import ddbreaker.bojbreaker.domain.group.GroupRepository;
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
class GroupServiceTest {

    SolvedRepository solvedRepository;
    ProblemRepository problemRepository;
    GroupRepository groupRepository;
    GroupService groupService;

    @Autowired
    public GroupServiceTest(SolvedRepository solvedRepository, ProblemRepository problemRepository, GroupRepository groupRepository, GroupService groupService) {
        this.solvedRepository = solvedRepository;
        this.problemRepository = problemRepository;
        this.groupRepository = groupRepository;
        this.groupService = groupService;
    }

    @AfterEach
    public void cleanup() {
        // 아래 순서 안지키면 에러남 (꼭 solved 먼저 제거)
        solvedRepository.deleteAll();
        groupRepository.deleteAll();
        problemRepository.deleteAll();
    }

    @Test
    @Transactional
    public void 안푼_문제_조회_쿼리_테스트() {
        // given
        // problem
        for (Long i = 0L; i < 2500L; i++) {
            problemRepository.save(Problem.builder()
                    .code(i)
                    .title("dummy:" + i)
                    .tier(SolvedAcTier.BRONZE5)
                    .build());
        }
        // group
        Long groupCode = 302L;
        Group group = Group.builder()
                .code(groupCode)
                .name("서울시립대")
                .rank(40L)
                .lastSubmitCode(0L)
                .score(0L)
                .build();
        groupRepository.save(group);
        // solved
        for (Long i = 0L; i < 500L; i++) {
            Problem problem = problemRepository.findByCode(i).get();
            Solved solved = Solved.builder()
                    .problem(problem)
                    .group(group)
                    .build();
            group.getSolvedSet().add(solved);
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
        ProblemListResponseDto unsolvedProblems = groupService.findUnsolvedProblems(groupCode, requestDto);

        //then
        assertThat(unsolvedProblems.getAppearedProblems().size()).isGreaterThan(0);
        assertThat(unsolvedProblems.getTotalProblems()).isEqualTo(2000);
        System.out.println(unsolvedProblems.getAppearedProblems().size());
        System.out.println(unsolvedProblems.getTotalPages());
        for(ProblemResponseDto dto: unsolvedProblems.getAppearedProblems())
            System.out.println(dto.getCode() + " " +
                                    dto.getTitle() + " " +
                                    dto.getTier() + " " +
                                    dto.getAcCnt() + " " +
                                    dto.getAcRate());
    }

    @Test
    public void 그룹_새로_맞은문제_갱신() throws Exception {

    }
}