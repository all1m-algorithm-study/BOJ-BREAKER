package ddbreaker.bojbreaker.service;

import ddbreaker.bojbreaker.domain.problem.SolvedAcTier;
import ddbreaker.bojbreaker.service.dto.ProblemParseDto;
import ddbreaker.bojbreaker.service.dto.Submit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CrawlerTest {
    Crawler crawler;

    @Autowired
    public CrawlerTest(Crawler crawler) {
        this.crawler = crawler;
    }

    @Test
    public void 특정_학교_채점현황_크롤링() throws Exception {
        //given
        Long schoolId = 302L;
        Long lastSubmitId = 26088286L;

        //when
        List<Submit> submitList = crawler.getSubmitList(schoolId, lastSubmitId);

        //then
        for (Submit submit : submitList) {
            System.out.println(submit.getSubmitId() + " " + submit.getUserId() + submit.getProblemId());
        }
        assertThat(submitList.size()).isGreaterThan(0);
    }

    @Test
    public void 특정_학교_최근채점번호_크롤링() throws Exception {
        //given
        Long schoolId = 302L;

        //when
        Long submitId = crawler.getLastCrawledSubmitId(schoolId);

        //then
        System.out.println(submitId);
        assertThat(submitId).isGreaterThan(0);
    }

    @Test
    public void 특정_학교_푼_문제수_크롤링() throws Exception {
        //given
        Long schoolId = 302L;

        //when
        Long solvedCount = crawler.getSolvedCount(schoolId);

        //then
        System.out.println(solvedCount);
        assertThat(solvedCount).isGreaterThan(0L);
    }

    @Test
    public void 특정_학교_푼_문제목록_크롤링() throws Exception {
        //given
        Long schoolId = 766L;

        //when
        List<Long> schoolSolvedList = crawler.getSchoolSolvedList(schoolId);

        //then
        System.out.println(schoolSolvedList.size());
        System.out.println(schoolSolvedList);
        assertThat(schoolSolvedList.size()).isGreaterThan(0);
    }

    @Test
    public void 특정_학교_유저목록_크롤링() throws Exception {
        //given
        Long schoolId = 302L;

        //when
        List<String> schoolUserList = crawler.getSchoolUserList(schoolId);

        //then
        for (String schoolUser : schoolUserList) {
            System.out.println(schoolUser);
        }
        assertThat(schoolUserList.size()).isGreaterThan(0);
    }

    @Test
    public void 특정_티어_문제_크롤링() throws Throwable {
        //given
        SolvedAcTier tier = SolvedAcTier.DIAMOND4;

        //when
        List<ProblemParseDto> problems = crawler.getProblemsFromTier(tier);

        //then
//        assertThat(problems.get(0).getId()).isEqualTo(1123);
        assertThat(problems.size()).isGreaterThan(0);
        System.out.println(problems.size());
        for (ProblemParseDto dto : problems) {
            System.out.println(dto.getProblemId() + " " + dto.getTitle());
        }
    }
}
