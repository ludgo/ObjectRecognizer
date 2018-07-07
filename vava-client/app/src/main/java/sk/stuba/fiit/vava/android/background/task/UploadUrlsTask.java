package sk.stuba.fiit.vava.android.background.task;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sk.stuba.fiit.vava.android.db.entity.TemporaryUrl;
import sk.stuba.fiit.vava.android.util.TokenManager;
import sk.stuba.fiit.vava.android.util.Utilities;
import sk.stuba.fiit.vava.client.java.model.UrlResponse;
import sk.stuba.fiit.vava.client.java.rest.RestCallBuilder;

/**
 * Pass pending image urls to server
 */
public class UploadUrlsTask extends AsyncTask<Void, Void, Void> {

    private String uid;

    /**
     * Constructor
     * @param uid User ID
     */
    public UploadUrlsTask(@NonNull String uid) {
        this.uid = uid;
    }

    @Override
    protected Void doInBackground(Void... params) {
        // Include all urls for images already on S3 but not at server yet for currently signed user
        List<TemporaryUrl> temporaryUrlList = Utilities.getTemporaryUrlProvider().find(uid);
        if (temporaryUrlList.isEmpty()) {
            // Nothing to upload
            return null;
        }

        // Prepare unique urls
        Set<String> urls = new HashSet<>();
        for (TemporaryUrl temporaryUrl : temporaryUrlList) {
            urls.add(temporaryUrl.getUrl());
        }

        // Call server input endpoint
        Call<UrlResponse> call = (new RestCallBuilder(TokenManager.getFirebaseToken()))
                .inputUrl(urls);
        call.enqueue(new Callback<UrlResponse>() {
            @Override
            public void onResponse(Call<UrlResponse> call, Response<UrlResponse> response) {
                if (response.isSuccessful()) {
                    // Do not store urls successfully uploaded on server any more
                    Set<String> urls = response.body().getUrls();
                    if (!urls.isEmpty()) {
                        Utilities.getTemporaryUrlProvider().delete(urls, uid);
                    }
                }
            }

            @Override
            public void onFailure(Call<UrlResponse> call, Throwable t) {
                Log.e(UploadUrlsTask.class.getName(), "Url upload to server failed", t);
            }
        });
        return null;
    }
}
