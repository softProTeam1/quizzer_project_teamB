package fi.haagahelia.quizzer.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fi.haagahelia.quizzer.model.Question;
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
    public List<Quizz> getPublishedQuizzNewestToOldest() {

        // Fetch the list of quizzes with a status of true (published)
        Status status = statusRepository.findByStatus(true);
        List<Quizz> publishedQuizzes = quizzRepository.findByStatus(status);

        // Sort the list by creation time in descending order
        Collections.sort(publishedQuizzes, Comparator.comparing(Quizz::getCreationTime).reversed());

        // Return the sorted list
        return publishedQuizzes;
    }



}