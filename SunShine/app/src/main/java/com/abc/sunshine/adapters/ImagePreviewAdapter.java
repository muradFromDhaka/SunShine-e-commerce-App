package com.abc.sunshine.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abc.sunshine.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImagePreviewAdapter extends RecyclerView.Adapter<ImagePreviewAdapter.ImageViewHolder> {

    private List<Uri> imageList;

    public ImagePreviewAdapter(List<Uri> imageList) {
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_image_preview, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Uri uri = imageList.get(position);

        // Picasso দিয়ে load করা
        Picasso.get()
                .load(uri)
                .fit()           // ImageView-এর size অনুযায়ী fit করবে
                .centerCrop()    // Crop করে ImageView-এ বসাবে
                .placeholder(R.drawable.ic_launcher_foreground) // placeholder image
                .error(R.drawable.ic_launcher_foreground)       // error image
                .into(holder.ivImage);
    }


    @Override
    public int getItemCount() {
        return imageList.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImagePreview);
        }
    }
}

