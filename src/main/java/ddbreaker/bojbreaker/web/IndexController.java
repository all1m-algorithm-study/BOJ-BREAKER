package ddbreaker.bojbreaker.web;

import ddbreaker.bojbreaker.domain.problem.SolvedAcTier;
import ddbreaker.bojbreaker.service.problem.ProblemService;
import ddbreaker.bojbreaker.service.group.GroupService;
import ddbreaker.bojbreaker.web.dto.ProblemListRequestDto;
import ddbreaker.bojbreaker.web.dto.ProblemResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final ProblemService problemService;
    private final GroupService groupService;
    private final HttpSession httpSession;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("problems", problemService.findAll());
        return "index";
    }

    @GetMapping("/unsolved/{groupCode}")
    public String unsolved(@PathVariable Long groupCode, Model model) {
        model.addAttribute("group", groupService.findByGroupCode(groupCode));
        List<ProblemResponseDto> unsolved = groupService.findUnsolvedProblems(groupCode);
        model.addAttribute("unsolvedSz", unsolved.size());
        model.addAttribute("unsolved", unsolved);
        return "unsolved-test";
    }

    @GetMapping("/group/{groupCode}/unsolved")
    public String findUnsolvedProblems(@PathVariable Long groupCode,
                                       @RequestParam(required = false, defaultValue = "tier") String sortedBy,
                                       @RequestParam(required = false, defaultValue = "asc") String direction,
                                       @RequestParam(required = false, defaultValue = "1") int page,
                                       @RequestParam(required = false) List<String> tierFilter,
                                       Model model) {
        if(tierFilter == null)
            tierFilter = new ArrayList<>();
        ProblemListRequestDto requestDto = ProblemListRequestDto.builder()
                                                            .tierFilter(tierFilter)
                                                            .sortedBy(sortedBy)
                                                            .direction(direction)
                                                            .page(page)
                                                            .build();
        model.addAttribute("group", groupService.findByGroupCode(groupCode));
        model.addAttribute("tiers", Arrays.asList(SolvedAcTier.values()));
        model.addAttribute("unsolved", groupService.findUnsolvedProblems(groupCode, requestDto));
        return "unsolved";
    }
}
