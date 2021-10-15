package ddbreaker.bojbreaker.service.school;

import ddbreaker.bojbreaker.domain.problem.ProblemRepository;
import ddbreaker.bojbreaker.domain.school.School;
import ddbreaker.bojbreaker.domain.school.SchoolRepository;
import ddbreaker.bojbreaker.domain.solved.Solved;
import ddbreaker.bojbreaker.web.dto.ProblemListRequestDto;
import ddbreaker.bojbreaker.web.dto.ProblemListResponseDto;
import ddbreaker.bojbreaker.web.dto.ProblemResponseDto;
import ddbreaker.bojbreaker.web.dto.SchoolResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SchoolService {

    private final SchoolRepository schoolRepository;
    private final ProblemRepository problemRepository;

    @Transactional
    public List<ProblemResponseDto> findUnsolvedProblems(Long schoolId) {
        School school = schoolRepository.findBySchoolId(schoolId)
                .orElseThrow(() -> new IllegalArgumentException("알 수 없는 학교 번호입니다. school_id="+schoolId));
        Set<Solved> solvedSet = school.getSolvedSet();
        return problemRepository.findAll().stream()
                .filter(problem -> !solvedSet.contains(Solved.builder()
                                                            .problem(problem)
                                                            .school(school)
                                                            .build()))
                .map(entity -> new ProblemResponseDto(
                        entity.getProblemId(),
                        entity.getTitle(),
                        entity.getTier(),
                        entity.getAcTries(),
                        entity.getAvgTries())
                ).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProblemListResponseDto findUnsolvedProblems(Long schoolId, ProblemListRequestDto requestDto) {
        School school = schoolRepository.findBySchoolId(schoolId)
                .orElseThrow(() -> new IllegalArgumentException("알 수 없는 학교 번호입니다. school_id="+schoolId));
        Set<Solved> solvedSet = school.getSolvedSet();

        List<ProblemResponseDto> problems = problemRepository.findAll().stream()
                .filter(problem -> !solvedSet.contains(Solved.builder()
                        .problem(problem)
                        .school(school)
                        .build()))
                .filter(problem -> requestDto.getTierFilter().isEmpty()
                        || requestDto.getTierFilter().contains(problem.getTier()))
                .map(entity -> new ProblemResponseDto(
                        entity.getProblemId(),
                        entity.getTitle(),
                        entity.getTier(),
                        entity.getAcTries(),
                        entity.getAvgTries())
                ).sorted((a, b) -> {
                    switch (requestDto.getSortedBy()) {
                        case "problemId":
                            return Long.compare(a.getProblemId(), b.getProblemId());
                        case "acTries":
                            return Long.compare(a.getAcTries(), b.getAcTries());
                        case "avgTries":
                            return Double.compare(a.getAvgTries(), b.getAvgTries());
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
    public SchoolResponseDto findBySchoolId(Long schoolId) {
        School entity = schoolRepository.findBySchoolId(schoolId)
                .orElseThrow(() -> new IllegalArgumentException("알 수 없는 학교 번호입니다. school_id="+schoolId));
        return new SchoolResponseDto(entity);
    }
}
