package fi.haagahelia.quizzer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import fi.haagahelia.quizzer.model.Category;
import fi.haagahelia.quizzer.model.Quizz;
import fi.haagahelia.quizzer.repository.CategoryRepository;
import fi.haagahelia.quizzer.repository.QuizzRepository;

@RestController
@RequestMapping("/api/category")
@CrossOrigin(origins = "*")
@Tag(name = "Categories", description = "Operations for accessing and managing the categories")
public class CategoryRestController {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private QuizzRepository quizzRepository;

    @Operation(summary = "List all categories", description = "Retrieves a list of all categories sorted in ascending order by their names.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of categories")
    })
    @GetMapping("")
    public List<Category> CategoriesList() {
        List<Category> categories = categoryRepository.findAllByOrderByNameAsc();
        return categories;
    }

    // filter quizzes by category
    @Operation(summary = "Filter quizzes by category", description = "Filters quizzes by category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the quizz by category"),
            @ApiResponse(responseCode = "400", description = "The quizz with the provided category ID is not published"),
            @ApiResponse(responseCode = "404", description = "Quizz with the provided category ID does not exist")
    })
    @GetMapping("/filterQuizzByCategory/{categoryId}")
    public List<Quizz> filterQuizzesByCategory(@PathVariable("categoryId") Long categoryId) {
        List<Quizz> quizzes = quizzRepository.findAll();
        if (categoryId != 0) {
            Category category = categoryRepository.findOneByCategoryId(categoryId);
            quizzes = quizzRepository.findByCategory(category);
        }
        return quizzes;
    }
}
