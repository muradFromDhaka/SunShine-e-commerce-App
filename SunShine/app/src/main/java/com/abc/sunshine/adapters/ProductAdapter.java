package com.abc.sunshine.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abc.sunshine.R;
import com.abc.sunshine.entity.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
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
        holder.ratingBar.setRating(product.());

        // Image load (simple version)
        // If you use Glide/Picasso later, this is the place
        if (product.getImageUrls() != null && !product.getImageUrls().isEmpty()) {
            // example: Glide.with(context).load(product.getImageUrls().get(0)).into(holder.ivProduct);
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // 🔽 ViewHolder
    static class ProductViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProduct;
        TextView tvName, tvPrice, tvQuantity;
        RatingBar ratingBar;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            ivProduct = itemView.findViewById(R.id.ivProduct);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
            tvQuantity = itemView.findViewById(R.id.tvProductQuantity);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }
}
