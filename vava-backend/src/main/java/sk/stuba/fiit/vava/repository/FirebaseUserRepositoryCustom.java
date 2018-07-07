package sk.stuba.fiit.vava.repository;

import org.springframework.transaction.annotation.Transactional;

/**
 * Custom methods definition for {@link FirebaseUserRepository}
 */
public interface FirebaseUserRepositoryCustom {

    @Transactional
    boolean createIfNotExist(String uid);
}
