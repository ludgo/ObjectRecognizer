package sk.stuba.fiit.vava.auth;

import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.tasks.OnFailureListener;
import com.google.firebase.tasks.OnSuccessListener;
import sk.stuba.fiit.vava.model.LoginCredentials;

import javax.validation.constraints.NotNull;

/**
 * Server verification of user token
 */
public interface TokenVerifier extends OnSuccessListener<FirebaseToken>, OnFailureListener {

    void verifyToken(@NotNull LoginCredentials loginCredentials);
}
