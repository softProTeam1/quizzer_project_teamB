package fi.haagahelia.quizzer.repository;

import fi.haagahelia.quizzer.model.Quizz;
import fi.haagahelia.quizzer.model.Status;
import org.springframework.data.repository.CrudRepository;

import fi.haagahelia.quizzer.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends CrudRepository<Category, Long> {
    List<Category> findByName(String name);

    List<Category> findAllByOrderByNameAsc();
}
