package com.saveetha.e_book.adminscreens;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.saveetha.e_book.R;
import com.saveetha.e_book.databinding.FragmentAdminHomeBinding;


public class AdminHomeFragment extends Fragment {

    FragmentAdminHomeBinding binding;

    FragmentActivity activity;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAdminHomeBinding.inflate(inflater, container, false);

        try {
            activity = requireActivity();
            context = requireContext();

        } catch (Exception e) {
            e.printStackTrace();
        }

        setUpTab();

        return binding.getRoot();
    }

    private void setUpTab() {
        binding.tabLayout.setTabMode(TabLayout.MODE_FIXED);
        binding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        for (int i = 0; i < binding.tabLayout.getTabCount(); i++) {
            View tab = ((ViewGroup) binding.tabLayout.getChildAt(0)).getChildAt(i);
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
            layoutParams.setMargins(16, 0, 50, 0); // Add margins (left, top, right, bottom)
            tab.requestLayout();
        }

        binding.viewPager.setAdapter(new ViewPagerAdapter(activity));


        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {

//            View view = getLayoutInflater().inflate(R.layout.tab_custom, null);
//            TextView tabText = view.findViewById(R.id.tabText);

            switch (position) {
                case 0:
                    tab.setText("New");
                    break;
                case 1:
                    tab.setText("Approved");
                    break;
                case 2:
                    tab.setText("Rejected");
                    break;
            }

//            tab.setCustomView(view);

        }).attach();

    }

    private class ViewPagerAdapter extends FragmentStateAdapter {
           public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new AdminNewFragment();
                case 1:
                    return new AdminApprovedFragment();
                case 2:
                    return new AdminRejectedFragment();
                default:
                    return new AdminNewFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }
}