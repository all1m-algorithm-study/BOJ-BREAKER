package ddbreaker.bojbreaker.web.dto;

import ddbreaker.bojbreaker.domain.school.School;
import lombok.Getter;

@Getter
public class SchoolResponseDto {

    private Long schoolId;
    private Long solvedCount;

    public SchoolResponseDto(School entity) {
        this.schoolId = entity.getSchoolId();
        this.solvedCount = entity.getSolvedCount();
    }
}
