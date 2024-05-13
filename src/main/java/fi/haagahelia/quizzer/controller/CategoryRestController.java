package fi.haagahelia.quizzer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import fi.haagahelia.quizzer.model.Category;
import fi.haagahelia.quizzer.repository.CategoryRepository;

@RestController
@RequestMapping("/api/category")
@CrossOrigin(origins = "*")
public class CategoryRestController {
    @Autowired
    private CategoryRepository categoryRepository;

    @Operation(summary = "List all categories",
            description = "Retrieves a list of all categories sorted in ascending order by their names.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of categories")
    })
    @GetMapping("")
    public List<Category> CategoriesList() {
        List<Category> categories = categoryRepository.findAllByOrderByNameAsc();
        return categories;
    }

//    @Operation(summary = "Get all published categories", description = "Returns all published categories filtered by published quizzes")
//    @ApiResponses(value = {
//            // The responseCode property defines the HTTP status code of the response
//            @ApiResponse(responseCode = "200", description = "Successful operation"),
//            @ApiResponse(responseCode = "404", description = "published quizzes with the provided category id does not exist")
//    })
//    @GetMapping("/publishedcategories")
//    public List<Category> getPublishedCategories(
//            @RequestParam(name = "quizzes", required = false) Long quizId) {
//        // get status object for published status
//        Status status = statusRepository.findByStatus(true);
//
//        if (quizId == null) {
//            // If no quizId is provided, return all categories whose quizzes are published
//            return categoryRepository.findCategoriesByQuizzesPublished(true);
//        } else {
//            // If quizId is provided, validate the quiz
//            Quizz quiz = quizzRepository.findById(quizId).orElseThrow(
//                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz with provided id does not exist"));
//
//            // Get categories associated with this quiz
//            List<Category> publishedCategories = categoryRepository.findByQuizzes(quiz);
//            if (publishedCategories.isEmpty()) {
//                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No published categories associated with this quiz");
//            }
//            return publishedCategories;
//    }
}