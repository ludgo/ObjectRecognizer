package sk.stuba.fiit.vava.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sk.stuba.fiit.vava.entity.FirebaseUser;

/**
 * JPA repository to perform database operations on {@link FirebaseUser},
 * CRUD methods available by default
 */
@Repository
public interface FirebaseUserRepository extends CrudRepository<FirebaseUser, Long>,
        FirebaseUserRepositoryCustom {

    FirebaseUser findByUid(String uid);
}
