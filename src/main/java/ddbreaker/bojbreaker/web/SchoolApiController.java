package ddbreaker.bojbreaker.web;

import ddbreaker.bojbreaker.service.school.SchoolService;
import ddbreaker.bojbreaker.web.dto.ProblemListRequestDto;
import ddbreaker.bojbreaker.web.dto.ProblemListResponseDto;
import ddbreaker.bojbreaker.web.dto.ProblemResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class SchoolApiController {

    private final SchoolService schoolService;

    // GetMapping은 오류남
//    @PostMapping("/api/v1/school/unsolved/{schoolId}")
//    public ProblemListResponseDto findUnsolvedProblems(@PathVariable Long schoolId,
//                                                       @RequestBody ProblemListRequestDto requestDto) {
//        return schoolService.findUnsolvedProblems(schoolId, requestDto);
//    }
}
