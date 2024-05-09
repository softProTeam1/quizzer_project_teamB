package fi.haagahelia.quizzer.controler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.List;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import fi.haagahelia.quizzer.dto.QuizzDto;
import fi.haagahelia.quizzer.model.Quizz;
import fi.haagahelia.quizzer.model.Status;
import fi.haagahelia.quizzer.repository.QuizzRepository;
import fi.haagahelia.quizzer.repository.StatusRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class QuizzerRestControllerTest {
    @Autowired
    QuizzRepository quizzRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() throws Exception {
        // Make sure that the database is empty before each test
        quizzRepository.deleteAll();
        statusRepository.deleteAll();
    }

    @AfterEach
    void cleanTestDb() throws Exception {
        quizzRepository.deleteAll();
        statusRepository.deleteAll();
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
