package com.abc.sunshine.Drawer.fragmentEx;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.abc.sunshine.Drawer.DrawerActivity;
import com.abc.sunshine.R;
import com.abc.sunshine.adapters.ImagePreviewAdapter;
import com.abc.sunshine.db.DatabaseHelper;
import com.abc.sunshine.db.ProductDao;
import com.abc.sunshine.entity.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductCreateFragment extends Fragment {

    private EditText etName, etDesc, etPrice, etDiscount, etQty, etSku;
    private Button btnAddImages, btnSubmit;
    private RecyclerView rvImages;
    private Spinner spCategory, spBrand;

    private List<Uri> imageUris = new ArrayList<>();
    private ImagePreviewAdapter imageAdapter;

    private ProductDao productDao;

    private long productId = -1; // 🔑 create or edit

    private ActivityResultLauncher<String[]> galleryLauncher;

    private List<String> brandNames = new ArrayList<>();
    private List<Long> brandIds = new ArrayList<>();
    private List<String> categoryNames = new ArrayList<>();
    private List<Long> categoryIds = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_product_create, container, false);

        initViews(view);
        setupRecycler();
        setupGalleryLauncher();

        productDao = new ProductDao(requireContext());

        loadBrands();
        loadCategories();

        // 🔁 Check edit mode
        if (getArguments() != null) {
            productId = getArguments().getLong("productId", -1);
            if (productId != -1) {
                loadProductForEdit(productId);
            }
        }

        btnAddImages.setOnClickListener(v ->
                galleryLauncher.launch(new String[]{"image/*"})
        );

        btnSubmit.setOnClickListener(v -> saveProduct());

        return view;
    }

    private void initViews(View view) {
        etName = view.findViewById(R.id.etProductName);
        etDesc = view.findViewById(R.id.etProductDescription);
        etPrice = view.findViewById(R.id.etProductPrice);
        etDiscount = view.findViewById(R.id.etDiscountPrice);
        etQty = view.findViewById(R.id.etQuantity);
        etSku = view.findViewById(R.id.etSku);
        btnAddImages = view.findViewById(R.id.btnAddImages);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        rvImages = view.findViewById(R.id.rvImages);
        spBrand = view.findViewById(R.id.spBrand);
        spCategory = view.findViewById(R.id.spCategory);
    }

    private void setupRecycler() {
        rvImages.setLayoutManager(
                new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        );
        imageAdapter = new ImagePreviewAdapter(imageUris);
        rvImages.setAdapter(imageAdapter);
    }

    private void setupGalleryLauncher() {
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.OpenMultipleDocuments(),
                uris -> {
                    if (uris != null) {
                        imageUris.addAll(uris);
                        imageAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void loadBrands() {
        brandNames.clear();
        brandIds.clear();

        SQLiteDatabase db = new DatabaseHelper(requireContext()).getReadableDatabase();
        Cursor c = db.query(DatabaseHelper.TABLE_BRAND,
                null, null, null, null, null, "name ASC");

        while (c.moveToNext()) {
            brandIds.add(c.getLong(c.getColumnIndexOrThrow("id")));
            brandNames.add(c.getString(c.getColumnIndexOrThrow("name")));
        }
        c.close();
        db.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                brandNames
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBrand.setAdapter(adapter);
    }

    private void loadCategories() {
        categoryNames.clear();
        categoryIds.clear();

        SQLiteDatabase db = new DatabaseHelper(requireContext()).getReadableDatabase();
        Cursor c = db.query(DatabaseHelper.TABLE_CATEGORY,
                null, null, null, null, null, "name ASC");

        while (c.moveToNext()) {
            categoryIds.add(c.getLong(c.getColumnIndexOrThrow("id")));
            categoryNames.add(c.getString(c.getColumnIndexOrThrow("name")));
        }
        c.close();
        db.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                categoryNames
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(adapter);
    }

    // 🔁 EDIT MODE DATA LOAD
    private void loadProductForEdit(long id) {
        Product p = productDao.getById(id);
        if (p == null) return;

        etName.setText(p.getName());
        etDesc.setText(p.getDescription());
        etPrice.setText(String.valueOf(p.getPrice()));
        etDiscount.setText(String.valueOf(p.getDiscountPrice()));
        etQty.setText(String.valueOf(p.getQuantity()));
        etSku.setText(p.getSku());

        spBrand.setSelection(brandIds.indexOf(p.getBrandId()));
        spCategory.setSelection(categoryIds.indexOf(p.getCategoryId()));

        for (String s : p.getImageUrls()) {
            imageUris.add(Uri.parse(s));
        }
        imageAdapter.notifyDataSetChanged();

        btnSubmit.setText("Update Product");
    }

    private void goToProductList() {
        ProductListFragment listFragment = new ProductListFragment();

        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, listFragment)
                .addToBackStack(null)
                .commit();
    }

    private void saveProduct() {
        try {
            Product product = new Product(
                    etName.getText().toString().trim(),
                    etDesc.getText().toString().trim(),
                    Double.parseDouble(etPrice.getText().toString()),
                    Double.parseDouble(etDiscount.getText().toString()),
                    etSku.getText().toString().trim(),
                    0,
                    Integer.parseInt(etQty.getText().toString()),
                    categoryIds.get(spCategory.getSelectedItemPosition()),
                    brandIds.get(spBrand.getSelectedItemPosition()),
                    uriToString(imageUris),
                    true
            );

            long result;
            if (productId == -1) {
                result = productDao.insert(product);
            } else {
                product.setId(productId);
                result = productDao.update(product);
            }

            if (result > 0) {
                Toast.makeText(requireContext(),
                        productId == -1 ? "Product Added successfully" : "Product Updated successfully",
                        Toast.LENGTH_SHORT).show();
                goToProductList();
            }

        } catch (Exception e) {
            Toast.makeText(requireContext(),
                    "Invalid input", Toast.LENGTH_SHORT).show();
        }
    }

    private List<String> uriToString(List<Uri> uris) {
        List<String> list = new ArrayList<>();
        for (Uri u : uris) list.add(u.toString());
        return list;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DrawerActivity) requireActivity())
                .setToolbarTitle(productId == -1 ? "Add Product" : "Edit Product");
    }
}
