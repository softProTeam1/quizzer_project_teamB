package fi.haagahelia.quizzer.repository;


import fi.haagahelia.quizzer.model.Quizz;
import org.springframework.data.repository.CrudRepository;

import fi.haagahelia.quizzer.model.Review;

import java.util.List;

public interface ReviewRepository  extends CrudRepository<Review, Long> {
    Review findByreviewId(Long reviewId);

    List<Review> findByQuizz(Quizz quizz);

}
