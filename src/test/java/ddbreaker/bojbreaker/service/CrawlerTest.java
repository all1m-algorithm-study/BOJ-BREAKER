package ddbreaker.bojbreaker.service;

import ddbreaker.bojbreaker.domain.problem.SolvedAcTier;
import ddbreaker.bojbreaker.service.dto.ProblemParseDto;
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
