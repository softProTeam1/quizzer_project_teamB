package fi.haagahelia.quizzer.controller;

import fi.haagahelia.quizzer.model.Category;
import fi.haagahelia.quizzer.model.Quizz;
import fi.haagahelia.quizzer.model.Status;
import fi.haagahelia.quizzer.repository.CategoryRepository;
import fi.haagahelia.quizzer.repository.QuizzRepository;
import fi.haagahelia.quizzer.repository.StatusRepository;
import fi.haagahelia.quizzer.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;


@RestController
@RequestMapping("/api/quizzer")
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
    private UserRepository userRepository;

    // show all quizzes
    @GetMapping("")
    public List<Quizz> showAllQuizz() {
        return (List<Quizz>) quizzRepository.findAll();
    }

    @Operation(summary = "Get a quiz by ID", description = "Returns a quiz by its ID or an appropriate error message if not found or unpublished")
    @ApiResponses(value = {
            // The responseCode property defines the HTTP status code of the response
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the quiz"),
            @ApiResponse(responseCode = "400", description = "The quiz with the provided ID is not published"),
            @ApiResponse(responseCode = "404", description = "Quiz with the provided ID does not exist")
    })

    // list quiz by Id
    @Operation(summary = "Get a quiz by ID", description = "Returns a quiz by its ID or an appropriate error message if not found or unpublished")
    @GetMapping("/quizz/{quizzId}")
    public Quizz getQuizById(@PathVariable Long quizzId) {
        // get the quizz by quizzId
        Quizz quiz = quizzRepository.findById(quizzId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Error: Quiz with the provided ID does not exist"));
        // if the quizz is not publish then throw 400
        if (!quiz.getStatus().getStatus()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: Quiz with the provided ID is not published");
        }

        return quiz;
    }
    @Operation(summary = "Get all published quizzes", description = "Returns all published quizzes and optional filtered by CategoryId")
    // http://localhost:8080/api/publishedquizz?categoryId=2
    @ApiResponses(value = {
            // The responseCode property defines the HTTP status code of the response
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "published quizzes with the provided category id does not exist")
    })
    // list all published quiz
    @GetMapping("/publishedquizz")
    public List<Quizz> getPublishedQuizzNewestToOldest(
            @RequestParam(name = "category", required = false) Long categoryId) {
        // get status object true (published)
        Status status = statusRepository.findByStatus(true);
        // Check if the categoryId is provided
        if (categoryId == null) {
            // find list of quiz by status true (published)
            List<Quizz> publishedQuizzesNotCategory = quizzRepository.findByStatus(status);
            // Sort the list by creation time in descending order
            // Sort newest to oldest
            publishedQuizzesNotCategory.sort(Comparator.comparing(Quizz::getCreationTime).reversed());
            return publishedQuizzesNotCategory;
        }
        // handle when there is optional parameter for category id
        else {
            List<Quizz> publishedQuizzes;
            // handle if categoryId is not found
            Category category = categoryRepository.findById(categoryId).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "category with provided id did not exist"));
            // Get published quizzes by categoryId
            publishedQuizzes = quizzRepository.findByStatusAndCategory(status, category);
            if (publishedQuizzes.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quizz is not published");
            }
            // Sort newest to oldest
            publishedQuizzes.sort(Comparator.comparing(Quizz::getCreationTime).reversed());
            return publishedQuizzes;
        }
    }


}