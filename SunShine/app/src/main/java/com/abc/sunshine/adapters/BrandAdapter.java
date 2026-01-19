
package com.abc.sunshine.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abc.sunshine.R;
import com.abc.sunshine.entity.Brand;

import java.util.List;

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.BrandViewHolder> {

    public interface OnBrandLongClickListener {
        void onLongClick(Brand brand, int position);
    }

    private Context context;
    private List<Brand> brandList;
    private OnBrandLongClickListener listener;

    public BrandAdapter(Context context, List<Brand> brandList, OnBrandLongClickListener listener) {
        this.context = context;
        this.brandList = brandList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BrandViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_brand, parent, false);
        return new BrandViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BrandViewHolder holder, int position) {
        Brand brand = brandList.get(position);
        holder.tvName.setText(brand.getName());
        holder.tvDescription.setText(brand.getDescription());

        holder.itemView.setOnLongClickListener(v -> {
            listener.onLongClick(brand, position);
            return true; // Event handled
        });
    }

    @Override
    public int getItemCount() {
        return brandList.size();
    }

    static class BrandViewHolder extends RecyclerView.ViewHolder {
        TextView tvName,tvDescription;

        public BrandViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvBrandName);
            tvDescription = itemView.findViewById(R.id.tvBrandDescription);
        }
    }
}
