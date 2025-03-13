package com.saveetha.e_book.userscreens.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.saveetha.e_book.R;
import com.saveetha.e_book.databinding.FragmentLibraryBinding;
import com.saveetha.e_book.userscreens.BooksListActivity;
import com.saveetha.e_book.userscreens.SavedAndFinishedActivity;

public class LibraryFragment extends Fragment {

    FragmentLibraryBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLibraryBinding.inflate(getLayoutInflater());

        binding.savedTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), SavedAndFinishedActivity.class);
                intent.putExtra("title","Saved List");
                startActivity(intent);
            }
        });

        binding.finishedTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), SavedAndFinishedActivity.class);
                intent.putExtra("title","Finished List");
                startActivity(intent);
            }
        });

        binding.myBooks.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), SavedAndFinishedActivity.class);
            intent.putExtra("title","My Books");
            startActivity(intent);
        });

        return binding.getRoot();
    }
}