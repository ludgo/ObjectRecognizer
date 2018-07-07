package sk.stuba.fiit.vava.android.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import sk.stuba.fiit.vava.android.R;

/**
 * Recommended structure image grid adapter for RecyclerView
 */
public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {

    private static final int ITEM_WIDTH = 480;
    private static final int ITEM_HEIGHT = 360;

    // Image urls of images to be loaded and displayed in grid
    private List<String> imageUrls;

    /**
     * Recommended structure image grid holder for RecyclerView
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;

        ViewHolder(ImageView imageView) {
            super(imageView);
            this.imageView = imageView;
        }
    }

    public GridAdapter(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    @Override
    public GridAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // A holder per each view group
        ImageView imageView = (ImageView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_item, parent, false);

        return new ViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Outsource image loading to Picasso
        Picasso.with(holder.imageView.getContext())
                .load(imageUrls.get(position))
                .resize(GridAdapter.ITEM_WIDTH, GridAdapter.ITEM_HEIGHT)
                .centerInside()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return (imageUrls == null) ? 0 : imageUrls.size();
    }
}
