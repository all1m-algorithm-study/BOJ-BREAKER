package ddbreaker.bojbreaker.web;

import ddbreaker.bojbreaker.service.problem.ProblemService;
import ddbreaker.bojbreaker.service.school.SchoolService;
import ddbreaker.bojbreaker.web.dto.ProblemListRequestDto;
import ddbreaker.bojbreaker.web.dto.ProblemResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
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
        List<ProblemResponseDto> unsolved = schoolService.findUnsolvedProblems(schoolId);
        model.addAttribute("unsolvedSz", unsolved.size());
        model.addAttribute("unsolved", unsolved);
        return "unsolved-test";
    }

    @GetMapping("/api/v1/school/unsolved/{schoolId}")
    public String findUnsolvedProblems(@PathVariable Long schoolId, @RequestParam String sortedBy,
                                         @RequestParam String direction, @RequestParam int page, Model model) {
        ProblemListRequestDto requestDto = ProblemListRequestDto.builder()
                                                            .sortedBy(sortedBy)
                                                            .direction(direction)
                                                            .page(page)
                                                            .build();
        model.addAttribute("school", schoolService.findBySchoolId(schoolId));
        model.addAttribute("unsolvedProblems", schoolService.findUnsolvedProblems(schoolId, requestDto));
        return "unsolved";
    }
}
