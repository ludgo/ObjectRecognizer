package sk.stuba.fiit.vava.client.java.rest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import sk.stuba.fiit.vava.client.java.model.InputUrl;
import sk.stuba.fiit.vava.client.java.model.LoginCredentials;
import sk.stuba.fiit.vava.client.java.model.SearchPhrase;
import sk.stuba.fiit.vava.client.java.model.UrlResponse;
import sk.stuba.fiit.vava.client.java.util.Constants;

/**
 * REST JSON API routes request-response definition
 */
interface RestService {

    @POST(Constants.PATH_AUTH_LOGIN)
    Call<Object> login(@Body LoginCredentials loginCredentials);

    @POST(Constants.PATH_INPUT_URL)
    Call<UrlResponse> inputUrl(@Body InputUrl inputUrl);

    @POST(Constants.PATH_SEARCH_PHRASE)
    Call<UrlResponse> searchPhrase(@Body SearchPhrase searchPhrase);
}
