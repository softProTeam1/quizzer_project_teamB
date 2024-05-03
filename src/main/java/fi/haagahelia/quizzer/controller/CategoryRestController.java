package fi.haagahelia.quizzer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
    
}
