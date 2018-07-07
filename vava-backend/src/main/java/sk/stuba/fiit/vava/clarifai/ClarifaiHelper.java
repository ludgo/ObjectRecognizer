package sk.stuba.fiit.vava.clarifai;


import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import com.google.gson.JsonObject;
import sk.stuba.fiit.vava.util.Constants;

import javax.validation.constraints.NotNull;


/**
 * Clarifai client utilities
 * https://www.clarifai.com/api
 */
public class ClarifaiHelper {

    /**
     * Build {@link ClarifaiClient}
     * @param id Clarifai ID
     * @param secret Clarifai secret
     * @return Usable Clarifai client
     */
    public static synchronized ClarifaiClient buildClarifaiClient(@NotNull String id, @NotNull String secret) {
        return new ClarifaiBuilder(id, secret).buildSync();
    }

    /**
     * Construct Clarifai per-image-stored user metadata
     * @param value User specific value
     * @return Metadata JSON object
     */
    public static JsonObject buildUserMetadata(@NotNull String value) {
        JsonObject metadata = new JsonObject();
        metadata.addProperty(Constants.METADATA_KEY_UID, value);
        return metadata;
    }
}
