package ddbreaker.bojbreaker.domain.school;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SchoolRepository extends JpaRepository<School, Long> {

    Optional<School> findBySchoolId(Long schoolId);
}
