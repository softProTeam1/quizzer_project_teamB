package fi.haagahelia.quizzer.repository;

import fi.haagahelia.quizzer.model.Difficulty;
import org.springframework.data.repository.CrudRepository;


public interface DifficultyRepository extends CrudRepository<Difficulty, Long> {

    Difficulty findByLevel(String level);

}
