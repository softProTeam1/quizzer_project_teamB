package fi.haagahelia.quizzer.repository;

import fi.haagahelia.quizzer.model.Difficulty;
import fi.haagahelia.quizzer.model.Question;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface QuestionRepository extends CrudRepository<Question, Long> {
    List<Question> findByQuestionId(Long quizzId);

    List<Question> findByQuizzQuizzId(Long quizzId);

    List<Question> findByQuizzQuizzIdAndDifficulty(Long quizzId, Difficulty difficulty);

}
