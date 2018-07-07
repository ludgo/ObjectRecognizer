package sk.stuba.fiit.vava.android.ui.activity;

import android.os.Bundle;

import sk.stuba.fiit.vava.android.R;

/**
 * Image text search results activity
 */
public class ResultActivity extends UserActivity {

    // Intent key
    public static final String INTENT_KEY_IMAGE_URLS = "image_urls";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
    }
}
