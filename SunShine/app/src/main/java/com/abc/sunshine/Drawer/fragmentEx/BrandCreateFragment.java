package com.abc.sunshine.Drawer.fragmentEx;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.abc.sunshine.Drawer.DrawerActivity;
import com.abc.sunshine.R;
import com.abc.sunshine.db.BrandDao;
import com.abc.sunshine.entity.Brand;

public class BrandCreateFragment extends Fragment {

    private EditText etBrandName, etBrandDescription;
    private Button btnSaveBrand;

    private Brand brand; // 🔑 important (null = insert, not null = update)

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(
                R.layout.fragment_brand_create,
                container,
                false
        );

        etBrandName = view.findViewById(R.id.etBrandName);
        etBrandDescription = view.findViewById(R.id.etBrandDescription);
        btnSaveBrand = view.findViewById(R.id.btnSaveBrand);

        // 📦 get brand from arguments (EDIT case)
        if (getArguments() != null) {
            brand = (Brand) getArguments().getSerializable("brand");
            if (brand != null) {
                etBrandName.setText(brand.getName());
                etBrandDescription.setText(brand.getDescription());
                btnSaveBrand.setText("Update Brand");
            }
        }

        btnSaveBrand.setOnClickListener(v -> saveOrUpdateBrand());

        return view;
    }

    private void saveOrUpdateBrand() {

        String name = etBrandName.getText().toString().trim();
        String description = etBrandDescription.getText().toString().trim();

        if (name.isEmpty()) {
            etBrandName.setError("Brand name required");
            return;
        }

        BrandDao brandDao = new BrandDao(requireContext());

        if (brand == null) {
            // ✅ INSERT
            Brand newBrand = new Brand(name, description);
            brandDao.insertBrand(newBrand);
            Toast.makeText(requireContext(),
                    "Brand created successfully",
                    Toast.LENGTH_SHORT).show();
        } else {
            // 🔄 UPDATE
            brand.setName(name);
            brand.setDescription(description);
            brandDao.updateBrand(brand.getId(),brand);
            Toast.makeText(requireContext(),
                    "Brand updated successfully",
                    Toast.LENGTH_SHORT).show();
        }

        goToBrandList();
    }

    private void goToBrandList() {
        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new BrandListFragment())
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DrawerActivity) requireActivity())
                .setToolbarTitle(
                        brand == null ? "Create Brand" : "Edit Brand"
                );
    }
}
