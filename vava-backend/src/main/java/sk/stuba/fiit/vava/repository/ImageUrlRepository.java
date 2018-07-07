package sk.stuba.fiit.vava.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sk.stuba.fiit.vava.entity.FirebaseUser;
import sk.stuba.fiit.vava.entity.ImageUrl;

import java.util.List;

/**
 * JPA repository to perform database operations on {@link ImageUrl},
 * CRUD methods available by default
 */
@Repository
public interface ImageUrlRepository extends CrudRepository<ImageUrl, Long>,
        ImageUrlRepositoryCustom {

    List<ImageUrl> findByIsUploadedAndFirebaseUser(boolean isUploaded, FirebaseUser firebaseUser);

    ImageUrl findByUrlAndFirebaseUser(String url, FirebaseUser firebaseUser);
}
