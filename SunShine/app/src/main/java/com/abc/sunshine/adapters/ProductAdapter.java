package com.abc.sunshine.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abc.sunshine.R;
import com.abc.sunshine.entity.Product;
import com.bumptech.glide.Glide;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;
    private OnItemClickListener listener;

    // Interface for click events
    public interface OnItemClickListener {
        void onItemClick(Product product);
        void onEditClick(Product product);
        void onImageClick(Product product);
        void onDeleteClick(Product product);
    }

    public ProductAdapter(Context context, List<Product> productList, OnItemClickListener listener) {
        this.context = context;
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.row_products, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        Product product = productList.get(position);

        holder.tvName.setText(product.getName());
        holder.tvPrice.setText("৳ " + product.getPrice());
        holder.tvQuantity.setText("Stock: " + product.getQuantity());
        holder.tvReviews.setText("(" + product.getReviewsCount() + " reviews)");
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(productList.get(position)));

        // Load first image using Glide
        if (product.getImageUrls() != null && !product.getImageUrls().isEmpty()) {
            Glide.with(context)
                    .load(product.getImageUrls().get(0))
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(holder.ivProduct);
        } else {
            holder.ivProduct.setImageResource(R.drawable.ic_launcher_foreground);
        }

        // Click events
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(product);
        });

        holder.ivProduct.setOnClickListener(v -> {
            if (listener != null) listener.onImageClick(product);
        });

        // Optional: Add edit button click (if exists in layout)
        // holder.btnEdit.setOnClickListener(v -> listener.onEditClick(product));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProduct;
        TextView tvName, tvPrice, tvQuantity, tvReviews;
        ImageButton btnDelete;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            ivProduct = itemView.findViewById(R.id.ivProduct);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
            tvQuantity = itemView.findViewById(R.id.tvProductQuantity);
            tvReviews = itemView.findViewById(R.id.tvProductReviews);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
