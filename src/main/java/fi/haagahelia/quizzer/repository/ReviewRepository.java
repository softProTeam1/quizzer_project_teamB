package fi.haagahelia.quizzer.repository;


import org.springframework.data.repository.CrudRepository;

import fi.haagahelia.quizzer.model.Review;

public interface ReviewRepository  extends CrudRepository<Review, Long> {
    Review findByreviewId(Long reviewId);

}
