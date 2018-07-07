package sk.stuba.fiit.vava.repository;

import org.springframework.transaction.annotation.Transactional;
import sk.stuba.fiit.vava.entity.FirebaseUser;
import sk.stuba.fiit.vava.entity.ImageUrl;

import java.util.List;
import java.util.Set;

/**
 * Custom methods definition for {@link ImageUrlRepository}
 */
public interface ImageUrlRepositoryCustom {

    @Transactional
    void createIfNotExist(Set<String> urls, FirebaseUser firebaseUser);

    void setUrlsUploaded(List<ImageUrl> imageUrls);
}
