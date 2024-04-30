package fi.haagahelia.quizzer.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fi.haagahelia.quizzer.model.Question;
import fi.haagahelia.quizzer.model.Status;
import fi.haagahelia.quizzer.repository.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fi.haagahelia.quizzer.model.Quizz;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@Tag(name = "Question", description = "Operations for accessing and managing the questions")
public class QuestionRestController {
    @Autowired
    private QuizzRepository quizzRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @Operation(summary = "Get all questions", description = "Returns all questions")
    @GetMapping("/questionlist")
    public List<Question> getQuestionList() {
        // Return question list
        return (List<Question>) questionRepository.findAll();
    }
}
