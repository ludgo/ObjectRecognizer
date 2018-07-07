package sk.stuba.fiit.vava.auth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.apache.log4j.Logger;
import org.springframework.web.context.request.async.DeferredResult;
import sk.stuba.fiit.vava.model.LoginCredentials;
import sk.stuba.fiit.vava.util.Constants;

import javax.validation.constraints.NotNull;

/**
 * Firebase token server verification and HTTP response definition
 */
public class FirebaseVerifiedResult extends DeferredResult<String> implements TokenVerifier {

    /**
     * Verify token validity asynchronously
     * Note: Firebase Admin SDK implementation caches token validity by implementation
     * which makes using this method repeatedly checking locally saved constant only
     * @param loginCredentials A model object containing Firebase token
     */
    public void verifyToken(@NotNull LoginCredentials loginCredentials) {
        FirebaseAuth.getInstance()
                .verifyIdToken(loginCredentials.getToken())
                .addOnSuccessListener(this)
                .addOnFailureListener(this);
    }

    /**
     * Firebase token verification succeeded
     * @param decodedToken A decoded token containing Firebase user basic info
     */
    @Override
    public void onSuccess(FirebaseToken decodedToken) {
        // Define API verification success response here
        this.setResult(Constants.RESPONSE_BODY_DEFAULT_SUCCESS);
    }

    /**
     * Firebase token verification failed
     * @param e Exception thrown on token verification
     */
    @Override
    public void onFailure(@NotNull Exception e) {
        // Define API verification failure response here
        Logger.getLogger(FirebaseVerifiedResult.class.getName()).warn(
                "Not a valid Firebase token issued for verification", e);
        this.setResult(Constants.RESPONSE_BODY_DEFAULT_ERROR);
    }
}
