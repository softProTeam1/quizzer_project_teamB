package fi.haagahelia.quizzer.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid request body, answer text cannot be blank");
        }

        Question question = questionRepository.findById(answerRequestDto.getQuestionId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Quiz with the provided id does not exist"));

        Quizz quizz = question.getQuizz();

        if (!quizz.getStatus().getStatus()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quiz with the provided id is not published");
        }

        Answer answer = new Answer();
        answer.setAnswerText(answerRequestDto.getAnswerText());
        answer.setCorrectness(answerRequestDto.getCorrectness());
        answer.setQuestion(question);

        return answerRepository.save(answer);
    }

@GetMapping("/quizz/{quizId}/answers")
public List<AnswerRequestDto> getQuizAnswers(@PathVariable Long quizId) {
    Optional<Quizz> quizOptional = quizzRepository.findById(quizId);
    if (quizOptional.isEmpty()) {
        // Quiz with the provided id does not exist
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz with the provided id does not exist");
    }

    Quizz quizz = quizOptional.get();

    if (!quizz.getStatus().getStatus()) {
        // Quiz with the provided id is not published
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quiz with the provided id is not published");
    }

    List<Answer> answers = answerRepository.findByQuestionQuizzQuizzId(quizId);

    // Map Answer entities to AnswerRequestDto objects
    List<AnswerRequestDto> answerRequestDtos = new ArrayList<>();
    for (Answer answer : answers) {
        AnswerRequestDto answerRequestDto = new AnswerRequestDto();
        answerRequestDto.setAnswerText(answer.getAnswerText());
        answerRequestDto.setQuestionId(answer.getQuestion().getQuestionId()); // Set questionId
        answerRequestDto.setCorrectness(answer.getCorrectness());
        answerRequestDtos.add(answerRequestDto);
    }

    return answerRequestDtos;
}


    

}
