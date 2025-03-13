package com.saveetha.e_book.adminscreens;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.saveetha.e_book.R;
import com.saveetha.e_book.databinding.FragmentAdminRequestBinding;


public class AdminRequestFragment extends Fragment {

    FragmentAdminRequestBinding binding;

    FragmentActivity activity;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAdminRequestBinding.inflate(inflater, container, false);

        try {
            activity = getActivity();
            context  = getContext();
        } catch (Exception e) {
            e.printStackTrace();
        }

        onClickAction();

        return binding.getRoot();
    }

    private void onClickAction() {

        binding.reviews.setOnClickListener(v -> {
            startActivity(new Intent(context,AdminReviewedActivity.class));
        });

        binding.purchasedList.setOnClickListener(v -> {
            startActivity(new Intent(context,AdminPurchasedActivity.class));
        });

        binding.authorsList.setOnClickListener(v -> startActivity(new Intent(context,AdminAuthorsActivity.class)));

    }
}