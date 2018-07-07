package sk.stuba.fiit.vava.repository;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import sk.stuba.fiit.vava.entity.FirebaseUser;
import sk.stuba.fiit.vava.entity.ImageUrl;

import java.util.List;
import java.util.Set;

/**
 * Custom methods implementation for {@link ImageUrlRepository}
 */
public class ImageUrlRepositoryImpl implements ImageUrlRepositoryCustom {

    @Setter(onMethod=@__({@Autowired}))
    private ImageUrlRepository imageUrlRepository;

    @Transactional
    public void createIfNotExist(Set<String> urls, FirebaseUser firebaseUser) {
        // Assign url to user if not yet
        for (String url : urls) {
            if (imageUrlRepository.findByUrlAndFirebaseUser(url, firebaseUser) == null) {
                imageUrlRepository.save(new ImageUrl(url, firebaseUser));
            }
        }
    }

    public void setUrlsUploaded(List<ImageUrl> imageUrls) {
        for (ImageUrl imageUrl : imageUrls) {
            imageUrl.setUploaded(true);
        }
        imageUrlRepository.save(imageUrls);
    }
}
