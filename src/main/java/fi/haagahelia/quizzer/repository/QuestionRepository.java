package fi.haagahelia.quizzer.repository;

import fi.haagahelia.quizzer.model.Question;
import fi.haagahelia.quizzer.model.Quizz;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface QuestionRepository extends CrudRepository<Question, Long> {
    List<Question> findByQuestionId(Long quizzId);

    List<Question> findByQuizz(Quizz quizz);

    List<Question> findByQuizzAndDifficulty(Quizz quizz, String difficulty);
}
