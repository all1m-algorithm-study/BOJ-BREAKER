package ddbreaker.bojbreaker.web;

import ddbreaker.bojbreaker.service.problem.ProblemService;
import ddbreaker.bojbreaker.service.school.SchoolService;
import ddbreaker.bojbreaker.web.dto.ProblemListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final ProblemService problemService;
    private final SchoolService schoolService;
    private final HttpSession httpSession;

    @GetMapping("/")
    public String index(Model model) throws Exception {
        model.addAttribute("problems", problemService.findAll());
        return "index";
    }

    @GetMapping("/unsolved/{schoolId}")
    public String unsolved(@PathVariable Long schoolId, Model model) {
        model.addAttribute("school", schoolService.findBySchoolId(schoolId));
        List<ProblemListResponseDto> unsolved = schoolService.findUnsolvedProblems(schoolId);
        model.addAttribute("unsolvedSz", unsolved.size());
        model.addAttribute("unsolved", unsolved);
        return "unsolved";
    }
}
