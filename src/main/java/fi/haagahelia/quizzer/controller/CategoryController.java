package fi.haagahelia.quizzer.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import fi.haagahelia.quizzer.model.Category;
import fi.haagahelia.quizzer.model.Quizz;
import fi.haagahelia.quizzer.repository.CategoryRepository;
import fi.haagahelia.quizzer.repository.QuizzRepository;

@Controller
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;
    private QuizzRepository quizzRepository;

    // add category - Hong
    @GetMapping(value = "/addCategory")
    public String addCategory(Model model) {
        model.addAttribute("category", new Category());
        return "addCategory";
    }

    // save category - Hong
    @PostMapping(value = "/saveCategory")
    public String saveCategory(Category category) {
        categoryRepository.save(category);
        return "redirect:/categorylist";
    }

    // show all category
    @GetMapping("/categorylist")
    public String showCat(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "categorylist";
    }

    @GetMapping(value = "/editcategory/{categoryId}")
    public String editCategoryForm(@PathVariable("categoryId") Long categoryId, Model model) {
        model.addAttribute("category", categoryRepository.findById(categoryId));
        return "editcategory.html";
    }

    // delete category
    @GetMapping("/deletecategory/{categoryId}")
    public String deleteCategory(@PathVariable("categoryId") Long categoryId, Model model) {
        categoryRepository.deleteById(categoryId);
        return "redirect:../categorylist";
    }

    // filter quizz by category
    @GetMapping("/filterQuizzesByCategory/{categoryId}")
    public String filterQuizzesByCategory(@PathVariable("categoryId") Long categoryId, Model model) {
        Category category = categoryRepository.findOneByCategoryId(categoryId);
        List<Quizz> quizzes = quizzRepository.findByCategory(category);
        model.addAttribute("quizzlist", quizzes);
        return "quizzlist";
    }
}
