package fi.haagahelia.quizzer.repository;

import fi.haagahelia.quizzer.model.Difficulty;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DifficultyRepository extends CrudRepository<Difficulty, Long> {

    Difficulty findByLevel(String level);

}
