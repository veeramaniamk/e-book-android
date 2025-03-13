package com.saveetha.e_book.adminscreens;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.saveetha.e_book.Constant;
import com.saveetha.e_book.R;
import com.saveetha.e_book.databinding.FragmentAdminProfileBinding;
import com.saveetha.e_book.openingscreens.SignInActivity;


public class AdminProfileFragment extends Fragment {

    FragmentAdminProfileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAdminProfileBinding.bind(inflater.inflate(R.layout.fragment_admin_profile, container, false));

        binding.logoutIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sf = requireActivity().getSharedPreferences(Constant.SIGN_IN_SF, Context.MODE_PRIVATE);
                sf.edit().clear().apply();
                startActivity(new Intent(getContext(), SignInActivity.class));
                requireActivity().finish();
            }
        });


        return binding.getRoot();
    }
}