package sk.stuba.fiit.vava.controller;

import clarifai2.dto.input.ClarifaiInput;
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
import sk.stuba.fiit.vava.clarifai.ClarifaiInputCallback;
import sk.stuba.fiit.vava.entity.FirebaseUser;
import sk.stuba.fiit.vava.entity.ImageUrl;
import sk.stuba.fiit.vava.model.InputUrl;
import sk.stuba.fiit.vava.model.LoginCredentials;
import sk.stuba.fiit.vava.model.UrlResponse;
import sk.stuba.fiit.vava.repository.FirebaseUserRepository;
import sk.stuba.fiit.vava.repository.ImageUrlRepository;
import sk.stuba.fiit.vava.service.ClarifaiService;
import sk.stuba.fiit.vava.util.Constants;
import sk.stuba.fiit.vava.util.Utilities;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * JSON API endpoint routes for image model input
 */
@Controller
@RequestMapping(path = Constants.PATH_INPUT,
        consumes = Constants.CONTENT_TYPE_JSON, produces = Constants.CONTENT_TYPE_JSON)
public class InputController {

    @Setter(onMethod=@__({@Autowired}))
    private FirebaseUserRepository firebaseUserRepository;
    @Setter(onMethod=@__({@Autowired}))
    private ImageUrlRepository imageUrlRepository;

    @Setter(onMethod=@__({@Autowired}))
    private ClarifaiService clarifaiService;

    @PostMapping(path = Constants.PATH_INPUT_URL)
    public @ResponseBody DeferredResult<String> url(@RequestBody InputUrl inputUrl) {

        Utilities.logPostRequest(InputController.class.getName(), Constants.PATH_INPUT+Constants.PATH_INPUT_URL,
                (new Gson()).toJson(inputUrl));

        LoginCredentials loginCredentials = inputUrl.getLoginCredentials();
        switch (loginCredentials.getProvider()) {
            case Constants.AUTH_PROVIDER_FIREBASE: {

                FirebaseVerifiedResult firebaseVerifiedResult = new FirebaseVerifiedResult() {
                    @Override
                    public void onSuccess(FirebaseToken decodedToken) {

                        // Identify user
                        String uid = decodedToken.getUid();
                        FirebaseUser firebaseUser = firebaseUserRepository.findByUid(uid);
                        // Must exist in server database to continue
                        if (firebaseUser == null) setResult(null);

                        // Keep urls in database
                        imageUrlRepository.createIfNotExist(inputUrl.getUrls(), firebaseUser);

                        // Add to Clarifai model
                        List<ImageUrl> imageUrls = imageUrlRepository.findByIsUploadedAndFirebaseUser(
                                false, firebaseUser);
                        if (!imageUrls.isEmpty()) {
                            Set<String> urls = new HashSet<>();
                            for (ImageUrl imageUrl : imageUrls) {
                                urls.add(imageUrl.getUrl());
                            }

                            clarifaiService.addPhotos(urls, uid, new ClarifaiInputCallback() {
                                @Override
                                public void onClarifaiResponseSuccess(List<ClarifaiInput> clarifaiInputs) {
                                    super.onClarifaiResponseSuccess(clarifaiInputs);
                                    // Remember these urls are part of Clarifai model from now
                                    imageUrlRepository.setUrlsUploaded(imageUrls);
                                }

                                @Override
                                public void onClarifaiResponseUnsuccessful(int errorCode) {
                                    super.onClarifaiResponseUnsuccessful(errorCode);
                                    // Uploading these urls produces no network related error
                                    imageUrlRepository.delete(imageUrls);
                                }
                            });
                        }

                        // Respond with same urls so that client knows they are server managed from now
                        UrlResponse urlResponse = new UrlResponse(inputUrl.getUrls());
                        Utilities.logPostResponse(InputController.class.getName(), Constants.PATH_INPUT_URL,
                                (new Gson()).toJson(urlResponse));
                        setResult((new Gson()).toJson(urlResponse));
                    }
                };
                // Construct response based on verification
                firebaseVerifiedResult.verifyToken(inputUrl.getLoginCredentials());
                return firebaseVerifiedResult;
            }
            // Here would be other authentication providers
            default:
                return null;
        }
    }
}
