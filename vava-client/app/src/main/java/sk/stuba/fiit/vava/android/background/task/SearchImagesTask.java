package sk.stuba.fiit.vava.android.background.task;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Response;
import sk.stuba.fiit.vava.android.util.TokenManager;
import sk.stuba.fiit.vava.client.java.rest.RestCallBuilder;
import sk.stuba.fiit.vava.client.java.model.UrlResponse;
import sk.stuba.fiit.vava.client.java.util.Constants;

/**
 * Ask server for urls of images containing objects matching desired text phrase
 */
public class SearchImagesTask extends AsyncTask<String, Void, Set<String>> {

    @Override
    protected Set<String> doInBackground(String... params) {
        String phrase = params[0];
        if (phrase == null || phrase.length() < Constants.SEARCH_PHRASE_LENGTH_MIN) return null;
        // Call server search endpoint
        Call<UrlResponse> call = (new RestCallBuilder(TokenManager.getFirebaseToken()))
                .searchPhrase(phrase);
        try {
            Response<UrlResponse> response = call.execute();
            if (response.isSuccessful()) {
                // Inform executor about findings
                return response.body().getUrls();
            }
        }
        catch (IOException e) {
            Log.e(SearchImagesTask.class.getName(), "Text search failed", e);
        }
        return null;
    }
}
