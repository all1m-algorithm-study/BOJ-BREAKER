package ddbreaker.bojbreaker.service.school;

import ddbreaker.bojbreaker.domain.problem.Problem;
import ddbreaker.bojbreaker.domain.problem.ProblemRepository;
import ddbreaker.bojbreaker.domain.school.School;
import ddbreaker.bojbreaker.domain.school.SchoolRepository;
import ddbreaker.bojbreaker.domain.solved.Solved;
import ddbreaker.bojbreaker.domain.solved.SolvedRepository;
import ddbreaker.bojbreaker.service.Crawler;
import ddbreaker.bojbreaker.service.dto.Submit;
import ddbreaker.bojbreaker.web.dto.ProblemListRequestDto;
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
    private final SolvedRepository solvedRepository;
    private final Crawler crawler;

    // 학교를 최초로 등록하여 초기화
    @Transactional
    public Long register(Long schoolId) throws Exception{
        // Entity 생성
        School school = School.builder()
                .schoolId(schoolId)
                .schoolName(crawler.getSchoolName(schoolId))
                .lastCrawledSubmitId(0L)
                .solvedCount(0L)
                .build();
        schoolRepository.save(school);
        updateAllSolved(schoolId);
        return schoolId;
    }

    // 학교의 푼 문제 목록 최신화
    @Transactional
    public Long update(Long schoolId) throws Exception{
        School school = schoolRepository.findBySchoolId(schoolId)
                .orElseThrow(() -> new IllegalArgumentException("[Id:" + schoolId + "] Id에 해당하는 학교가 없습니다."));
        Set<Solved> newSolvedSet = new HashSet<>();
        Long nowSolvedCount = crawler.getSolvedCount(schoolId);
        // 새로 맞은 문제를 newSolvedSet에 넣는다.
        List<Submit> submitList = crawler.getSubmitList(schoolId, school.getLastCrawledSubmitId());
        for (Submit submit : submitList) {
            Problem problem = problemRepository.findByProblemId(submit.getProblemId())
                    .orElseThrow(() -> new IllegalArgumentException("[Id:" + submit.getProblemId() + "] Id에 해당하는 문제가 없습니다."));
            Solved solved = Solved.builder()
                    .problem(problem)
                    .school(school)
                    .solvedDate(submit.getSubmitTime())
                    .solvedUser(submit.getUserId())
                    .build();
            if (!school.getSolvedSet().contains(solved)) {
                newSolvedSet.add(solved);
            }
        }
        // 재채점 검사
        if (school.getSolvedCount() + newSolvedSet.size() == nowSolvedCount) {
            // 새로 맞은 문제 저장
            for (Solved solved : newSolvedSet) {
                solved.getSchool().getSolvedSet().add(solved);
                solved.getProblem().getSolvedSet().add(solved);
                solvedRepository.save(solved);
            }
            // 푼 문제수, 마지막 제출번호 최신화
            school.update(nowSolvedCount, submitList.get(0).getSubmitId());
        } else {
            // 재채점으로 인한 푼 문제 수 불일치
            updateAllSolved(schoolId);
        }
        return schoolId;
    }

    @Transactional
    public void updateAllSolved(Long schoolId) throws Exception {
        School school = schoolRepository.findBySchoolId(schoolId)
                .orElseThrow(() -> new IllegalArgumentException("[Id:" + schoolId + "] Id에 해당하는 학교가 없습니다."));

        Set<Long> schoolSolvedProblemIdSet = crawler.getSchoolSolvedProblemIdSet(schoolId);
        // 재채점으로 틀린 문제 삭제
        for (Solved solved : school.getSolvedSet()) {
            if (!schoolSolvedProblemIdSet.contains(solved.getProblem().getProblemId())) {
                solved.getProblem().getSolvedSet().remove(solved);
                solved.getProblem().getSolvedSet().remove(solved);
                solvedRepository.delete(solved);
            }
        }
        // 재채점으로 맞은 문제 추가
        for (Long schoolSolvedProblemId : schoolSolvedProblemIdSet) {
            Optional<Problem> problem = problemRepository.findByProblemId(schoolSolvedProblemId);
            if (problem.isEmpty())
                continue;
            Solved solved = Solved.builder()
                    .problem(problem.get())
                    .school(school)
                    .build();
            if (!school.getSolvedSet().contains(solved)) {
                school.getSolvedSet().add(solved);
                problem.get().getSolvedSet().add(solved);
                solvedRepository.save(solved);
            }
        }
        // 푼 문제수, 마지막 재출번호 최신화
        school.update(crawler.getSolvedCount(schoolId),crawler.getLastCrawledSubmitId(schoolId));
    }

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
    public List<ProblemResponseDto> findUnsolvedProblems(ProblemListRequestDto requestDto) {
        School school = schoolRepository.findBySchoolId(requestDto.getSchoolId())
                .orElseThrow(() -> new IllegalArgumentException("알 수 없는 학교 번호입니다. school_id="+requestDto.getSchoolId()));
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

        if(requestDto.getPage() < 1)
            return problems.subList(0, Integer.min(99, problems.size()));
        else if(requestDto.getPage() < lastPage)
            return problems.subList(100*(requestDto.getPage()-1), 100*requestDto.getPage());
        else if(requestDto.getPage() == lastPage)
            return problems.subList(100*(lastPage-1), problems.size());
        else
            return new ArrayList<ProblemResponseDto>();
    }

    @Transactional(readOnly = true)
    public SchoolResponseDto findBySchoolId(Long schoolId) {
        School entity = schoolRepository.findBySchoolId(schoolId)
                .orElseThrow(() -> new IllegalArgumentException("알 수 없는 학교 번호입니다. school_id="+schoolId));
        return new SchoolResponseDto(entity);
    }
}
