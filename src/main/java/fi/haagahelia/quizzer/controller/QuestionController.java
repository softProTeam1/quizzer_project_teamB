package fi.haagahelia.quizzer.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
// import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;

import fi.haagahelia.quizzer.model.Question;
import fi.haagahelia.quizzer.model.Quizz;
import fi.haagahelia.quizzer.repository.DifficultyRepository;
import fi.haagahelia.quizzer.repository.QuestionRepository;
import fi.haagahelia.quizzer.repository.QuizzRepository;
import jakarta.persistence.EntityNotFoundException;

@Controller
public class QuestionController {

    @Autowired
    private QuizzRepository quizzRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private DifficultyRepository difficultyRepository;


    
    @GetMapping("/questionlist/{quizzId}")
    public String questionList(@PathVariable("quizzId") Long quizzId, Model model) {
        Quizz quizz = quizzRepository.findById(quizzId)
                .orElseThrow(() -> new EntityNotFoundException("project not found"));
        List<Question> questions = quizz.getQuestion();
        model.addAttribute("quizzName", quizz.getName().toUpperCase());
        model.addAttribute("questions", questions);
        return "questionlist";
    }
    
    @GetMapping("/easyquestionlist/{quizzId}")
    public String easyquestionList(@PathVariable("quizzId") Long quizzId, Model model) {
        return getQuestionListView(quizzId, model, 1L);
    }
    
    @GetMapping("/normalquestionlist/{quizzId}")
    public String normalquestionList(@PathVariable("quizzId") Long quizzId, Model model) {
        return getQuestionListView(quizzId, model, 2L);
    }
    
    @GetMapping("/hardquestionlist/{quizzId}")
    public String hardquestionList(@PathVariable("quizzId") Long quizzId, Model model) {
        return getQuestionListView(quizzId, model, 3L);
    }
  
    
    private String getQuestionListView(Long quizzId, Model model, Long difficultyId) {
        Quizz quizz = quizzRepository.findById(quizzId)
                .orElseThrow(() -> new EntityNotFoundException("Quizz not found"));
    
        List <Question> questions = quizz.getQuestion();

       
            List <Question> filteredQuestions = new ArrayList<>();
            for (Question question : questions) {
                if (question.getDifficulty().getDifficultyId().equals(difficultyId)) {
                    filteredQuestions.add(question);
                }
            }
            questions = filteredQuestions;
        

        model.addAttribute("quizzName", quizz.getName().toUpperCase());
        model.addAttribute("questions", questions);
        model.addAttribute("quizzId", quizzId);
        return "questionlist";
    }


    

    // add question
    @GetMapping("/addQuestion/{quizzId}")
    public String addQuestion(@PathVariable("quizzId") Long quizzId, Model model) {
        model.addAttribute("question", new Question());
        model.addAttribute("difficulties", difficultyRepository.findAll());
        model.addAttribute("quizzId", quizzId);
        return "addQuestion";
    }

    // save question
    @PostMapping("/saveQuestion/{quizzId}")
    public String saveQuestion(@PathVariable("quizzId") Long quizzId, Question question) {
        Quizz quizz = quizzRepository.findById(quizzId)
                .orElseThrow(() -> new EntityNotFoundException("Quiz not found"));
        question.setQuizz(quizz);
        questionRepository.save(question);
        return "redirect:/questionlist/" + quizzId;
    }

    // edit question
    @GetMapping(value = "/editquestion/{questionId}/{quizzId}")
    public String editQuizForm(@PathVariable("questionId") Long questionId, @PathVariable("quizzId") Long quizzId,
            Model model) {
        model.addAttribute("question", questionRepository.findById(questionId));
        model.addAttribute("difficulties", difficultyRepository.findAll());
        model.addAttribute("quizzes", quizzRepository.findAll());
        model.addAttribute("quizzId", quizzId);
        return "editquestion.html";
    }

    // update editted question
    @PostMapping("/updatequestion/{quizzId}")
    public String save(@PathVariable("quizzId") Long quizzId, Question question) {
        Quizz quizz = quizzRepository.findById(quizzId)
                .orElseThrow(() -> new EntityNotFoundException("Quiz not found"));
        question.setQuizz(quizz);
        questionRepository.save(question);
        return "redirect:/questionlist/" + quizzId;
    }

    // deleting question
    @GetMapping("/deletequestion/{questionId}/{quizzId}")
    public String deletequestion(@PathVariable("questionId") Long questionId, @PathVariable("quizzId") Long quizzId) {
        questionRepository.deleteById(questionId);
        return "redirect:/questionlist/" + quizzId;
    }

}