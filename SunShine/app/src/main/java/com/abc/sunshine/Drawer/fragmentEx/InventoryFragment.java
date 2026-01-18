package com.abc.sunshine.Drawer.fragmentEx;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abc.sunshine.Drawer.DrawerActivity;
import com.abc.sunshine.R;

public class InventoryFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DrawerActivity) requireActivity())
                .setToolbarTitle("inventory");
    }
}