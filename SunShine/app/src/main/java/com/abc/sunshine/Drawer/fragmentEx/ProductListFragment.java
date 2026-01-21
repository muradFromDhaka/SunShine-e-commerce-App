package com.abc.sunshine.Drawer.fragmentEx;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abc.sunshine.Drawer.DrawerActivity;
import com.abc.sunshine.R;
import com.abc.sunshine.adapters.ProductAdapter;
import com.abc.sunshine.db.ProductDao;
import com.abc.sunshine.entity.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductListFragment extends Fragment
        implements ProductAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private ProductDao productDao;
    private List<Product> productList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_product_list, container, false);

        Button btnAddProduct = view.findViewById(R.id.btnAddProduct);
        recyclerView = view.findViewById(R.id.rvProducts);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));

        productDao = new ProductDao(requireContext());

        adapter = new ProductAdapter(requireContext(), productList, this);
        recyclerView.setAdapter(adapter);

        loadProducts();

        btnAddProduct.setOnClickListener(v -> {
            // ProductCreateFragment খুলবো
            ProductCreateFragment fragment = new ProductCreateFragment();

            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)  // fragmentContainer হলো FrameLayout/ConstraintLayout ID
                    .addToBackStack(null) // back button-এ ফিরে আসতে পারে
                    .commit();
        });

        return view;
    }

    private void loadProducts() {
        productList.clear();
        productList.addAll(productDao.getAll());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DrawerActivity) requireActivity())
                .setToolbarTitle("Product List");
        loadProducts(); // 🔁 refresh when coming back
    }

    // ---------------- Adapter Clicks ----------------

    @Override
    public void onItemClick(Product product) {
        openProductCreateFragment(product.getId());
    }

    @Override
    public void onEditClick(Product product) {
        openProductCreateFragment(product.getId());
    }

    @Override
    public void onImageClick(Product product) {
        // optional: fullscreen image / gallery
    }

    @Override
    public void onDeleteClick(Product product) {
        // AlertDialog তৈরি
        new AlertDialog.Builder(requireContext())
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this product?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    // ইউজার Delete চাপলে
                    productDao.delete(product.getId()); // Database থেকে remove
                    loadProducts(); // RecyclerView refresh
                    Toast.makeText(requireContext(), "Product deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // ইউজার Cancel করলে শুধু dialog বন্ধ হবে
                    dialog.dismiss();
                })
                .show();
    }

    // ---------------- Helper ----------------
    private void openProductCreateFragment(long productId) {
        ProductCreateFragment fragment = new ProductCreateFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("productId", productId);
        fragment.setArguments(bundle);

        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment) // <-- activity layout-এর container id
                .addToBackStack(null)
                .commit();
    }


}
