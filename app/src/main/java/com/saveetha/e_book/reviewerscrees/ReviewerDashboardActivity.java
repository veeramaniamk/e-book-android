package com.saveetha.e_book.reviewerscrees;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.saveetha.e_book.R;
import com.saveetha.e_book.databinding.ActivityReviewerDashboardBinding;
import com.saveetha.e_book.userscreens.fragments.ProfileFragment;

public class ReviewerDashboardActivity extends AppCompatActivity {

    ActivityReviewerDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityReviewerDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupNavBottom();
    }

    private void setupNavBottom() {

        getSupportFragmentManager().beginTransaction().replace(R.id.reviewerContainer, new ReviewerHomeFragment()).commit();

        binding.reviewerBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                int id = item.getItemId();

                if (id == R.id.home) {
                    selectedFragment = new ReviewerHomeFragment();
                    replaceFragment(selectedFragment);
                }
                if (id == R.id.add) {

                    try {
                        showPopupWindow(binding.reviewerBottomNav);
                    } catch (Exception e) {
                        Toast.makeText(ReviewerDashboardActivity.this, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                if (id == R.id.settings) {
                    selectedFragment = new ProfileFragment();
                    replaceFragment(selectedFragment);
                }

                return true;
            }
        });

    }

    private void replaceFragment(Fragment selectedFragment) {

        getSupportFragmentManager().beginTransaction().replace(R.id.reviewerContainer, selectedFragment).commit();

    }

    private void showPopupWindow(View anchorView) {
        // Inflate the custom layout
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.reviewer_popup_menu_layout, null);

        // Create the PopupWindow
        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        // Set up the custom layout views
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button addCategory = popupView.findViewById(R.id.add_category);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button addBook = popupView.findViewById(R.id.add_book);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button viewCategory = popupView.findViewById(R.id.view_category);

        addCategory.setOnClickListener(v -> {
            startActivity(new Intent(this, ReviewerAddCategoryActivity.class));
            popupWindow.dismiss();
        });

        addBook.setOnClickListener(v -> {
            startActivity(new Intent(this, ReviewerAddBookActivity.class));
            popupWindow.dismiss();
        });

        viewCategory.setOnClickListener(v -> {
            startActivity(new Intent(this, ViewCategoryActivity.class));
            popupWindow.dismiss();
        });

        popupWindow.showAtLocation(anchorView, Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 230); // Adjust gravity and position as needed

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}