package sk.stuba.fiit.vava.android.ui.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Size;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Set;

import sk.stuba.fiit.vava.android.R;
import sk.stuba.fiit.vava.android.background.task.SearchImagesTask;
import sk.stuba.fiit.vava.android.ui.activity.ResultActivity;

/**
 * Add and search for photos fragment
 */
public class MainActivityFragment extends Fragment {

    // Necessary empty constructor
    public MainActivityFragment() {
        // Never override! - use newInstance pattern instead
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_main, container, false);
        EditText searchEditText = (EditText) rootView.findViewById(R.id.searchPhrase);
        // Set listener on search action
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String textPhrase = v.getText().toString();
                    if (!"".equals(textPhrase)) {
                        performSearch(textPhrase);
                        return true;
                    }
                }
                return false;
            }
        });
        return rootView;
    }

    /**
     * Perform server image search
     * @param textPhrase A phrase describing the object to be included in result images
     */
    private void performSearch(@NonNull String textPhrase) {
        SearchImagesTask searchImagesTask = new SearchImagesTask() {
            @Override
            protected void onPostExecute(Set<String> urls) {
                if (urls != null && !urls.isEmpty()) {
                    toResultActivity(urls);
                }
            }
        };
        searchImagesTask.execute(textPhrase);
    }

    /**
     * To {@link ResultActivity}
     * @param urls Search result urls
     */
    private void toResultActivity(@NonNull @Size(min=1) Set<String> urls) {
        Intent intent = new Intent(getActivity(), ResultActivity.class);
        intent.putStringArrayListExtra(ResultActivity.INTENT_KEY_IMAGE_URLS,
                new ArrayList<>(urls));
        startActivity(intent);
    }
}
