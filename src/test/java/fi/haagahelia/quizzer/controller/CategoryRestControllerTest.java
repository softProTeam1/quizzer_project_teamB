package fi.haagahelia.quizzer.controller;


import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

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

}
