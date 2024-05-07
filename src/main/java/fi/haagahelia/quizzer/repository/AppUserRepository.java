package fi.haagahelia.quizzer.repository;

import fi.haagahelia.quizzer.model.AppUser;
import org.springframework.data.repository.CrudRepository;

public interface AppUserRepository extends CrudRepository<AppUser, Long> {
    AppUser findByUsername(String username);
    
}

