package ddbreaker.bojbreaker;

import ddbreaker.bojbreaker.service.problem.ProblemService;
import ddbreaker.bojbreaker.service.school.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AppStartupRunner implements ApplicationRunner {

    private final ProblemService problemService;
    private final SchoolService schoolService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        problemService.updateAllProblems(3);
//        schoolService.register(302L);
    }
}
