package sk.stuba.fiit.vava.controller;

import com.google.firebase.auth.FirebaseToken;
import com.google.gson.Gson;
import lombok.Setter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;
import sk.stuba.fiit.vava.auth.FirebaseVerifiedResult;
import sk.stuba.fiit.vava.model.LoginCredentials;
import sk.stuba.fiit.vava.repository.FirebaseUserRepository;
import sk.stuba.fiit.vava.util.Constants;
import sk.stuba.fiit.vava.util.Utilities;

/**
 * JSON API endpoint routes for authentication within application and user management
 */
@Controller
@RequestMapping(path = Constants.PATH_AUTH,
        consumes = Constants.CONTENT_TYPE_JSON, produces = Constants.CONTENT_TYPE_JSON)
public class AuthController {

    @Setter(onMethod=@__({@Autowired}))
    private FirebaseUserRepository firebaseUserRepository;

    @PostMapping(path = Constants.PATH_AUTH_LOGIN)
    public @ResponseBody DeferredResult<String> login(@RequestBody LoginCredentials loginCredentials) {

        Utilities.logPostRequest(AuthController.class.getName(), Constants.PATH_AUTH+Constants.PATH_AUTH_LOGIN,
                (new Gson()).toJson(loginCredentials));

        switch (loginCredentials.getProvider()) {
            case Constants.AUTH_PROVIDER_FIREBASE: {

                FirebaseVerifiedResult firebaseVerifiedResult = new FirebaseVerifiedResult() {
                    @Override
                    public void onSuccess(FirebaseToken decodedToken) {

                        // Identify user
                        String uid = decodedToken.getUid();

                        // Create user if not exists
                        if (firebaseUserRepository.createIfNotExist(uid)) {
                            Logger.getLogger(AuthController.class.getName()).info(
                                    "New user created for uid " + uid);
                        }

                        Utilities.logPostResponse(AuthController.class.getName(), Constants.PATH_AUTH_LOGIN,
                                (new Gson()).toJson("success"));
                        super.onSuccess(decodedToken);
                    }
                };
                // Construct response based on verification
                firebaseVerifiedResult.verifyToken(loginCredentials);
                return firebaseVerifiedResult;
            }
            // Here would be other authentication providers
            default:
                return null;
        }
    }
}
