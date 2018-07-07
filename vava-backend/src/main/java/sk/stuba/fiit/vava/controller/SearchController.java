package sk.stuba.fiit.vava.controller;

import com.google.firebase.auth.FirebaseToken;
import com.google.gson.Gson;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;
import sk.stuba.fiit.vava.auth.FirebaseVerifiedResult;
import sk.stuba.fiit.vava.model.LoginCredentials;
import sk.stuba.fiit.vava.model.SearchPhrase;
import sk.stuba.fiit.vava.model.UrlResponse;
import sk.stuba.fiit.vava.repository.FirebaseUserRepository;
import sk.stuba.fiit.vava.service.ClarifaiService;
import sk.stuba.fiit.vava.util.Constants;
import sk.stuba.fiit.vava.util.Utilities;

import java.util.Set;

/**
 * JSON API endpoint routes for search queries
 */
@Controller
@RequestMapping(path = Constants.PATH_SEARCH, consumes = Constants.CONTENT_TYPE_JSON, produces = Constants.CONTENT_TYPE_JSON)
public class SearchController {

    @Setter(onMethod=@__({@Autowired}))
    private FirebaseUserRepository firebaseUserRepository;

    @Setter(onMethod=@__({@Autowired}))
    private ClarifaiService clarifaiService;

    @PostMapping(path = Constants.PATH_SEARCH_PHRASE)
    public @ResponseBody DeferredResult<String> phrase(@RequestBody SearchPhrase searchPhrase) {

        Utilities.logPostRequest(SearchController.class.getName(), Constants.PATH_SEARCH+Constants.PATH_SEARCH_PHRASE,
                (new Gson()).toJson(searchPhrase));

        LoginCredentials loginCredentials = searchPhrase.getLoginCredentials();
        switch (loginCredentials.getProvider()) {
            case Constants.AUTH_PROVIDER_FIREBASE: {

                FirebaseVerifiedResult firebaseVerifiedResult = new FirebaseVerifiedResult() {
                    @Override
                    public void onSuccess(FirebaseToken decodedToken) {

                        // Identify user
                        String uid = decodedToken.getUid();
                        // Must exist in server database to continue
                        if (firebaseUserRepository.findByUid(uid) == null) {
                            setResult(null);
                            return;
                        }

                        // Validate search phrase
                        String phrase = searchPhrase.getPhrase();
                        if (phrase == null || phrase.length() < Constants.SEARCH_PHRASE_LENGTH_MIN) {
                            setResult(null);
                            return;
                        }
                        phrase = phrase.toLowerCase();

                        // Search Clarifai model
                        Set<String> urls = clarifaiService.searchPhotos(phrase, uid);

                        // Respond with search results
                        UrlResponse urlResponse = new UrlResponse(urls);
                        Utilities.logPostResponse(SearchController.class.getName(), Constants.PATH_SEARCH_PHRASE,
                                (new Gson()).toJson(urlResponse));
                        setResult((new Gson()).toJson(urlResponse));
                    }
                };
                // Construct response based on verification
                firebaseVerifiedResult.verifyToken(searchPhrase.getLoginCredentials());
                return firebaseVerifiedResult;
            }
            // Here would be other authentication providers
            default:
                return null;
        }
    }
}
