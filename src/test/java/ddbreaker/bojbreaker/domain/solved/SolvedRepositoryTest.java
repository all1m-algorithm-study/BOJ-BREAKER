package ddbreaker.bojbreaker.domain.solved;

import ddbreaker.bojbreaker.domain.problem.Problem;
import ddbreaker.bojbreaker.domain.problem.ProblemRepository;
import ddbreaker.bojbreaker.domain.problem.SolvedAcTier;
import ddbreaker.bojbreaker.domain.group.Group;
import ddbreaker.bojbreaker.domain.group.GroupRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class SolvedRepositoryTest {

    SolvedRepository solvedRepository;
    ProblemRepository problemRepository;
    GroupRepository groupRepository;

    @Autowired
    public SolvedRepositoryTest(SolvedRepository solvedRepository, ProblemRepository problemRepository, GroupRepository groupRepository) {
        this.solvedRepository = solvedRepository;
        this.problemRepository = problemRepository;
        this.groupRepository = groupRepository;
    }

    @AfterEach
    public void cleanup() {
        problemRepository.deleteAll();
        groupRepository.deleteAll();
        solvedRepository.deleteAll();
    }

    @Test
    @Transactional
    public void 그룹문제_관계저장_불러오기() throws Exception {
        //given
        Long problemCode = 1000L;
        String title = "A+B";
        SolvedAcTier tier = SolvedAcTier.BRONZE5;
        Long acCnt = 112017L;
        double acRate = 2.2773;
        Problem problem = Problem.builder()
                .code(problemCode)
                .title(title)
                .tier(tier)
                .acCnt(acCnt)
                .acRate(acRate)
                .build();
        problemRepository.save(problem);

        Long groupCode = 302L;
        String groupName = "서울시립대학교";
        Long rank = 40L;
        Long scroe = 1L;
        Long lastSubmitCode = 0L;
        Group group = Group.builder()
                .code(groupCode)
                .name(groupName)
                .rank(rank)
                .score(scroe)
                .lastSubmitCode(lastSubmitCode)
                .build();
        groupRepository.save(group);

        LocalDateTime time = LocalDateTime.of(2021, 2, 10, 10, 10);
        String user = "kir3i";

        // when
        Solved solved = Solved.builder()
                .problem(problem)
                .group(group)
                .timestamp(time)
                .solvedBy(user)
                .build();
        solvedRepository.save(solved);
        group.getSolvedSet().add(solved);
        problem.getSolvedSet().add(solved);

        // then1 : School -> Solved 참조 여부
        Group group1 = groupRepository.findByCode(groupCode).get();
        assertThat(group1.getSolvedSet().size()).isGreaterThan(0);
        assertThat(group1.getSolvedSet().toArray()[0]).isEqualTo(solved);
        // School -> Solved -> Problem 참조 여부
        for (Solved solved1 : group1.getSolvedSet()) {
            assertThat(solved1.getProblem()).isEqualTo(problem);
        }

        // then2 : Problem -> Solved 참조 여부
        Problem problem1 = problemRepository.findByCode(problemCode).get();
        assertThat(problem1.getSolvedSet().size()).isGreaterThan(0);
        assertThat(problem1.getSolvedSet().toArray()[0]).isEqualTo(solved);
        // Problem -> Solved -> School 참조 여부
        for (Solved solved1 : problem1.getSolvedSet()) {
            assertThat(solved1.getGroup()).isEqualTo(group);
        }

        // then4 : dummy Solved 객체의 포함 여부
        Solved solved_dummy = Solved.builder()
                .problem(problem)
                .group(group)
                .solvedBy("iknoom")
                .build();
        assertThat(group1.getSolvedSet().contains(solved_dummy)).isTrue();
        assertThat(problem1.getSolvedSet().contains(solved_dummy)).isTrue();
    }
}
