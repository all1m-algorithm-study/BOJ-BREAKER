package ddbreaker.bojbreaker.domain.group;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class GroupRepositoyTest {

    @Autowired
    GroupRepository groupRepository;

    @BeforeEach
    public void cleanup() {
        groupRepository.deleteAll();
    }

    @Test
    public void 그룹저장_불러오기() {
        // given
        Long code = 302L;
        String name = "서울시립대학교";
        Long rank = 40L;
        Long lastSubmitCode = 3000L;
        Long score = 2000L;

        // when
        groupRepository.save(Group.builder()
                .code(code)
                .name(name)
                .rank(rank)
                .lastSubmitCode(lastSubmitCode)
                .score(score)
                .build());

        // then
        Group group = groupRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("[code:" + code + "] code에 해당하는 그룹이 없습니다."));
        assertThat(group.getName()).isEqualTo(name);
        assertThat(group.getRank()).isEqualTo(rank);
        assertThat(group.getCode()).isEqualTo(code);
        assertThat(group.getLastSubmitCode()).isEqualTo(lastSubmitCode);
        assertThat(group.getScore()).isEqualTo(score);
    }
}
