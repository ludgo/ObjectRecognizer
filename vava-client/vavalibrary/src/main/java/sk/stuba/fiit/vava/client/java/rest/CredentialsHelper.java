package sk.stuba.fiit.vava.client.java.rest;

import lombok.NonNull;
import sk.stuba.fiit.vava.client.java.model.LoginCredentials;
import sk.stuba.fiit.vava.client.java.util.Constants;

class CredentialsHelper {

    /**
     * Build Firebase credentials on token to be included in API requests
     * @return Firebase credentials object containing token
     */
    static LoginCredentials buildFirebaseLoginCredentials(@NonNull String token) {
        return new LoginCredentials(Constants.AUTH_PROVIDER_FIREBASE, token);
    }
}
