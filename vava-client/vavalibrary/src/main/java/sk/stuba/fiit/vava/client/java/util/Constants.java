package sk.stuba.fiit.vava.client.java.util;

/**
 * Client library constants
 */
public class Constants {

    // Auth provider name
    public static final String AUTH_PROVIDER_FIREBASE = "firebase";

    // TODO change url after server deployment
    public static final String SERVER_API_BASE_URL = "http:/192.168.1.19:8080";
    // Server API routes
    public static final String PATH_AUTH_LOGIN = "/auth/login";
    public static final String PATH_INPUT_URL = "/input/url";
    public static final String PATH_SEARCH_PHRASE = "/search/phrase";

    // Search phrase requirements
    public static final int SEARCH_PHRASE_LENGTH_MIN = 3;
}
