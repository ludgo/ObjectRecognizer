package sk.stuba.fiit.vava.util;

/**
 * Application constants
 */
public class Constants {

    // Firebase Admin SDK configuration .json file
    public static final String FIREBASE_AUTH_PRIVATE_KEY_FILE_NAME = "vava-b94bd-firebase-adminsdk-uv1bz-da450a7e7c.json";
    // Firebase database URL
    public static final String FIREBASE_DATABASE_URL = "https://vava-b94bd.firebaseio.com";

    // Auth provider name
    public static final String AUTH_PROVIDER_FIREBASE = "firebase";

    // HTTP header content type
    public static final String CONTENT_TYPE_JSON = "application/json";

    // Default HTTP response bodies for REST
    public static final String RESPONSE_BODY_DEFAULT_SUCCESS = "{ \"status\": \"success\" }";
    public static final String RESPONSE_BODY_DEFAULT_ERROR = "{ \"status\": \"error\" }";

    // Clarifai user metadata key
    public static final String METADATA_KEY_UID = "uid";
    // Search phrase requirements
    public static final int SEARCH_PHRASE_LENGTH_MIN = 3;

    // Application's routes
    public static final String PATH_AUTH = "/auth";
    public static final String PATH_AUTH_LOGIN = "/login";
    public static final String PATH_INPUT = "/input";
    public static final String PATH_INPUT_URL = "/url";
    public static final String PATH_SEARCH = "/search";
    public static final String PATH_SEARCH_PHRASE = "/phrase";

}
