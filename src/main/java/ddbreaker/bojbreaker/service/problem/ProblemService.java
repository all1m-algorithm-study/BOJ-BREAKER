package ddbreaker.bojbreaker.service.problem;

import ddbreaker.bojbreaker.domain.problem.ProblemRepository;
import ddbreaker.bojbreaker.web.dto.ProblemResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProblemService {

    private final ProblemRepository problemRepository;

    @Transactional(readOnly = true)
    public List<ProblemResponseDto> findAll() {
        return problemRepository.findAll().stream()
                .map(entity -> new ProblemResponseDto(
                        entity.getCode(),
                        entity.getTitle(),
                        entity.getTier(),
                        entity.getAcCnt(),
                        entity.getAcRate())
                ).collect(Collectors.toList());
    }


}
