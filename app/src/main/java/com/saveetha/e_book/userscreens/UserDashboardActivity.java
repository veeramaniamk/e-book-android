package com.saveetha.e_book.userscreens;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.saveetha.e_book.R;
import com.saveetha.e_book.databinding.ActivityUserDashboardBinding;
import com.saveetha.e_book.userscreens.fragments.HomeFragment;
import com.saveetha.e_book.userscreens.fragments.LibraryFragment;
import com.saveetha.e_book.userscreens.fragments.ProfileFragment;

public class UserDashboardActivity extends AppCompatActivity {

    ActivityUserDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            // Set default fragment
            loadFragment(new HomeFragment());
        }

        binding.bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.nav_library) {
                selectedFragment = new LibraryFragment();
            } else if (item.getItemId() == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }

            return loadFragment(selectedFragment);
        });
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
