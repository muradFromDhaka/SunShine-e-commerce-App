package com.abc.sunshine.Drawer.fragmentEx;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.abc.sunshine.Drawer.DrawerActivity;
import com.abc.sunshine.R;
import com.abc.sunshine.db.BrandDao;
import com.abc.sunshine.db.CategoryDao;
import com.abc.sunshine.entity.Brand;
import com.abc.sunshine.entity.Category;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryCreateFragment extends Fragment {

    private static final int PICK_IMAGE = 101;

    private EditText etName, etDescription;
    private Spinner spBrand;
    private ImageView ivImage;
    private Button btnPickImage, btnSave;

    private Uri imageUri; // selected image uri
    private CategoryDao categoryDao;
    private List<Brand> brandList;
    private Category editCategory = null; // update mode

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_category_create, container, false);

        // init views
        etName = view.findViewById(R.id.etCategoryName);
        etDescription = view.findViewById(R.id.etCategoryDescription);
        spBrand = view.findViewById(R.id.spBrand);
        ivImage = view.findViewById(R.id.ivCategoryImage);
        btnPickImage = view.findViewById(R.id.btnPickImage);
        btnSave = view.findViewById(R.id.btnSaveCategory);

        categoryDao = new CategoryDao(requireContext());
        BrandDao brandDao = new BrandDao(requireContext());
        brandList = brandDao.getAllBrands();

        // spinner setup
        ArrayAdapter<Brand> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                brandList
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBrand.setAdapter(adapter);

        // 🔹 check if update mode
        if (getArguments() != null && getArguments().containsKey("category")) {
            editCategory = (Category) getArguments().getSerializable("category");
            populateFields(editCategory);
        }

        // pick image
        btnPickImage.setOnClickListener(v -> openGallery());

        // save category
        btnSave.setOnClickListener(v -> saveCategory());

        return view;
    }

    // 🔹 Populate fields in update mode
    private void populateFields(Category category) {
        etName.setText(category.getName());
        etDescription.setText(category.getDescription());
        imageUri = Uri.parse(category.getImageUrl());
        Picasso.get()
                .load(imageUri)
                .placeholder(R.drawable.placeholder)
                .fit()
                .centerCrop()
                .into(ivImage);

        // spinner select
        for (int i = 0; i < brandList.size(); i++) {
            if (brandList.get(i).getId() == category.getBrandId()) {
                spBrand.setSelection(i);
                break;
            }
        }
    }

    // 🔹 Open Gallery
    private void openGallery() {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        );
        startActivityForResult(intent, PICK_IMAGE);
    }

    // 🔹 Receive selected image & preview
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.getData();

            Picasso.get()
                    .load(imageUri)
                    .placeholder(R.drawable.placeholder)
                    .fit()
                    .centerCrop()
                    .into(ivImage);
        }
    }

    // 🔹 Save or Update Category
    private void saveCategory() {
        String name = etName.getText().toString().trim();
        String desc = etDescription.getText().toString().trim();

        if (name.isEmpty()) {
            etName.setError("Required");
            return;
        }

        if (imageUri == null) {
            Toast.makeText(getContext(), "Select image", Toast.LENGTH_SHORT).show();
            return;
        }

        Brand selectedBrand = (Brand) spBrand.getSelectedItem();
        if (selectedBrand == null) {
            Toast.makeText(getContext(), "Select brand", Toast.LENGTH_SHORT).show();
            return;
        }

        long brandId = selectedBrand.getId();

        if (editCategory != null) {
            // 🔹 Update mode
            editCategory.setName(name);
            editCategory.setDescription(desc);
            editCategory.setBrandId(brandId);
            editCategory.setImageUrl(imageUri.toString());
            categoryDao.updateCategory(editCategory); // make sure DAO has update method
            Toast.makeText(getContext(), "Category updated", Toast.LENGTH_SHORT).show();
        } else {
            // 🔹 Insert mode
            Category category = new Category();
            category.setName(name);
            category.setDescription(desc);
            category.setBrandId(brandId);
            category.setImageUrl(imageUri.toString());
            categoryDao.insertCategory(category);
            Toast.makeText(getContext(), "Category created", Toast.LENGTH_SHORT).show();
        }

        // 🔹 Go back to list
        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new CategoryListFragment())
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DrawerActivity) requireActivity())
                .setToolbarTitle(editCategory != null ? "Update Category" : "Create Category");
    }
}
