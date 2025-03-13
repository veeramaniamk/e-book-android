package com.saveetha.e_book.reviewerscrees;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.saveetha.e_book.Constant;
import com.saveetha.e_book.RestClient;
import com.saveetha.e_book.databinding.FragmentReviewerHomeBinding;
import com.saveetha.e_book.response.GetCategoryResponse;
import com.saveetha.e_book.response.admin.GetBooksData;
import com.saveetha.e_book.response.admin.GetBooksResponse;
import com.saveetha.e_book.reviewerscrees.revieweradapter.ReviewerHomeBooksAdapter;
import com.saveetha.e_book.reviewerscrees.reviewerapi.request.RequestGetBook;
import com.saveetha.e_book.reviewerscrees.reviewermodules.ReviewerBooksModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReviewerHomeFragment extends Fragment {

    FragmentReviewerHomeBinding binding;
    private ArrayList<ReviewerBooksModule> list;
    FragmentActivity activity;
    Context context;
    private String chipText;
    ReviewerHomeBooksAdapter adapter;
    String publisherName;
    int publisherId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentReviewerHomeBinding.inflate(inflater, container, false);

        try {
            activity = getActivity();
            context = getContext();
        } catch (Exception e) {
            e.printStackTrace();
        }

        SharedPreferences sharedPreferences = activity.getSharedPreferences(Constant.SIGN_IN_SF, Context.MODE_PRIVATE);
        publisherId = sharedPreferences.getInt(Constant.ID_SI_SF, 0);
        publisherName = sharedPreferences.getString(Constant.NAME_SI_SF, "");

        onChipListener();

        loadReviewerHome();

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                filter(newText);
                return false;
            }
        });

        return binding.getRoot();


    }

    private void chipFilter(String status) {
        ArrayList<ReviewerBooksModule> filteredlist = new ArrayList<>();
        if(Objects.equals(status, "ALL")){
            for (ReviewerBooksModule item : list) {
                  filteredlist.add(item);
            }
        } else {


            for (ReviewerBooksModule item : list) {
                if (item.getStatus().toLowerCase().contains(status.toLowerCase())) {
                    filteredlist.add(item);
                }
            }

        }

        if (filteredlist.isEmpty()) {
            Toast.makeText(context, "No Data Found..", Toast.LENGTH_SHORT).show();
            try {
                ArrayList<ReviewerBooksModule> list1 = new ArrayList<>();
                adapter.filterList(list1);
            }catch (Exception e){
                e.printStackTrace();
            }
        } else {
            adapter.filterList(filteredlist);
        }

    }

    private void filter(String newText) {
        ArrayList<ReviewerBooksModule> filteredlist = new ArrayList<>();

        for (ReviewerBooksModule item : list) {
            if (item.getBookName().toLowerCase().contains(newText.toLowerCase())) {
                filteredlist.add(item);
            }
        }

        if (filteredlist.isEmpty()) {
            Toast.makeText(context, "No Data Found..", Toast.LENGTH_SHORT).show();
            try {
                ArrayList<ReviewerBooksModule> list1 = new ArrayList<>();
                adapter.filterList(list1);
            }catch (Exception e){
                e.printStackTrace();
            }

        } else {
            adapter.filterList(filteredlist);
        }

    }

    private void onChipListener() {

        binding.chipGroup.setOnCheckedChangeListener((group, checkedId) -> {

            if (checkedId != -1) {
                Chip selectedChip = binding.chipGroup.findViewById(checkedId);
                chipText = selectedChip.getText().toString();
                chipFilter(chipText);
            } else {
                chipText = "ALL";
            }

        });

    }


    private void loadReviewerHome() {

        list = new ArrayList<>();

        RequestGetBook requestGetBook = new RequestGetBook();
        requestGetBook.setPublisher_id(publisherId);


        Call<GetBooksResponse> call = RestClient.makeAPI().getReviewerBooks(requestGetBook);

        call.enqueue(new Callback<GetBooksResponse>() {
            @Override
            public void onResponse(Call<GetBooksResponse> call, Response<GetBooksResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 200) {
                        try {
                            for (GetBooksData r : response.body().getData()) {
                                list.add(new ReviewerBooksModule(r.getBook_id(),r.getBook_title(), r.getBook_cover_image(), r.getBook_description(),r.getBook_approval_status()));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                        adapter = new ReviewerHomeBooksAdapter(list, context);
                        binding.recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        binding.recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onFailure(Call<GetBooksResponse> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}