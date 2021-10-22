package ddbreaker.bojbreaker.service.group;

import ddbreaker.bojbreaker.domain.problem.ProblemRepository;
import ddbreaker.bojbreaker.domain.group.Group;
import ddbreaker.bojbreaker.domain.group.GroupRepository;
import ddbreaker.bojbreaker.domain.solved.Solved;
import ddbreaker.bojbreaker.web.dto.ProblemListRequestDto;
import ddbreaker.bojbreaker.web.dto.ProblemListResponseDto;
import ddbreaker.bojbreaker.web.dto.ProblemResponseDto;
import ddbreaker.bojbreaker.web.dto.GroupResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final ProblemRepository problemRepository;

    @Transactional
    public List<ProblemResponseDto> findUnsolvedProblems(Long groupCode) {
        Group group = groupRepository.findByCode(groupCode)
                .orElseThrow(() -> new IllegalArgumentException("알 수 없는 학교 번호입니다. school_id="+groupCode));
        Set<Solved> solvedSet = group.getSolvedSet();
        return problemRepository.findAll().stream()
                .filter(problem -> !solvedSet.contains(Solved.builder()
                                                            .problem(problem)
                                                            .group(group)
                                                            .build()))
                .map(entity -> new ProblemResponseDto(
                        entity.getCode(),
                        entity.getTitle(),
                        entity.getTier(),
                        entity.getAcCnt(),
                        entity.getAcRate())
                ).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProblemListResponseDto findUnsolvedProblems(Long groupCode, ProblemListRequestDto requestDto) {
        Group group = groupRepository.findByCode(groupCode)
                .orElseThrow(() -> new IllegalArgumentException("알 수 없는 그룹 번호입니다. group.code="+groupCode));
        Set<Solved> solvedSet = group.getSolvedSet();

        List<ProblemResponseDto> problems = problemRepository.findAll().stream()
                .filter(problem -> !solvedSet.contains(Solved.builder()
                        .problem(problem)
                        .group(group)
                        .build()))
                .filter(problem -> requestDto.getTierFilter().isEmpty()
                        || requestDto.getTierFilter().contains(problem.getTier()))
                .map(entity -> new ProblemResponseDto(
                        entity.getCode(),
                        entity.getTitle(),
                        entity.getTier(),
                        entity.getAcCnt(),
                        entity.getAcRate())
                ).sorted((a, b) -> {
                    switch (requestDto.getSortedBy()) {
                        case "problemId":
                            return Long.compare(a.getCode(), b.getCode());
                        case "acTries":
                            return Long.compare(a.getAcCnt(), b.getAcCnt());
                        case "avgTries":
                            return Double.compare(a.getAcRate(), b.getAcRate());
                        default:
                            return Integer.compare(a.getTier().ordinal(), b.getTier().ordinal());
                    }
                }).collect(Collectors.toList());

        if(requestDto.getDirection().equals("desc"))
            Collections.reverse(problems);

        int lastPage;
        if(problems.size()%100 != 0)
            lastPage = problems.size() / 100 + 1;
        else
            lastPage = problems.size() / 100;

        List<ProblemResponseDto> appearedProblems;
        if(requestDto.getPage() < 1)
            appearedProblems = problems.subList(0, Integer.min(99, problems.size()));
        else if(requestDto.getPage() < lastPage)
            appearedProblems = problems.subList(100*(requestDto.getPage()-1), 100*requestDto.getPage());
        else if(requestDto.getPage() == lastPage)
            appearedProblems = problems.subList(100*(lastPage-1), problems.size());
        else
            appearedProblems = new ArrayList<ProblemResponseDto>();

        return ProblemListResponseDto.builder()
                .appearedProblems(appearedProblems)
                .totalProblems(problems.size())
                .totalPages(lastPage)
                .build();
    }

    @Transactional(readOnly = true)
    public GroupResponseDto findByGroupCode(Long groupCode) {
        Group entity = groupRepository.findByCode(groupCode)
                .orElseThrow(() -> new IllegalArgumentException("알 수 없는 그룹 번호입니다. group.code="+groupCode));
        return new GroupResponseDto(entity);
    }
}
