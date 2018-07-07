package sk.stuba.fiit.vava.android.util;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import sk.stuba.fiit.vava.android.VavaApplication;

import static android.content.Context.MODE_PRIVATE;

/**
 * Token management tools and usage
 */
public class TokenManager {

    // SharedPreferences constants
    private static final String SP_FIREBASE_TOKEN_FILE_NAME = "sp_firebase_token_file_name";
    private static final String SP_FIREBASE_TOKEN_KEY = "sp_firebase_token_key";
    private static final String SP_FIREBASE_TOKEN_DEFAULT = "";

    /**
     * Persist Firebase token by saving it to SharedPreferences
     * @param token Firebase token
     */
    public static void setFirebaseToken(@NonNull String token) {
        SharedPreferences sharedPreferences = VavaApplication.getContext()
                .getSharedPreferences(SP_FIREBASE_TOKEN_FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SP_FIREBASE_TOKEN_KEY, token);
        editor.commit();
    }

    /**
     * Obtain persisted Firebase token from SharedPreferences
     * @return Firebase token
     */
    public static String getFirebaseToken() {
        SharedPreferences sharedPreferences = VavaApplication.getContext()
                .getSharedPreferences(SP_FIREBASE_TOKEN_FILE_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(SP_FIREBASE_TOKEN_KEY, SP_FIREBASE_TOKEN_DEFAULT);
    }

    /**
     * Remove saved FirebaseToken
     */
    public static void clearFirebaseToken() {
        setFirebaseToken("");
    }
}
