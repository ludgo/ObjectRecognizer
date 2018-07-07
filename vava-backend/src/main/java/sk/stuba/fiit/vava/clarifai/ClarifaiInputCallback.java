package sk.stuba.fiit.vava.clarifai;

import clarifai2.api.request.ClarifaiRequest;
import clarifai2.dto.input.ClarifaiInput;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;

/**
 * Custom callback waiting for {@link clarifai2.api.ClarifaiResponse}
 */
public abstract class ClarifaiInputCallback implements ClarifaiRequest.Callback<List<ClarifaiInput>> {

    @Override
    public void onClarifaiResponseSuccess(List<ClarifaiInput> clarifaiInputs) {
        // Image urls have been successfully included in model
        // Note: Objects in images are recognized automatically
        Logger.getLogger(ClarifaiInputCallback.class.getName()).debug(
                "Clarifai <-- " + clarifaiInputs.size() + " images added");
    }

    /**
     * @param errorCode HTTP 4xx or 5xx
     */
    @Override
    public void onClarifaiResponseUnsuccessful(int errorCode) {
        // Image urls have NOT been successfully included in model
        Logger.getLogger(ClarifaiInputCallback.class.getName()).warn(
                "Error with code " + errorCode + " occurred during Clarifai input");
    }

    @Override
    public void onClarifaiResponseNetworkError(IOException e) {
        // Image urls have NOT been successfully included in model
        Logger.getLogger(ClarifaiInputCallback.class.getName()).error(
                "Network error occurred during Clarifai input", e);
    }
}
