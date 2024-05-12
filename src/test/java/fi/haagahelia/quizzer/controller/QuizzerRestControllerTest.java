package fi.haagahelia.quizzer.controller;

import fi.haagahelia.quizzer.model.Category;
import fi.haagahelia.quizzer.model.Quizz;
import fi.haagahelia.quizzer.model.Status;
import fi.haagahelia.quizzer.repository.QuizzRepository;
import fi.haagahelia.quizzer.repository.StatusRepository;
import fi.haagahelia.quizzer.repository.CategoryRepository;
import fi.haagahelia.quizzer.repository.DifficultyRepository;
import fi.haagahelia.quizzer.repository.QuestionRepository;
import fi.haagahelia.quizzer.repository.AnswerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(QuizzerRestController.class)
public class QuizzerRestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private QuizzRepository quizzRepository;
    
    @MockBean
    private QuestionRepository questionRepository;
    
    @MockBean
    private StatusRepository statusRepository;
    
    @MockBean
    private CategoryRepository categoryRepository;
    
    @MockBean
    private DifficultyRepository difficultyRepository;

    @MockBean
    private AnswerRepository answerRepository;
    

    @Test
    public void getAllQuizzesReturnsEmptyListWhenNoQuizzesExist() throws Exception {
        // Act
        this.mockMvc.perform(get("/api/quizzer"))
        // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

 

@Test
public void getAllQuizzesReturnsListOfPublishedQuizzesWhenQuizzesExist() throws Exception {
    // Arrange
    // Create a published status
    Status publishedStatus = new Status(true);
    statusRepository.save(publishedStatus);

    // Create a non-published status
    Status nonPublishedStatus = new Status(false);
    statusRepository.save(nonPublishedStatus);

    // Create a category
    Category category = new Category("Test Category Name","Test Category Description");
    categoryRepository.save(category);

    // Save a few quizzes (both published and non-published)
    Quizz publishedQuizz1 = new Quizz("Published Quizz 1", "Description", publishedStatus, category);
    Quizz publishedQuizz2 = new Quizz("Published Quizz 2", "Description", publishedStatus, category);
    Quizz nonPublishedQuizz = new Quizz("Non-Published Quizz", "Description", nonPublishedStatus, category);
    quizzRepository.saveAll(List.of(publishedQuizz1, publishedQuizz2, nonPublishedQuizz));

    // Act
    // Send a request to retrieve all quizzes
    this.mockMvc.perform(get("/api/quizzer"))
    // Assert
            .andExpect(status().isOk())
            // Verify that the response contains a list of the saved published quizzes
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].name").value("Published Quizz 1"))
            .andExpect(jsonPath("$[0].id").value(publishedQuizz1.getQuizzId()))

            .andExpect(jsonPath("$[1].name").value("Published Quizz 2"))
            .andExpect(jsonPath("$[1].id").value(publishedQuizz2.getQuizzId()))

            .andExpect(jsonPath("$[2].name", not(hasItem("Non-Published Quizz"))))
            .andExpect(jsonPath("$[2].id").value(nonPublishedQuizz.getQuizzId()));

}

// Test method for success case of category filtering
    @Test
    public void getPublishedQuizzByCategoryReturnsQuizzesWhenCategoryExists() throws Exception {
        // Mock category and quizzes data
        Category category = new Category("Test Category Name","Test Category Description");
        Quizz quiz1 = new Quizz("Quiz 1", "Description 1", new Status(true), category);
        Quizz quiz2 = new Quizz("Quiz 2", "Description 2", new Status(true), category);
        List<Quizz> quizzes = List.of(quiz1, quiz2);

        // Mock behavior of quizzRepository.findByStatusAndCategory
        when(quizzRepository.findByStatusAndCategory(any(Status.class), any(Category.class))).thenReturn(quizzes);

        // Perform GET request with category parameter
        mockMvc.perform(get("/api/quizzer/publishedquizz?category=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Quiz 1")))
                .andExpect(jsonPath("$[1].name", is("Quiz 2")));
    }

    // Test method for error case of category filtering when category does not exist
    @Test
    public void getPublishedQuizzByCategoryReturnsNotFoundWhenCategoryDoesNotExist() throws Exception {
        // Mock behavior of categoryRepository.findById
        when(categoryRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // Perform GET request with non-existing category parameter
        mockMvc.perform(get("/api/quizzer/publishedquizz?category=999"))
                .andExpect(status().isNotFound());
    }


}
