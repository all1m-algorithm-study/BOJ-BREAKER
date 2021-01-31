package ddbreaker.bojbreaker.service;

import ddbreaker.bojbreaker.domain.solvedLogs.SolvedLogsRepository;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

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
}
