package ddbreaker.bojbreaker.service.school;

import ddbreaker.bojbreaker.domain.problem.Problem;
import ddbreaker.bojbreaker.domain.problem.ProblemRepository;
import ddbreaker.bojbreaker.domain.problem.SolvedAcTier;
import ddbreaker.bojbreaker.domain.school.School;
import ddbreaker.bojbreaker.domain.school.SchoolRepository;
import ddbreaker.bojbreaker.domain.solved.Solved;
import ddbreaker.bojbreaker.domain.solved.SolvedRepository;
import ddbreaker.bojbreaker.service.Crawler;
import ddbreaker.bojbreaker.service.dto.Submit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
            if (problem.isEmpty()) {
                continue;
            }
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
}
