package ddbreaker.bojbreaker.service.school;

import ddbreaker.bojbreaker.domain.school.School;
import ddbreaker.bojbreaker.domain.school.SchoolRepository;
import ddbreaker.bojbreaker.service.Crawler;
import ddbreaker.bojbreaker.service.dto.Submit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SchoolService {

    private final SchoolRepository schoolRepository;
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

    // 학교의 모든 attribute를 최신화
    @Transactional
    public Long update(Long schoolId) throws Exception{
        School school = schoolRepository.findBySchoolId(schoolId)
                .orElseThrow(() -> new IllegalArgumentException("[Id:" + schoolId + "] Id에 해당하는 학교가 없습니다."));
        Long beforeSolvedCount = school.getSolvedCount();
        Long nowSolvedCount = crawler.getSolvedCount(schoolId);
        List<Submit> submitList = crawler.getSubmitList(schoolId, school.getLastCrawledSubmitId());
        // 1. submitList를 순회하면서
        //          solvedList를 보고 새로 풀린 문제인지 확인
        //              - 없으면 Entity 만들어서 추가 / school에 추가 / problem에 추가
        // 2. 만약 beforeSolvedCount + 새로 풀린 문제 수 != nowSolvedCount이면 updateAllSolved를 호출
        // 3. school의 solvedCount와 lastCrawledSubmitId를 갱신
        return schoolId;
    }

    public void updateAllSolved(Long schoolId) {
        School school = schoolRepository.findBySchoolId(schoolId)
                .orElseThrow(() -> new IllegalArgumentException("[Id:" + schoolId + "] Id에 해당하는 학교가 없습니다."));

        // 1. 전체 풀린 문제를 파싱
        // 2. 전체 풀린 문제들을 순회하면서
        //      solvedList를 보고 변경사항을 파악 - 없어졌으면 제거 / 새로 생겼으면 추가
        // 3. school의 solvedCount와 lastCrawledSubmitId를 갱신
    }
}
