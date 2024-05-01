package fi.haagahelia.quizzer.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fi.haagahelia.quizzer.model.Status;
import fi.haagahelia.quizzer.repository.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import fi.haagahelia.quizzer.model.Quizz;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@Tag(name = "Quizzer", description = "Operations for accessing and managing the quizzes")
public class QuizzerRestController {

    @Autowired
    private QuizzRepository quizzRepository;
    @Autowired
    private StatusRepository statusRepository;

    // show all quizzes
    @GetMapping("/quizzlist")
    public List<Quizz> showAllQuizz() {
        return (List<Quizz>) quizzRepository.findAll();
    }

    @Operation(summary = "Get a quiz by ID", description = "Returns a quiz by its ID or an appropriate error message if not found or unpublished")
    @GetMapping("/quizz/{id}")
    public ResponseEntity<?> getQuizById(@PathVariable Long id) {
        return quizzRepository.findById(id)
                .map(quiz -> {
                    if (quiz.getStatus() != null && quiz.getStatus().getStatus()) {
                        return ResponseEntity.ok(quiz);
                    } else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body("Error: Quiz with the provided ID is not published");
                    }
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Error: Quiz with the provided ID does not exist"));
    }

    @Operation(summary = "Get all published quizzes", description = "Returns all published quizzes")
    @GetMapping("/publishedquizz")
    public List<Quizz> getPublishedQuizzNewestToOldest() {

        // Fetch the list of quizzes with a status of true (published)
        Status status = statusRepository.findByStatus(true);
        List<Quizz> publishedQuizzes = quizzRepository.findByStatus(status);

        // Sort the list by creation time in descending order
        Collections.sort(publishedQuizzes, Comparator.comparing(Quizz::getCreationTime).reversed());

        // Return the sorted list
        return publishedQuizzes;
    }

    /*
     * @Operation(summary = "Get the all anwers of a quiz", description =
     * "Returns all anwers of a quiz or an appropriate error message if not found or unpublished"
     * )
     * 
     * @GetMapping("/quizz/{quizzId}/answers")
     * public ResponseEntity<?> getQuizAnswers(@PathVariable Long quizzId) {
     * // Check if quiz exists
     * Optional<Quizz> optionalQuizz = quizzRepository.findById(quizzId);
     * if (optionalQuizz.isEmpty()) {
     * return ResponseEntity.status(HttpStatus.NOT_FOUND)
     * .body("Quiz with id " + quizzId + " does not exist");
     * }
     * 
     * Quizz quizz = optionalQuizz.get();
     * 
     * // Check if quiz is published
     * if (!quizz.getStatus().getStatus()) {
     * return ResponseEntity.status(HttpStatus.FORBIDDEN)
     * .body("Quiz with id " + quizzId + " is not published");
     * }
     * 
     * // Get answers for the quiz (send only needed information (DTO))
     * List<Question> questions = questionRepository.findByQuizzQuizzId(quizzId);
     * List<AnswerRequestDto> answers = new ArrayList<>();
     * for (Question question : questions) {
     * AnswerRequestDto answer = new AnswerRequestDto();
     * answer.setQuestionId(question.getQuestionId());
     * answer.setCorrectAnswer(question.getCorrectAnswer());
     * answers.add(answer);
     * }
     * 
     * return ResponseEntity.ok(answers);
     * }
     */
}