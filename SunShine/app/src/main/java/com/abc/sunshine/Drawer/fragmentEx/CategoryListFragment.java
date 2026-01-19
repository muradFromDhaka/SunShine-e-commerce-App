package com.abc.sunshine.Drawer.fragmentEx;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abc.sunshine.Drawer.DrawerActivity;
import com.abc.sunshine.R;
import com.abc.sunshine.adapters.CategoryAdapter;
import com.abc.sunshine.db.BrandDao;
import com.abc.sunshine.db.CategoryDao;
import com.abc.sunshine.entity.Brand;
import com.abc.sunshine.entity.Category;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryListFragment extends Fragment {

    private RecyclerView recyclerView;
    private CategoryAdapter adapter;
    private CategoryDao categoryDao;
    private BrandDao brandDao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_category_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerCategory);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        categoryDao = new CategoryDao(requireContext());
        brandDao = new BrandDao(requireContext());

        loadCategories();

        return view;
    }

    private void loadCategories() {

        List<Category> categories = categoryDao.getAllCategories(null);

        // 🔹 brandId → brandName map
        Map<Long, String> brandMap = new HashMap<>();
        for (Brand brand : brandDao.getAllBrands()) {
            brandMap.put(brand.getId(), brand.getName());
        }

        adapter = new CategoryAdapter(
                requireContext(),
                categories,
                brandMap,
                category -> showActionDialog(category)
        );

        recyclerView.setAdapter(adapter);
    }

    // 🔹 Update / Delete dialog
    private void showActionDialog(Category category) {

        String[] options = {"Update", "Delete"};

        new AlertDialog.Builder(requireContext())
                .setTitle("Select Action")
                .setItems(options, (dialog, which) -> {

                    if (which == 0) {
                        openUpdateScreen(category);
                    } else {
                        confirmDelete(category);
                    }
                })
                .show();
    }

    // 🔹 Go to update fragment
    private void openUpdateScreen(Category category) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("category", category);

        CategoryCreateFragment fragment = new CategoryCreateFragment();
        fragment.setArguments(bundle);

        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    // 🔹 Delete confirmation
    private void confirmDelete(Category category) {

        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Category")
                .setMessage("Are you sure you want to delete this category?")
                .setPositiveButton("Yes", (d, w) -> {
                    categoryDao.deleteCategory(category.getId());
                    Toast.makeText(requireContext(),
                            "Category deleted",
                            Toast.LENGTH_SHORT).show();
                    loadCategories(); // refresh list
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DrawerActivity) requireActivity())
                .setToolbarTitle("Category List");
    }
}
