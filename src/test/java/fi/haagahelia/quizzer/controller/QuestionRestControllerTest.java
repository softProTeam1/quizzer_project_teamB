package fi.haagahelia.quizzer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fi.haagahelia.quizzer.model.*;
import fi.haagahelia.quizzer.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class QuestionRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuizzRepository quizzRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private DifficultyRepository difficultyRepository;

    ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() throws Exception {
        // clear the db before starting the test
        questionRepository.deleteAll();
    }

    @Test
    public void getQuestionsByQuizIdReturnsEmptyListWhenQuizDoesNotHaveQuestions() throws Exception {
        // Act
        this.mockMvc.perform(get("/api/questions/{quizzId}", 4))
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void getQuestionsByQuizIdReturnsListOfQuestionsWhenQuizHasQuestions() throws Exception {
        // Arrange
        Difficulty difficulty1 = new Difficulty("Easy");
        Difficulty difficulty2 = new Difficulty("Normal");
        Difficulty difficulty3 = new Difficulty("Hard");
        difficultyRepository.saveAll(List.of(difficulty1, difficulty2, difficulty3));

        Status publishedStatus = new Status(true);
        statusRepository.save(publishedStatus);

        Status nonPublishedStatus = new Status(false);
        statusRepository.save(nonPublishedStatus);

        Category category1 = new Category("Test category 1", "Category description test");
        categoryRepository.save(category1);

        Quizz quizz5 = new Quizz("Quiz 5", "Description 1", publishedStatus, category1 );
        quizzRepository.save(quizz5);

        Question question1 = new Question("Question 1", "Answer 1", difficulty1, quizz5);
        Question question2 = new Question("Question 2", "Answer 2", difficulty2, quizz5);
        Question question3 = new Question("Question 3", "Answer 3", difficulty3, quizz5);
        questionRepository.saveAll(List.of(question1, question2, question3));

        // Act
        this.mockMvc.perform(get("/api/questions/{quizzId}", quizz5.getQuizzId()))
                // Assert
                .andExpect(status().isOk())

                .andExpect(jsonPath("$", hasSize(3))) // Expecting 3 questions

                .andExpect(jsonPath("$[0].questionText").value("Question 1")) // Expecting first question text
                .andExpect(jsonPath("$[0].correctAnswer").value("Answer 1")) // Expecting first question correct answer

                .andExpect(jsonPath("$[1].questionText").value("Question 2")) // Expecting second question text
                .andExpect(jsonPath("$[1].correctAnswer").value("Answer 2")) // Expecting second question correct answer

                .andExpect(jsonPath("$[2].questionText").value("Question 3")) // Expecting third question text
                .andExpect(jsonPath("$[2].correctAnswer").value("Answer 3")); // Expecting third question correct answer

                assertEquals(difficulty1,(question1.getDifficulty())); // Expecting first question difficulty
                assertEquals(difficulty2,(question2.getDifficulty())); // Expecting second question difficulty
                assertEquals(difficulty3,(question3.getDifficulty())); // Expecting third question difficulty
    }

    @Test
    public void getQuestionsByQuizIdReturnsErrorWhenQuestionDoesNotExist() throws Exception {
        // Arrange
        Long nonExistentQuizId = 9999L; // This ID should not exist in your database

        // Act & Assert
        this.mockMvc.perform(get("/api/questions/{quizzId}", nonExistentQuizId))
                .andExpect(status().isNotFound()); // API returns a 404 status when no questions are found
    }

    @Test
    public void getQuestionsByQuizIdReturnsErrorWhenQuizIsNotPublished() throws Exception {

        // Arrange
        Status nonPublishedStatus = new Status(false);
        statusRepository.save(nonPublishedStatus);

        Category category1 = new Category("Test category 1", "Category description test");
        categoryRepository.save(category1);

        Quizz nonPublishedQuiz = new Quizz("Non-Published Quiz", "Description", nonPublishedStatus, category1 );
        quizzRepository.save(nonPublishedQuiz);

        // Act & Assert
        this.mockMvc.perform(get("/api/questions/{quizzId}", nonPublishedQuiz.getQuizzId()))
                .andExpect(status().isForbidden()); // API returns a 403 status when the quiz is not published
    }

}
