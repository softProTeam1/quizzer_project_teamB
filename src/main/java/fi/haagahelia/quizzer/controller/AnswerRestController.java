package fi.haagahelia.quizzer.controller;

import java.util.List;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@CrossOrigin(origins = "*")
public class AnswerRestController {
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private QuizzRepository quizzRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @Operation(summary = "Create an answer for a quiz's question", description = "Creates and returns an answer for a specific quiz's question by quiz ID. Checks if the quiz does not exist, is published and the answer is invalid.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Answer created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request due to validation errors in the request body or quiz is not published"),
            @ApiResponse(responseCode = "404", description = "Question or quiz with the provided ID does not exist")
    })
    @PostMapping("/add")
    public Answer createAnswer(@Valid @RequestBody AnswerRequestDto answerRequestDto, BindingResult bindingResult) {
        // invalid request body
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid request body, answer text cannot be blank");
        }
        //handle if question id is not found
        Question question = questionRepository.findById(answerRequestDto.getQuestionId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "question with question id does not exist"));
        // handle quiz id not found
        Quizz quiz = quizzRepository.findById(question.getQuizz().getQuizzId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "quiz with the quizzId does not exist"));

        // handle quizz not publish
        if (!quiz.getStatus().getStatus()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quiz with the provided id is not published");
        }

        Answer answer = new Answer(answerRequestDto.getAnswerText(), answerRequestDto.getCorrectness());
        answer.setQuestion(question);

        return answerRepository.save(answer);
    
    }

    @Operation(summary = "Get a quiz by ID", description = "Returns answers of a quiz by quiz ID or an appropriate error message if not found or unpublished")
    @ApiResponses(value = {
            // The responseCode property defines the HTTP status code of the response
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all answers for the quiz"),
            @ApiResponse(responseCode = "400", description = "Quiz with the provided ID is not published"),
            @ApiResponse(responseCode = "404", description = "Quiz with the provided ID does not exist")
    })
    @GetMapping("/quiz/{quizId}")
    public List<Answer> getQuizAnswers(@PathVariable Long quizId) {
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

        List<Answer> answers = answerRepository.findByQuestionQuizz(quizz);

        return answers;
}


    

}
