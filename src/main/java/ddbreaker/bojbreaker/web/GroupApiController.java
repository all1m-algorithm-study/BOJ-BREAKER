package ddbreaker.bojbreaker.web;

import ddbreaker.bojbreaker.service.group.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class GroupApiController {

    private final GroupService groupService;

    // GetMapping은 오류남
//    @PostMapping("/api/v1/school/unsolved/{schoolId}")
//    public ProblemListResponseDto findUnsolvedProblems(@PathVariable Long schoolId,
//                                                       @RequestBody ProblemListRequestDto requestDto) {
//        return schoolService.findUnsolvedProblems(schoolId, requestDto);
//    }
}
