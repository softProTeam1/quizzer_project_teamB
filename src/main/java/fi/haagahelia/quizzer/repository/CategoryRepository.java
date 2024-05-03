package fi.haagahelia.quizzer.repository;

import org.springframework.data.repository.CrudRepository;

import fi.haagahelia.quizzer.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends CrudRepository<Category, Long> {
    List<Category> findByName(String name);

    Optional<Category> findById(Long categoryId);
    List<Category> findAllByOrderByNameAsc();

<<<<<<< HEAD
    Category findOneByCategoryId(Long categoryId);

}
=======
}
>>>>>>> b63af94fcac96a7597a597ee4a361765fc2c4418
