package sk.stuba.fiit.vava.repository;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import sk.stuba.fiit.vava.entity.FirebaseUser;

/**
 * Custom methods implementation for {@link FirebaseUserRepository}
 */
public class FirebaseUserRepositoryImpl implements FirebaseUserRepositoryCustom {

    @Setter(onMethod=@__({@Autowired}))
    private FirebaseUserRepository firebaseUserRepository;

    @Transactional
    public boolean createIfNotExist(String uid) {
        if (firebaseUserRepository.findByUid(uid) == null) {
            firebaseUserRepository.save(new FirebaseUser(uid));
            return true;
        }
        return false;
    }
}
