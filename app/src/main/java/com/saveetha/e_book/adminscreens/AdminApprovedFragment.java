package com.saveetha.e_book.adminscreens;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.saveetha.e_book.R;
import com.saveetha.e_book.RestClient;
import com.saveetha.e_book.adminscreens.adminadapters.AdminHomeBooksAdapter;
import com.saveetha.e_book.adminscreens.adminmodules.AdminBooksModule;
import com.saveetha.e_book.databinding.FragmentAdminApprovedBinding;
import com.saveetha.e_book.response.admin.GetBooksData;
import com.saveetha.e_book.response.admin.GetBooksResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AdminApprovedFragment extends Fragment {

    FragmentAdminApprovedBinding binding;
    FragmentActivity activity;
    Context context;

    List<AdminBooksModule> adminBooksModuleList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAdminApprovedBinding.inflate(inflater, container, false);

        try {
            activity = getActivity();
            context = getContext();
        } catch (Exception e) {
            e.printStackTrace();
        }

        apiCall("APPROVED");

        return binding.getRoot();
    }

    private void apiCall(String status) {
        Call<GetBooksResponse> responseCall = RestClient.makeAPI().getBooksAdminHomePage(status);
        responseCall.enqueue(new Callback<GetBooksResponse>() {
            @Override
            public void onResponse(@NonNull Call<GetBooksResponse> call, @NonNull Response<GetBooksResponse> response) {
                if(response.isSuccessful()) {
                    GetBooksResponse getBooksResponse = response.body();
                    List<GetBooksData> data = getBooksResponse.getData();
                    if(data==null){
                        Toast.makeText(context, ""+getBooksResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(getBooksResponse.getStatus() == 200) {
                        for(GetBooksData data1 : data) {
                            adminBooksModuleList.add(new AdminBooksModule(data1.getBook_id(),data1.getBook_title(),data1.getBook_cover_image(),data1.getBook_description(),data1.getBook_approval_status()));
                        }
                        AdminHomeBooksAdapter adminHomeBooksAdapter = new AdminHomeBooksAdapter(adminBooksModuleList,context);

                        binding.recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        binding.recyclerView.setAdapter(adminHomeBooksAdapter);
                    }else {
                        Toast.makeText(context, ""+getBooksResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(context, ""+response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetBooksResponse> call, @NonNull Throwable t) {
                Toast.makeText(context, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Error", t.getMessage());
            }
        });
    }
}