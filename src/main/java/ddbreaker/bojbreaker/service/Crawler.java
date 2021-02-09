package ddbreaker.bojbreaker.service;

import ddbreaker.bojbreaker.domain.problem.SolvedAcTier;
import ddbreaker.bojbreaker.service.dto.ProblemParseDto;
import ddbreaker.bojbreaker.service.dto.Submit;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class Crawler {

    // 우리학교가 새롭게 푼 문제 parsing
    public List<Submit> getSubmitList(Long schoolId, Long lastSubmitId) throws Exception {
        List<Submit> submitList = new ArrayList<>();
        Long curSubmitId = -1L;
        String uri = "https://www.acmicpc.net/status?result_id=4&school_id=" + schoolId;
        Document document = Jsoup.connect(uri).get();
        while (true) {
            Element statusTable = document.selectFirst("#status-table");
            Elements trs = statusTable.select("tr");
            for (int i = 1; i < trs.size(); i++) {
                Elements tds = trs.get(i).select("td");
                // 채점번호
                curSubmitId = Long.parseLong(tds.get(0).text());
                // 아이디
                String userId = tds.get(1).text();
                // 문제번호
                Long problemId = Long.parseLong(tds.get(2).text());
                // 언어
                String language = tds.get(6).text();
                // 제출시간
                String submitTimeStr = tds.get(8).selectFirst("a[href]").attr("title");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime submitTime = LocalDateTime.parse(submitTimeStr, formatter);
                // 마지막 채점 번호에 도달
                if (curSubmitId <= lastSubmitId) {
                    return submitList;
                }
                // Add Submit
                submitList.add(
                        Submit.builder()
                        .submitId(curSubmitId)
                        .userId(userId)
                        .problemId(problemId)
                        .language(language)
                        .submitTime(submitTime)
                        .build()
                );
            }
            Thread.sleep(300);
            document = Jsoup.connect(uri + "&top=" + (curSubmitId - 1)).get();
        }
    }

    // 우리학교의 최근(1페이지 최하단) 제출번호 parsing
    public Long getLastCrawledSubmitId(Long schoolId) throws Exception {
        String uri = "https://www.acmicpc.net/status?result_id=4&school_id=" + schoolId;
        Document document = Jsoup.connect(uri).get();
        Element statusTable = document.selectFirst("#status-table");
        Elements trs = statusTable.select("tr");
        Elements tds = trs.last().select("td");
        return Long.parseLong(tds.get(0).text());
    }

    // 우리학교가 푼 문제 수 parsing
    public Long getSolvedCount(Long schoolId) throws Exception {
        String uri = "https://www.acmicpc.net/ranklist/school";
        Document document = Jsoup.connect(uri).get();
        Element rankList = document.selectFirst("#ranklist");
        Elements trs = rankList.select("tr");
        for (int i = 1; i < trs.size(); i++) {
            Elements tds = trs.get(i).select("td");
            Element link = tds.get(4).selectFirst("a[href]");
            if (link.attr("href").equals(String.format("/status?school_id=%d&result_id=4", schoolId))) {
                return Long.parseLong(link.text());
            }
        }
        return -1L;
    }

    public List<Long> getSchoolSolvedList(Long schoolId) throws Exception {
        Set<Long> solvedSet = new TreeSet<>();
        List<String> schoolUserList = getSchoolUserList(schoolId);
        String uri = "https://www.acmicpc.net/user/";
        for (String schoolUser : schoolUserList) {
            Document document = Jsoup.connect(uri + schoolUser).get();
            Element panel = document.select("div[class=panel panel-default]").get(0);
            List<Long> problemIds = Arrays.stream(
                    panel.selectFirst("div.panel-body").text().split(" "))
                    .mapToLong(Long::parseLong)
                    .boxed()
                    .collect(Collectors.toList());
            solvedSet.addAll(problemIds);
            Thread.sleep(300);
        }
        return new ArrayList<>(solvedSet);
    }

    public List<String> getSchoolUserList(Long schoolId) throws Exception {
        List<String> userList = new ArrayList<>();
        String uri = "https://www.acmicpc.net/school/ranklist/" + schoolId;
        Document document = Jsoup.connect(uri).get();
        // page
        int pageSize = document.select("ul.pagination > li").size() - 2;
        for (int page = 1; page <= pageSize; page++) {
            document = Jsoup.connect(uri + '/' + page).get();
            Element rankList = document.selectFirst("#ranklist");
            Elements trs = rankList.select("tr");
            for (int i = 1; i < trs.size(); i++) {
                Elements tds = trs.get(i).select("td");
                String userId = tds.get(1).text();
                userList.add(userId);
            }
            Thread.sleep(300);
        }
        return userList;
    }

    // solved.ac에서 각 티어별 문제 parsing
    public List<ProblemParseDto> getProblemsFromTier(SolvedAcTier tier) throws Exception {
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
                            .problemId(Long.parseLong(problemIds.get(i).text()))
                            .title(problemTitles.get(i).text())
                            .acTries(Long.parseLong(acTriesAndAvgTries.get(2*i).text().replaceAll(",", "")))
                            .avgTries(Double.parseDouble(acTriesAndAvgTries.get(2*i+1).text().replaceAll(",", "")))
                            .tier(tier)
                            .build()
                );
            }
            
            if (problemIds.size() < 100)
                break;
            Thread.sleep((int)(Math.random() * 1500) + 500);   // 0.5 ~ 1.5 초 대기
        }

        return parsedProblems;
    }
}
