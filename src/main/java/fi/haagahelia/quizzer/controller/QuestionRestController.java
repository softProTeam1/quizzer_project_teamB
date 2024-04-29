package fi.haagahelia.quizzer.controller;

import fi.haagahelia.quizzer.model.Question;
import fi.haagahelia.quizzer.model.Quizz;
import fi.haagahelia.quizzer.repository.CategoryRepository;
import fi.haagahelia.quizzer.repository.QuestionRepository;
import fi.haagahelia.quizzer.repository.QuizzRepository;
import fi.haagahelia.quizzer.repository.StatusRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@Tag(name = "Question", description = "Operations for accessing and managing the questions")
public class QuestionRestController {
    @Autowired
    private QuizzRepository quizzRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @Operation(summary = "Get all questions by quizz ID", description = "Returns all questions of a quizz by ID")
    @RequestMapping("/api/quizzlist")

    @GetMapping("/{quizzId}/questions")
    // endpoint path /api/quizzlist/{quizzId}/questions/?{difficultyId}

    public ResponseEntity<?> getQuizQuestions(
            @PathVariable("quizzId") Long quizzId,
            @RequestParam(value = "difficulty", required = false) String difficulty) {

        // Retrieve quiz and check if it exists
        Optional<Quizz> quizOptional = quizzRepository.findById(quizzId);
        if (quizOptional.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Error: Quiz with the provided ID does not exist");
        }

        Quizz quizz = quizOptional.get();

        // Check if the quiz is published
        if (quizz.getStatus() == null || !quizz.getStatus().getStatus()) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Error: Quiz with the provided ID is not published");
        }

        // Get questions with optional filtering by difficulty
        List<Question> questions;
        if (difficulty == null) {
//         Retrieve all questions if difficulty is not specified
            questions = questionRepository.findByQuizz(quizz);

        } else {
            // Retrieve questions filtered by difficulty ID
            questions = questionRepository.findByQuizzAndDifficulty(quizz, difficulty);
        }

        return ResponseEntity.ok(questions);

    }
}
