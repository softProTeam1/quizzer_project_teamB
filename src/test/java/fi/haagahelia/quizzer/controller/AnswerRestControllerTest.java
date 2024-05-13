package fi.haagahelia.quizzer.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import fi.haagahelia.quizzer.dto.AnswerRequestDto;
import fi.haagahelia.quizzer.model.Answer;
import fi.haagahelia.quizzer.repository.AnswerRepository;
import fi.haagahelia.quizzer.repository.QuestionRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.List;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
public class AnswerRestControllerTest {
    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() throws Exception {
        // clear the db before starting the test
        answerRepository.deleteAll();
    }

    @Test
    public void createAnswerSavesValidAnswer() throws Exception {
        // Arrange
        Long questionId = (long) 1;
        AnswerRequestDto answer = new AnswerRequestDto("test answer", questionId);
        String requestBody = mapper.writeValueAsString(answer);

        // Act
        this.mockMvc.perform(post("/api/answer/add").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.answerText").value("test answer"))
                .andExpect(jsonPath("$.correctness").value(false));

        List<Answer> answers = (List<Answer>) answerRepository.findAll();
        assertEquals(1, answers.size());
        assertEquals("test answer", answers.get(0).getAnswerText());
        assertEquals(false, answers.get(0).getCorrectness());
        assertEquals(questionId, answers.get(0).getQuestion().getQuestionId());

    }

    @Test
    public void createAnswerDoesNotSaveInvalidAnswer() throws Exception {
        // Arrange
        Long questionId = (long) 1;
        AnswerRequestDto answer = new AnswerRequestDto("", questionId);
        String requestBody = mapper.writeValueAsString(answer);

        // Act
        this.mockMvc.perform(post("/api/answer/add").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                // Assert
                .andExpect(status().isBadRequest());

        List<Answer> answers = (List<Answer>) answerRepository.findAll();
        assertEquals(0, answers.size());

    }

    @Test
    public void createAnswerDoesNotSaveAnswerForNonExistingQuestion() throws Exception {
        // Arrange
        Long questionId = (long) 14072004;
        AnswerRequestDto answer = new AnswerRequestDto("", questionId);
        String requestBody = mapper.writeValueAsString(answer);

        // Act
        this.mockMvc.perform(post("/api/answer/add").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                // Assert
                .andExpect(status().isBadRequest());

        List<Answer> answers = (List<Answer>) answerRepository.findAll();
        assertEquals(0, answers.size());

    }

    // unpublished quizzes: 3 and 4
    // questionId for quiz3:7,8,9
    @Test
    public void createAnswerDoesNotSaveAnswerForNonPublishedQuiz() throws Exception {
        // Arrange
        Long questionId = (long) 8;
        AnswerRequestDto answer = new AnswerRequestDto("test answer", questionId);
        String requestBody = mapper.writeValueAsString(answer);

        // Act
        this.mockMvc.perform(post("/api/answer/add").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                // Assert
                .andExpect(status().isForbidden());

        List<Answer> answers = (List<Answer>) answerRepository.findAll();
        assertEquals(0, answers.size());

    }

    @Test
    public void getAllAnswerInAQuizTest() throws Exception {
        // Arrange
        Long questionId = (long) 1;
        AnswerRequestDto answerDTO1 = new AnswerRequestDto("test answer 1", questionId);
        AnswerRequestDto answerDTO2 = new AnswerRequestDto("test answer 2", questionId);
        answerDTO2.setCorrectness(true);
        Answer answer1 = new Answer(answerDTO1.getAnswerText(), answerDTO1.getCorrectness());
        answer1.setQuestion(questionRepository.findById(questionId).orElseThrow());
        Answer answer2 = new Answer(answerDTO2.getAnswerText(), answerDTO2.getCorrectness());
        answer2.setQuestion(questionRepository.findById(questionId).orElseThrow());
        answerRepository.saveAll(List.of(answer1, answer2));

        // Act
        this.mockMvc.perform(get("/api/answer/quiz/" + answer1.getQuestion().getQuizz().getQuizzId()))
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].answerText").value("test answer 1"))
                .andExpect(jsonPath("$.[0].answerId").value(answer1.getAnswerId()))
                .andExpect(jsonPath("$.[0].correctness").value(false))
                .andExpect(jsonPath("$.[1].answerText").value("test answer 2"))
                .andExpect(jsonPath("$.[1].correctness").value(true))
                .andExpect(jsonPath("$.[1].answerId").value(answer2.getAnswerId()));

    }

}
