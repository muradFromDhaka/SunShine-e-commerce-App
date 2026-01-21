package com.abc.sunshine.Drawer;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.abc.sunshine.Drawer.fragmentEx.AboutFragment;
import com.abc.sunshine.Drawer.fragmentEx.BrandListFragment;
import com.abc.sunshine.Drawer.fragmentEx.CategoryListFragment;
import com.abc.sunshine.Drawer.fragmentEx.CustomerFragment;
import com.abc.sunshine.Drawer.fragmentEx.HomeFragment;
import com.abc.sunshine.Drawer.fragmentEx.InventoryFragment;
import com.abc.sunshine.Drawer.fragmentEx.ProductCreateFragment;
import com.abc.sunshine.Drawer.fragmentEx.ProductListFragment;
import com.abc.sunshine.Drawer.fragmentEx.ProfileFragment;
import com.abc.sunshine.Drawer.fragmentEx.SettingsFragment;
import com.abc.sunshine.Drawer.fragmentEx.VendorFragment;
import com.abc.sunshine.Drawer.fragmentEx.logoutFragment;
import com.abc.sunshine.R;
import com.google.android.material.navigation.NavigationView;

public class DrawerActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_drawer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,drawerLayout,toolbar, R.string.open, R.string.close
        );

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item->{
            int id = item.getItemId();
            Fragment fragment = null;

            if(id== R.id.nav_home){
                Toast.makeText(this, "Home Clicked", Toast.LENGTH_SHORT).show();
                fragment = new HomeFragment();
            }else if(id== R.id.nav_profile) {
                Toast.makeText(this, "Profile Clicked", Toast.LENGTH_SHORT).show();
                fragment = new ProfileFragment();
            }else if (id == R.id.nav_settings) {
                Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show();
                fragment = new SettingsFragment();
            } else if (id == R.id.nav_brand) {
                Toast.makeText(this, "Brand clicked", Toast.LENGTH_SHORT).show();
                fragment = new BrandListFragment();
            }else if(id== R.id.nav_category) {
                Toast.makeText(this, "category Clicked", Toast.LENGTH_SHORT).show();
                fragment = new CategoryListFragment();
            }else if (id == R.id.nav_products) {
                Toast.makeText(this, "products clicked", Toast.LENGTH_SHORT).show();
                fragment = new ProductListFragment();
            } else if (id == R.id.nav_inventory) {
                Toast.makeText(this, "Inventory clicked", Toast.LENGTH_SHORT).show();
                fragment = new InventoryFragment();
            }else if(id== R.id.nav_customer) {
                Toast.makeText(this, "Customer Clicked", Toast.LENGTH_SHORT).show();
                fragment = new CustomerFragment();
            }else if (id == R.id.nav_vendor) {
                Toast.makeText(this, "Vendor clicked", Toast.LENGTH_SHORT).show();
                fragment = new VendorFragment();
            } else if (id == R.id.nav_about) {
                Toast.makeText(this, "About clicked", Toast.LENGTH_SHORT).show();
                fragment = new AboutFragment();
            }else if (id == R.id.nav_logout) {
                Toast.makeText(this, "logout clicked", Toast.LENGTH_SHORT).show();
                fragment = new logoutFragment();
            }


            if (fragment != null) {
                loadFragment(fragment);
            }
            drawerLayout.closeDrawers();
            return true;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    public void setToolbarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

}