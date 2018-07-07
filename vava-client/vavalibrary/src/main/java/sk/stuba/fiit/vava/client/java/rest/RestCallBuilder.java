package sk.stuba.fiit.vava.client.java.rest;

import java.util.Set;

import lombok.NonNull;
import retrofit2.Call;
import sk.stuba.fiit.vava.client.java.model.InputUrl;
import sk.stuba.fiit.vava.client.java.model.LoginCredentials;
import sk.stuba.fiit.vava.client.java.model.SearchPhrase;
import sk.stuba.fiit.vava.client.java.model.UrlResponse;

/**
 * REST call builder class. Builds request bodies and prepares whole call objects
 */
public class RestCallBuilder {

    private String token;

    /**
     * Constructor
     * @param token Valid token must be contained in every request
     */
    public RestCallBuilder(@NonNull String token) {
        this.token = token;
    }

    /**
     * Prepare login API call
     * @return Login call
     */
    public Call<Object> login() {
        LoginCredentials loginCredentials = CredentialsHelper.buildFirebaseLoginCredentials(token);

        RestService restService = RestHelper.buildRestService();
        return restService.login(loginCredentials);
    }

    /**
     * Prepare input urls API call
     * @return Input call
     */
    public Call<UrlResponse> inputUrl(@NonNull Set<String> urls) {
        LoginCredentials loginCredentials = CredentialsHelper.buildFirebaseLoginCredentials(token);
        InputUrl inputUrl = new InputUrl(loginCredentials, urls);

        RestService restService = RestHelper.buildRestService();
        return restService.inputUrl(inputUrl);
    }

    /**
     * Prepare photo search API call
     * @param textPhrase A phrase to be searched for
     * @return Search call
     */
    public Call<UrlResponse> searchPhrase(@NonNull String textPhrase) {
        LoginCredentials loginCredentials = CredentialsHelper.buildFirebaseLoginCredentials(token);
        SearchPhrase searchPhrase = new SearchPhrase(loginCredentials, textPhrase);

        RestService restService = RestHelper.buildRestService();
        return restService.searchPhrase(searchPhrase);
    }
}
