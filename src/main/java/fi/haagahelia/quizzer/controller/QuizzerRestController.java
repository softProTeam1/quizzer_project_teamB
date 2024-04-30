package fi.haagahelia.quizzer.controller;

import fi.haagahelia.quizzer.model.Category;
import fi.haagahelia.quizzer.model.Quizz;
import fi.haagahelia.quizzer.model.Status;
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

import java.util.Comparator;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@Tag(name = "Quizzer", description = "Operations for accessing and managing the quizzes")
public class QuizzerRestController {

    @Autowired
    private QuizzRepository quizzRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private QuestionRepository questionRepository;

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
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Quiz with the provided ID is not published");
                    }
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: Quiz with the provided ID does not exist"));
    }

    @Operation(summary = "Get all published quizzes", description = "Returns all published quizzes")
    @GetMapping("/publishedquizz")
    public ResponseEntity<?> getPublishedQuizzNewestToOldest(@RequestParam(required = false) Long categoryId) {
        List<Quizz> publishedQuizzes;
        // Fetch the list of quizzes with a status of true (published)
        Status status = statusRepository.findByStatus(true);
        if (categoryId == null) {
            List<Quizz> publishedQuizzesNotCategory = quizzRepository.findByStatus(status);
            // Sort the list by creation time in descending order
            publishedQuizzesNotCategory.sort(Comparator.comparing(Quizz::getCreationTime).reversed()); // Sort newest to oldest
            publishedQuizzes = publishedQuizzesNotCategory;
        } else {
            // Check if the category exists
            Optional<Category> categoryLookUp = categoryRepository.findById(categoryId);
            Category category;

            if (categoryLookUp.isPresent()) {
                category = categoryLookUp.get();
            } else
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Category with the provided ID is not found");

            // Get published quizzes by category in descending order of creation
            publishedQuizzes = quizzRepository.findByStatusAndCategory(status, category);
            publishedQuizzes.sort(Comparator.comparing(Quizz::getCreationTime).reversed());  // Sort newest to oldest

        }
        return ResponseEntity.ok(publishedQuizzes);
    }
}