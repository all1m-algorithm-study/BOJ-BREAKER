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
    public void 특정_티어_문제_크롤링() throws Throwable {
        //given
        SolvedAcTier tier = SolvedAcTier.GOLD1;

        //when
        List<ProblemParseDto> problems = crawler.crawlProblmes(tier);

        //then
//        assertThat(problems.get(0).getId()).isEqualTo(1123);
        System.out.println(problems.size());
        for (ProblemParseDto dto : problems) {
            System.out.println(dto.getId() + " " + dto.getTitle());
        }
    }
}
