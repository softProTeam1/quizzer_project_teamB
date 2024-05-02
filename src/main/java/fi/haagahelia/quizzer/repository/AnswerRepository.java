package fi.haagahelia.quizzer.repository;
import org.springframework.data.repository.CrudRepository;

import fi.haagahelia.quizzer.model.Answer;
import fi.haagahelia.quizzer.model.Quizz;


import java.util.List;


public interface AnswerRepository extends CrudRepository<Answer,Long>{
    List<Answer> findByAnswerText(String answerText);
    List<Answer> findByQuizz(Quizz quizz);


}
