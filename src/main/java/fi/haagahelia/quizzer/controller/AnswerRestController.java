package fi.haagahelia.quizzer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import fi.haagahelia.quizzer.dto.AnswerRequestDto;
import fi.haagahelia.quizzer.model.Answer;
import fi.haagahelia.quizzer.model.Question;
import fi.haagahelia.quizzer.model.Quizz;
import fi.haagahelia.quizzer.repository.AnswerRepository;
import fi.haagahelia.quizzer.repository.QuestionRepository;
import fi.haagahelia.quizzer.repository.QuizzRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/answer")
public class AnswerRestController {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private QuizzRepository quizzRepository;

    @PostMapping("/add")
    public Answer createAnswer(@Valid @RequestBody AnswerRequestDto answerRequestDto, BindingResult bindingResult) {
        // invalid request body
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid request body, answer text cannot be blank");
        }
        Question question = questionRepository.findById(answerRequestDto.getQuestionId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "question with questionId does not exist"));

        // handle quiz id not found
        Quizz quiz = quizzRepository.findById(question.getQuizz().getQuizzId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "quiz with the quizzId does not exist"));

        // handle quizz not publish
        if (!quiz.getStatus().getStatus()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quiz with the provided id is not published");
        }

        Answer answer = new Answer(answerRequestDto.getAnswerText(), answerRequestDto.getCorrectness());
        answer.setQuizz(quiz);

        return answerRepository.save(answer);
    }

    @GetMapping("/{quizId}")
    public List<Answer> getQuizAnswers(@PathVariable Long quizId) {
        // get the quizz by quizz id if quiz with id doesn't exist throw 404
        Quizz quiz = quizzRepository.findById(quizId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz with the id does not exist"));
        // if the quizz is not publish then throw 400
        if (!quiz.getStatus().getStatus()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quiz with the provided id is not published");
        }
        // get all the answer of the quiz via the quizId
        List<Answer> answers = answerRepository.findByQuizz(quiz);

        // return answer as a JSON without needing any questionId
        return answers;
    }

}
