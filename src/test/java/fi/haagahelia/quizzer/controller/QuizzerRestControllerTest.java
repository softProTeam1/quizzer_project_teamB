package fi.haagahelia.quizzer.controller;

import fi.haagahelia.quizzer.model.Category;
import fi.haagahelia.quizzer.model.Quizz;
import fi.haagahelia.quizzer.model.Status;
import fi.haagahelia.quizzer.repository.QuizzRepository;
import fi.haagahelia.quizzer.repository.StatusRepository;
import fi.haagahelia.quizzer.repository.CategoryRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class QuizzerRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private QuizzRepository quizzRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() throws Exception {
        quizzRepository.deleteAll();
        statusRepository.deleteAll();
        categoryRepository.deleteAll();

    }

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

        Status publishedStatus = new Status(true);
        statusRepository.save(publishedStatus);

        Status nonPublishedStatus = new Status(false);
        statusRepository.save(nonPublishedStatus);

        Category category = new Category("Test Category Name", "Test Category Description");
        categoryRepository.save(category);

        Quizz publishedQuizz1 = new Quizz("Published Quizz 1", "Description", publishedStatus, category);
        Quizz publishedQuizz2 = new Quizz("Published Quizz 2", "Description", publishedStatus, category);
        Quizz nonPublishedQuizz = new Quizz("Non-Published Quizz", "Description", nonPublishedStatus, category);
        quizzRepository.saveAll(List.of(publishedQuizz1, publishedQuizz2, nonPublishedQuizz));

        // Act

        this.mockMvc.perform(get("/api/quizzer"))
                // Assert
                .andExpect(status().isOk())

                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name").value("Published Quizz 1"))
                .andExpect(jsonPath("$[0].quizzId").value(publishedQuizz1.getQuizzId()))

                .andExpect(jsonPath("$[1].name").value("Published Quizz 2"))
                .andExpect(jsonPath("$[1].quizzId").value(publishedQuizz2.getQuizzId()))

                .andExpect(jsonPath("$[2].name", not(hasItem("Non-Published Quizz"))))
                .andExpect(jsonPath("$[2].quizzId").value(nonPublishedQuizz.getQuizzId()));

    }

    @Test
    public void getQuizzByIdReturnsQuizWhenIdExists() throws Exception {
        // Arrange
        Category category = new Category("Test Category Name", "Test Category Description");
        categoryRepository.save(category);

        Status status = new Status(true);
        statusRepository.save(status);

        Quizz quiz = new Quizz("Quiz 1", "Description 1", status, category);
        quizzRepository.save(quiz);

        // Act
        mockMvc.perform(get("/api/quizzer/quizz/" + quiz.getQuizzId()))
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Quiz 1")))
                .andExpect(jsonPath("$.description", is("Description 1")));
    }

    @Test
    public void getQuizzByIdReturnsNotFoundWhenIdDoesNotExist() throws Exception {

        // Act
        mockMvc.perform(get("/api/quizzer/quizz/1"))
                // Assert
                .andExpect(status().isNotFound());
    }

    @Test
    public void getQuizzByCategoryReturnsQuizWhenCategoryExists() throws Exception {
        // Arrange
        Category category = new Category("Test Category Name", "Test Category Description");
        categoryRepository.save(category);

        Status status = new Status(true);
        statusRepository.save(status);

        Quizz quiz = new Quizz("Quiz 1", "Description 1", status, category);
        quizzRepository.save(quiz);

        // Act
        mockMvc.perform(get("/api/quizzer/publishedquizz?category=" + category.getCategoryId()))
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Quiz 1")))
                .andExpect(jsonPath("$[0].description", is("Description 1")));
    }

    @Test
    public void getQuizzByCategoryReturnsNotFoundWhenCategoryDoesNotExist() throws Exception {

        // Act
        mockMvc.perform(get("/api/quizzer/publishedquizz?category=1"))
                // Assert
                .andExpect(status().isNotFound());
    }

     // The test methods go here
     @Test
     public void getQuizByIdReturnsPublishedQuizWhenQuizExists() throws Exception {
         // Arrange
         Status status = new Status();
         status.setStatus(true);
         statusRepository.save(status);
 
         Quizz quizz = new Quizz();
 
         quizz.setName("Sample Quiz");
         quizz.setDescription("A simple quiz for testing");
         quizz.setStatus(status);
         quizzRepository.save(quizz);
 
         this.mockMvc.perform(get("/api/quizzer/quizz/{quizzId}", quizz.getQuizzId()))
                 .andExpect(status().isOk())
                 .andExpect(jsonPath("$.quizzId").value(quizz.getQuizzId()))
                 .andExpect(jsonPath("$.name").value("Sample Quiz"))
                 .andExpect(jsonPath("$.description").value("A simple quiz for testing"));
     }
 
     @Test
     public void getQuizByIdReturnsErrorWhenQuizDoesNotExist() throws Exception {
         // Act and Assert
         this.mockMvc.perform(get("/api/quizzer/quizz/{quizzId}", 999))
                 .andExpect(status().isNotFound());
     }
 
     @Test
     public void getQuizByIdReturnsErrorWhenQuizIsNotPublished() throws Exception {
         // Arrange
         Status statusFalse = new Status();
         statusFalse.setStatus(false);
         statusRepository.save(statusFalse);
 
         Quizz quizz = new Quizz();
 
         quizz.setName("Sample Quiz");
         quizz.setDescription("A simple quiz for testing");
         quizz.setStatus(statusFalse);
         quizzRepository.save(quizz);
 
         this.mockMvc.perform(get("/api/quizzer/quizz/{quizzId}", quizz.getQuizzId()))
                 .andExpect(status().isBadRequest()); // Expect HTTP 400
     }
 
}
