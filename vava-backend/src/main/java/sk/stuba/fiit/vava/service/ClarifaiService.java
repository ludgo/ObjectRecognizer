package sk.stuba.fiit.vava.service;

import clarifai2.api.ClarifaiClient;
import clarifai2.api.ClarifaiResponse;
import clarifai2.api.request.ClarifaiRequest;
import clarifai2.api.request.input.AddInputsRequest;
import clarifai2.api.request.input.SearchClause;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.input.SearchHit;
import clarifai2.dto.input.image.ClarifaiImage;
import clarifai2.dto.input.image.ClarifaiURLImage;
import clarifai2.dto.prediction.Concept;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.Setter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.stuba.fiit.vava.clarifai.ClarifaiHelper;
import sk.stuba.fiit.vava.clarifai.ClarifaiInputCallback;
import sk.stuba.fiit.vava.component.ClarifaiProperties;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Service as the access tool to application's Clarifai model
 */
@Service
public class ClarifaiService {

    // Clarifai user metadata key
    private static final String METADATA_KEY_UID = "uid";

    private ClarifaiClient clarifaiClient;

    @Setter(onMethod=@__({@Autowired}))
    private ClarifaiProperties clarifaiProperties;

    @PostConstruct
    private void initialize() {
        clarifaiClient = ClarifaiHelper.buildClarifaiClient(
                clarifaiProperties.getId(), clarifaiProperties.getSecret());
    }

    /**
     * Include image urls to Clarifai model database asynchronously
     * @param urls Urls of images to be passed for Clarifai automatic object recognition
     * @param uid User metadata value
     * @param clarifaiInputCallback Callback waiting for asynchronous call response
     */
    public synchronized void addPhotos(
            @NotNull @Size(min=1) Set<String> urls, @NotNull String uid,
            @NotNull ClarifaiInputCallback clarifaiInputCallback) {

        // Make each image model entry user specific
        final JsonObject metadata = ClarifaiHelper.buildUserMetadata(uid);

        // Prepare input image list
        List<ClarifaiInput> clarifaiInputList = new ArrayList<>();
        for (String url : urls) {
            clarifaiInputList.add(ClarifaiInput.forImage(ClarifaiImage.of(url))
                    .withMetadata(metadata));
        }

        // Build input request
        AddInputsRequest addInputsRequest = clarifaiClient.addInputs()
                .plus(clarifaiInputList);

        Logger.getLogger(ClarifaiService.class.getName()).debug(
                "Clarifai --> Add urls: " + (new Gson()).toJson(urls));

        // Execute asynchronously
        addInputsRequest.executeAsync(clarifaiInputCallback);
    }

    /**
     * Search objects recognized in Clarifai model database by string phrase
     * @param searchPhrase String phrase characterizing demanded images
     * @param uid User metadata value
     * @return Unique set of image urls for images with highest probability match
     */
    public synchronized Set<String> searchPhotos(@NotNull String searchPhrase,
                                                        @NotNull String uid) {

        // Return image uploads by requesting user only
        final JsonObject metadata = ClarifaiHelper.buildUserMetadata(uid);

        // Build request
        // Match both phrase and user
        ClarifaiRequest<List<SearchHit>> clarifaiRequest = clarifaiClient.searchInputs()
                .ands(SearchClause.matchConcept(Concept.forName(searchPhrase)),
                        SearchClause.matchMetadata(metadata))
                .build()
                .getPage(1);

        Logger.getLogger(ClarifaiService.class.getName()).debug(
                "Clarifai --> Search for: " + searchPhrase);

        // Execute synchronously
        ClarifaiResponse<List<SearchHit>> clarifaiResponse = clarifaiRequest.executeSync();

        // Process response (extract urls)
        Set<String> urls = new HashSet<>();
        if (clarifaiResponse.isSuccessful()) {
            // Request successful
            Logger.getLogger(ClarifaiService.class.getName()).debug(
                    "Clarifai <-- " + clarifaiResponse.get().size() + " images found");

            for (SearchHit searchHit : clarifaiResponse.get()) {
                ClarifaiImage clarifaiImage = searchHit.input().image();
                if (clarifaiImage instanceof ClarifaiURLImage) {
                    ClarifaiURLImage clarifaiURLImage = (ClarifaiURLImage) clarifaiImage;
                    String url = clarifaiURLImage.url().toString();
                    urls.add(url);
                }
            }
        }
        else {
            // Request not successful for whatever reason
            // ALSO FOR CONCEPTS NOT EXISTING YET !!
            Logger.getLogger(ClarifaiService.class.getName()).error(
                    "Clarifai <-- Request unsuccessful or the concept does not exist");
        }
        return urls;
    }
}
