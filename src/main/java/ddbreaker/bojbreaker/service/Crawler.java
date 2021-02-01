package ddbreaker.bojbreaker.service;

import ddbreaker.bojbreaker.domain.problem.SolvedAcTier;
import ddbreaker.bojbreaker.domain.solvedLogs.SolvedLogsRepository;
import ddbreaker.bojbreaker.service.dto.ProblemParseDto;
import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Component
public class Crawler {

    public Long updateSolvedProlems() {
        try {
            Document doc = Jsoup.connect("https://www.acmicpc.net/status?result_id=4&school_id=302").get();
            Element tbody = doc.selectFirst("tbody");
        } catch (java.io.IOException e) {
            System.out.println("jsoup error");
        }
        return 0L;
    }

    public void updateNewProlems() {

    }

    public Long getSolvedCount() {
        try {
            Document doc = Jsoup.connect("https://www.acmicpc.net/ranklist/school").get();
            Element tbody = doc.selectFirst("tbody");
        } catch (java.io.IOException e) {
            System.out.println("jsoup error");
        }
        return 0L;
    }

    // solved.ac에서 각 티어별 문제 parsing
    public List<ProblemParseDto> crawlProblmes(SolvedAcTier tier) throws Exception {
        List<ProblemParseDto> parsedProblems = new ArrayList<>();
        String uri = "https://solved.ac/problems/level/" + tier.ordinal();
        for (int page = 1 ; ; page++) {
            Document document = Jsoup.connect(uri+"?page="+page)
                    .timeout(5000)
                    .get();
            System.out.println(uri+"?page="+page);
            Elements problemIds = document.select(".ProblemInline__ProblemStyle-cvf1lm-0");
            Elements problemTitles = document.select(".hover_underline");
            Elements acTriesAndAvgTries = document.select(".TabularFigures__TabularFiguresStyle-ved8ab-0");

            int numOfProblmes = problemIds.size();

            for (int i = 0; i < numOfProblmes; i++) {
                parsedProblems.add(
                    ProblemParseDto.builder()
                            .id(Long.parseLong(problemIds.get(i).text()))
                            .title(problemTitles.get(i).text())
                            .acTries(Long.parseLong(acTriesAndAvgTries.get(2*i).text().replaceAll(",", "")))
                            .avgTries(Double.parseDouble(acTriesAndAvgTries.get(2*i+1).text()))
                            .tier(tier)
                            .build()
                );
            }
            
            if (problemIds.size() < 100)
                break;
            Thread.sleep((int)(Math.random() * 10000) + 7000);   // 7~10초 대기
        }

        return parsedProblems;
    }
}
