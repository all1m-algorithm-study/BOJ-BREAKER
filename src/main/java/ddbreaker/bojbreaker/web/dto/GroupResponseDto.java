package ddbreaker.bojbreaker.web.dto;

import ddbreaker.bojbreaker.domain.group.Group;
import lombok.Getter;

@Getter
public class GroupResponseDto {

    private Long code;
    private String name;
    private Long rank;
    private Long score;

    public GroupResponseDto(Group entity) {
        this.code = entity.getCode();
        this.name = entity.getName();
        this.rank = entity.getRank();
        this.score = entity.getScore();
    }
}
