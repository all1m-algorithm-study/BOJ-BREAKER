package ddbreaker.bojbreaker.web;

import ddbreaker.bojbreaker.domain.problem.SolvedAcTier;
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
import java.util.Arrays;
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

    @GetMapping("/school/{schoolId}/unsolved")
    public String findUnsolvedProblems(@PathVariable Long schoolId,
                                       @RequestParam(required = false, defaultValue = "tier") String sortedBy,
                                       @RequestParam(required = false, defaultValue = "asc") String direction,
                                       @RequestParam(required = false, defaultValue = "1") int page,
                                       @RequestParam(required = false) List<String> tierFilter,
                                       Model model) {
        if(tierFilter == null)
            tierFilter = new ArrayList<>();
        ProblemListRequestDto requestDto = ProblemListRequestDto.builder()
                                                            .tierFilter(tierFilter)  // 추후 수정 요함
                                                            .sortedBy(sortedBy)
                                                            .direction(direction)
                                                            .page(page)
                                                            .build();
        model.addAttribute("school", schoolService.findBySchoolId(schoolId));
        model.addAttribute("tiers", Arrays.asList(SolvedAcTier.values()));
        model.addAttribute("unsolved", schoolService.findUnsolvedProblems(schoolId, requestDto));
        return "unsolved";
    }
}
