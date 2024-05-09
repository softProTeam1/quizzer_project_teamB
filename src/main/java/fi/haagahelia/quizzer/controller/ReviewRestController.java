package fi.haagahelia.quizzer.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import fi.haagahelia.quizzer.model.Quizz;
import fi.haagahelia.quizzer.model.Review;
import fi.haagahelia.quizzer.repository.QuizzRepository;
import fi.haagahelia.quizzer.repository.ReviewRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/review")
@CrossOrigin(origins = "*")
public class ReviewRestController {

    @Autowired
    private QuizzRepository quizzRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    

    @PostMapping("/add")
    public Review createReview(@Valid @RequestBody Review review, BindingResult bindingResult) {
        // Invalid request body
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid request body. Please provide valid review data.");
        }

        // Retrieve the quiz by ID
        Quizz quiz = quizzRepository.findById(review.getQuizz().getQuizzId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz with the provided ID not found."));

        // Check if the quiz is published
        if (!quiz.getStatus().getStatus()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quiz with the provided ID is not published.");
        }

        // Set the quiz for the review
        review.setQuizz(quiz);

        // Save the review
        return reviewRepository.save(review);
    }

  // used only for testing (now can be deleted)
  @GetMapping("/all")
  public Iterable<Review> getAllReviews() {
      return reviewRepository.findAll();
  }

    // used only for testing (now can be deleted)
    @DeleteMapping("/delete/all")
    public void deleteAllReviews() {
        try {
            reviewRepository.deleteAll();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete reviews.");
        }
    }
}
