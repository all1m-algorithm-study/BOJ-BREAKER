package ddbreaker.bojbreaker.service.problem;

import ddbreaker.bojbreaker.domain.problem.Problem;
import ddbreaker.bojbreaker.domain.problem.ProblemRepository;
import ddbreaker.bojbreaker.domain.problem.SolvedAcTier;
import ddbreaker.bojbreaker.service.Crawler;
import ddbreaker.bojbreaker.service.dto.ProblemParseDto;
import ddbreaker.bojbreaker.web.dto.ProblemListRequestDto;
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
    private final Crawler crawler;

    @Transactional
    public Boolean updateAllProblems(int maxRetry) {
        for (SolvedAcTier tier: SolvedAcTier.values()) {
            for (int i=0; i<maxRetry; i++) {
                try {
                    List<ProblemParseDto> problems = crawler.getProblemsFromTier(tier);
                    for (ProblemParseDto p : problems)
                        saveOrUpdate(p);
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(i == maxRetry-1)
                    return false;   //최대 시도 횟수 초과로 실패
            }
        }
        return true;
    }

    private void saveOrUpdate(ProblemParseDto dto) {
        Problem problem =  problemRepository.findByProblemId(dto.getProblemId())
                .map(entity -> entity.update(
                        dto.getTitle(),
                        dto.getTier(),
                        dto.getAcTries(),
                        dto.getAvgTries()))
                .orElse(dto.toEntity());

        problemRepository.save(problem);
    }

    @Transactional(readOnly = true)
    public List<ProblemResponseDto> findAll() {
        return problemRepository.findAll().stream()
                .map(entity -> new ProblemResponseDto(
                        entity.getProblemId(),
                        entity.getTitle(),
                        entity.getTier(),
                        entity.getAcTries(),
                        entity.getAvgTries())
                ).collect(Collectors.toList());
    }


}
