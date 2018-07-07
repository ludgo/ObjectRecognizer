package sk.stuba.fiit.vava.android.ui.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import sk.stuba.fiit.vava.android.R;
import sk.stuba.fiit.vava.android.ui.activity.ResultActivity;
import sk.stuba.fiit.vava.android.ui.adapter.GridAdapter;

/**
 * Result images grid fragment
 */
public class ResultActivityFragment extends Fragment {

    // Necessary empty constructor
    public ResultActivityFragment() {
        // Never override! - use newInstance pattern instead
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_result, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        // Performance improvement - only do following if no layout size change!
        recyclerView.setHasFixedSize(true);

        // Set manager for 2 column grid
        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        // Get urls of images to be displayed
        Intent intent = getActivity().getIntent();
        List<String> imageUrls = intent.getStringArrayListExtra(ResultActivity.INTENT_KEY_IMAGE_URLS);

        // Bind adapter to RecyclerView
        GridAdapter gridAdapter = new GridAdapter(imageUrls);
        recyclerView.setAdapter(gridAdapter);

        return rootView;
    }
}
