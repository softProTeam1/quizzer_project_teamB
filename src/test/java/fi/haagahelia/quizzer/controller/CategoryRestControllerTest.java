package fi.haagahelia.quizzer.controller;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.haagahelia.quizzer.model.Category;
import fi.haagahelia.quizzer.repository.CategoryRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryRestControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private CategoryRepository categoryRepository;

        ObjectMapper mapper = new ObjectMapper();

        @BeforeEach
        void setUp() throws Exception {
                categoryRepository.deleteAll();
        }

        @Test
        public void getAllCategoriesReturnsEmptyListWhenNoCategoriesExist() throws Exception {

                // Act
                this.mockMvc.perform(get("/api/category"))

                                // Assert
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(0)));
        }

        @Test
        public void getAllCategoriesReturnsListOfCategoriesWhenCategoryExist() throws Exception {

                // Arrange
                Category category1 = new Category("Test Category Name 1", "Test Category Description");
                Category category2 = new Category("Test Category Name 2", "Test Category Description");
                Category category3 = new Category("Test Category Name 3", "Test Category Description");
                categoryRepository.saveAll(List.of(category1, category2, category3));

                // Act
                this.mockMvc.perform(get("/api/category"))

                                // Assert
                                .andExpect(status().isOk())

                                .andExpect(jsonPath("$", hasSize(3)))
                                .andExpect(jsonPath("$[0].name").value("Test Category Name 1"))
                                .andExpect(jsonPath("$[0].description").value("Test Category Description"))

                                .andExpect(jsonPath("$[1].name").value("Test Category Name 2"))
                                .andExpect(jsonPath("$[1].description").value("Test Category Description"))

                                .andExpect(jsonPath("$[2].name", not(hasItem("Test Category Name 3"))))
                                .andExpect(jsonPath("$[2].description").value("Test Category Description"));
        }
}
