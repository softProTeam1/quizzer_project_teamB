package fi.haagahelia.quizzer.repository;

import fi.haagahelia.quizzer.model.Category;
import fi.haagahelia.quizzer.model.Quizz;
import fi.haagahelia.quizzer.model.Status;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface QuizzRepository extends CrudRepository<Quizz, Long> {
    List<Quizz> findByStatus(Status status);

    List<Quizz> findAll();


    List<Quizz> findByCategory(Category category);

    Optional<Quizz> findById(Long id);

    List<Quizz> findByStatusAndCategory(Status status, Category category);

}
