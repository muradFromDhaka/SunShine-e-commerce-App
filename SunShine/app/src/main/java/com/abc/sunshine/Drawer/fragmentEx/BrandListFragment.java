
package com.abc.sunshine.Drawer.fragmentEx;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abc.sunshine.Drawer.DrawerActivity;
import com.abc.sunshine.R;
import com.abc.sunshine.adapters.BrandAdapter;
import com.abc.sunshine.db.BrandDao;
import com.abc.sunshine.entity.Brand;

import java.util.List;

public class BrandListFragment extends Fragment {

    private RecyclerView recyclerView;
    private BrandAdapter adapter;
    private BrandDao brandDao;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_brand_list, container, false);

        Button goBrand = view.findViewById(R.id.goBrand);
        recyclerView = view.findViewById(R.id.recyclerBrands);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        brandDao = new BrandDao(requireContext());

        loadBrands();

        goBrand.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new BrandCreateFragment())
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    private void loadBrands() {
        List<Brand> brandList = brandDao.getAllBrands();

        adapter = new BrandAdapter(requireContext(), brandList, (brand, position) -> {
            // Long click: show edit/delete options
            String[] options = {"Update", "Delete"};

            new AlertDialog.Builder(requireContext())
                    .setTitle("Select Action")
                    .setItems(options, (dialog, which) -> {
                        if (which == 0) {
                            // Edit fragment
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("brand", brand);

                            BrandCreateFragment fragment = new BrandCreateFragment();
                            fragment.setArguments(bundle);

                            requireActivity()
                                    .getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragment_container, fragment)
                                    .addToBackStack(null)
                                    .commit();
                        } else if (which == 1) {
                            // Delete confirmation
                            new AlertDialog.Builder(requireContext())
                                    .setTitle("Confirm Delete")
                                    .setMessage("Are you sure you want to delete this brand?")
                                    .setPositiveButton("Yes", (d, w) -> {
                                        brandDao.deleteBrand(brand.getId());
                                        Toast.makeText(requireContext(), "Brand deleted", Toast.LENGTH_SHORT).show();
                                        loadBrands(); // refresh list
                                    })
                                    .setNegativeButton("No", (d, w) -> d.dismiss())
                                    .show();
                        }
                    })
                    .show();
        });

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DrawerActivity) requireActivity()).setToolbarTitle("Brand List");
    }
}
