package fi.haagahelia.quizzer.repository;

import fi.haagahelia.quizzer.model.Difficulty;
import fi.haagahelia.quizzer.model.Question;
import fi.haagahelia.quizzer.model.Quizz;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends CrudRepository<Question, Long> {
    List<Question> findByQuestionId(Long quizzId);

    List<Question> findByQuizzQuizzId(Long quizzId);

    Optional<Question> findByQuizzAndDifficulty(Quizz quizz, Difficulty difficulty);

}
