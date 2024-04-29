package fi.haagahelia.quizzer.repository;

import fi.haagahelia.quizzer.model.Difficulty;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface DifficultyRepository extends CrudRepository<Difficulty, Long> {
    Optional<Difficulty> findByLevel(String level);

}
