package ddbreaker.bojbreaker.service.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Submit {
    private Long submitId;
    private String userId;
    private Long problemId;
    private String language;
    private LocalDateTime submitTime;

    @Builder
    public Submit(Long submitId, String userId, Long problemId, String language, LocalDateTime submitTime) {
        this.submitId = submitId;
        this.userId = userId;
        this.problemId = problemId;
        this.language = language;
        this.submitTime = submitTime;
    }
}
