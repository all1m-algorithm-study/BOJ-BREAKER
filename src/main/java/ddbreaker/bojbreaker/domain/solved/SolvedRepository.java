package ddbreaker.bojbreaker.domain.solved;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SolvedRepository extends JpaRepository<Solved, Long> {

}
