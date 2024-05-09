package fi.haagahelia.quizzer.controler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.List;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import fi.haagahelia.quizzer.model.Quizz;
import fi.haagahelia.quizzer.repository.QuizzRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class QuizzerRestControllerTest {
    @Autowired
    QuizzRepository quizzRepository;
    @Autowired
    private MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() throws Exception {
        // Make sure that the database is empty before each test
        quizzRepository.deleteAll();
    }

    // The test methods go here
}
