package ddbreaker.bojbreaker.domain.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class SchoolRepositoyTest {

    @Autowired
    SchoolRepository schoolRepository;

    @AfterEach
    public void cleanup() {
        schoolRepository.deleteAll();
    }

    @Test
    public void 학교저장_불러오기() {
        // given
        Long schoolId = 302L;
        String schoolName = "서울시립대학교";
        Long lastCrawledSubmitId = 3000L;
        Long solvedCount = 2000L;

        // when
        schoolRepository.save(School.builder()
                .schoolId(schoolId)
                .schoolName(schoolName)
                .lastCrawledSubmitId(lastCrawledSubmitId)
                .solvedCount(solvedCount)
                .build());

        // then
        School school = schoolRepository.findBySchoolId(schoolId)
                .orElseThrow(() -> new IllegalArgumentException("[Id:" + schoolId + "] Id에 해당하는 학교가 없습니다."));
        assertThat(school.getSchoolName()).isEqualTo(schoolName);
        assertThat(school.getSchoolId()).isEqualTo(schoolId);
        assertThat(school.getLastCrawledSubmitId()).isEqualTo(lastCrawledSubmitId);
        assertThat(school.getSolvedCount()).isEqualTo(solvedCount);
    }
}
