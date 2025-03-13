package com.saveetha.e_book.userscreens;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.saveetha.e_book.Constant;
import com.saveetha.e_book.RestClient;
import com.saveetha.e_book.SF;
import com.saveetha.e_book.databinding.ActivityChangePasswordBinding;
import com.saveetha.e_book.request.Request;
import com.saveetha.e_book.request.Signin;
import com.saveetha.e_book.response.CommonResponse;
import com.saveetha.e_book.response.SignInResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {


    ActivityChangePasswordBinding binding;

    Context context ;
    FragmentActivity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        try {
            context = this;
            activity = this;
        }catch (Exception e){
            e.printStackTrace();
        }

        binding.backCV.setOnClickListener(v -> finish());

        binding.saveBTN.setOnClickListener(v -> getText());
    }
    private void getText(){
        String oldPassword = binding.currentPasswordET.getText().toString();
        String newPassword = binding.newPasswordET.getText().toString();
        String confirmPassword = binding.reEnterPasswordET.getText().toString();
        if(oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {

            Toast.makeText(context, "Please enter all fields", Toast.LENGTH_SHORT).show();
        }else {
            if(newPassword.equals(confirmPassword)){
                apiCall(oldPassword,newPassword);
            }else {
                Toast.makeText(context, "New password and confirm password does not match", Toast.LENGTH_SHORT).show();
            }
        }
    }
    void apiCall(String currentPassword, String newPassword) {
        String userId = SF.getSignInSFValue(activity).get(Constant.ID_SI_SF);
        String email = SF.getSignInSFValue(activity).get(Constant.EMAIL_SI_SF);
        Request.ChangePassword request = new Request.ChangePassword(userId,email,currentPassword,newPassword);
        Call<CommonResponse> responseCall = RestClient.makeAPI().changePassword(request);
        responseCall.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                if(response.isSuccessful()) {
                    if(response.body().getStatus() ==200) {
                        finish();
                        Toast.makeText(context, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(context, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, ""+response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                Toast.makeText(context, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Error", t.getMessage());
            }
        });
    }
}