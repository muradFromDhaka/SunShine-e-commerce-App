//package com.abc.sunshine.adapters;
//
//import android.content.Context;
//import android.net.Uri;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.abc.sunshine.R;
//import com.abc.sunshine.entity.Category;
//import com.squareup.picasso.Picasso;
//
//import java.util.List;
//import java.util.Map;
//
//public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
//
//    private Context context;
//    private List<Category> categoryList;
//    private OnCategoryLongClickListener listener;
//    private Map<Long, String> brandNames;
//
//    public interface OnCategoryLongClickListener {
//        void onLongClick(Category category);
//    }
//
//    // Constructor
//    public CategoryAdapter(Context context, List<Category> categoryList,
//                           Map<Long, String> brandNames,
//                           OnCategoryLongClickListener listener) {
//        this.context = context;
//        this.categoryList = categoryList;
//        this.brandNames = brandNames;
//        this.listener = listener;
//    }
//
//    @NonNull
//    @Override
//    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.row_category, parent, false);
//        return new CategoryViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
//        Category category = categoryList.get(position);
//
//        holder.name.setText(category.getName());
//        holder.description.setText(category.getDescription());
//
//        // Brand name
//        String brandName = brandNames.get(category.getBrandId());
//        holder.brandName.setText(brandName != null ? brandName : "Unknown Brand");
//
//        // Image from URL or local Uri
//        if (category.getImageUrl() != null && !category.getImageUrl().isEmpty()) {
//            try {
//                Uri uri = Uri.parse(category.getImageUrl());
//                Picasso.get()
//                        .load(uri)
//                        .placeholder(R.drawable.placeholder)
//                        .fit()
//                        .centerCrop()
//                        .into(holder.image);
//            } catch (Exception e) {
//                holder.image.setImageResource(R.drawable.placeholder);
//            }
//        } else {
//            holder.image.setImageResource(R.drawable.placeholder);
//        }
//
//        // Long click for update/delete
//        holder.itemView.setOnLongClickListener(v -> {
//            listener.onLongClick(category);
//            return true;
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return categoryList.size();
//    }
//
//    // 🔹 Helper method: Add new category and refresh
//    public void addCategory(Category category) {
//        categoryList.add(category);
//        notifyItemInserted(categoryList.size() - 1);
//    }
//
//    // 🔹 Helper method: Refresh whole list
//    public void updateCategories(List<Category> newList) {
//        categoryList.clear();
//        categoryList.addAll(newList);
//        notifyDataSetChanged();
//    }
//
//    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
//        TextView name, description, brandName;
//        ImageView image;
//
//        public CategoryViewHolder(@NonNull View itemView) {
//            super(itemView);
//            name = itemView.findViewById(R.id.tvCategoryName);
//            description = itemView.findViewById(R.id.tvCategoryDescription);
//            brandName = itemView.findViewById(R.id.tvBrandName);
//            image = itemView.findViewById(R.id.ivCategoryImage);
//        }
//    }
//}
//


package com.abc.sunshine.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abc.sunshine.R;
import com.abc.sunshine.entity.Category;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context context;
    private List<Category> categoryList = new ArrayList<>();
    private Map<Long, String> brandNames;
    private OnCategoryLongClickListener listener;

    // 🔹 Long click interface
    public interface OnCategoryLongClickListener {
        void onLongClick(Category category);
    }

    public CategoryAdapter(Context context,
                           List<Category> categoryList,
                           Map<Long, String> brandNames,
                           OnCategoryLongClickListener listener) {

        this.context = context;
        this.categoryList = categoryList;
        this.brandNames = brandNames;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.row_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

        Category category = categoryList.get(position);

        holder.name.setText(category.getName());
//        holder.description.setText(category.getDescription());

        // 🔹 Brand name resolve
        String brandName = brandNames.get(category.getBrandId());
        holder.brandName.setText(
                brandName != null ? "Brand: " + brandName : "Unknown Brand"
        );

        // 🔹 Image handling (gallery / camera / url)
        String imagePath = category.getImageUrl();

        if (imagePath != null && !imagePath.trim().isEmpty()) {
            Picasso.get()
                    .load(Uri.parse(imagePath))
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .fit()
                    .centerCrop()
                    .into(holder.image);
        } else {
            holder.image.setImageResource(R.drawable.placeholder);
        }

        // 🔹 Long click safe call
        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) {
                listener.onLongClick(category);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    // ✅ Replace whole list (best for reload from DB)
    public void setCategories(List<Category> newList) {
        categoryList.clear();
        categoryList.addAll(newList);
        notifyDataSetChanged();
    }

    // ✅ Add single item
    public void addCategory(Category category) {
        categoryList.add(category);
        notifyItemInserted(categoryList.size() - 1);
    }

    // ✅ Remove item
    public void removeCategory(int position) {
        if (position >= 0 && position < categoryList.size()) {
            categoryList.remove(position);
            notifyItemRemoved(position);
        }
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {

        TextView name, description, brandName;
        ImageView image;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvCategoryName);
//            description = itemView.findViewById(R.id.tvCategoryDescription);
            brandName = itemView.findViewById(R.id.tvBrandName);
            image = itemView.findViewById(R.id.ivCategoryImage);
        }
    }
}
