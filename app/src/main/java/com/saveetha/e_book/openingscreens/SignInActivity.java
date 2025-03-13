package com.saveetha.e_book.openingscreens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.saveetha.e_book.Constant;
import com.saveetha.e_book.R;
import com.saveetha.e_book.RestClient;
import com.saveetha.e_book.SF;
import com.saveetha.e_book.adminscreens.AdminDashboardActivity;
import com.saveetha.e_book.databinding.ActivitySignInBinding;
import com.saveetha.e_book.request.Signin;
import com.saveetha.e_book.response.SignInData;
import com.saveetha.e_book.response.SignInResponse;
import com.saveetha.e_book.reviewerscrees.ReviewerDashboardActivity;
import com.saveetha.e_book.userscreens.UserDashboardActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    ActivitySignInBinding binding;
    String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.signInBtn.setOnClickListener(v -> {
            if(validateuser()) {
                apiCall(email, password);
            }
        });

        binding.registerTV.setOnClickListener(v -> {
            startActivity(new Intent(this, SignUpActivity.class));
        });

    }

    private void apiCall(String email, String password) {

        Signin signInRequest = new Signin();
        signInRequest.setEmail(email);
        signInRequest.setPassword(password);
        Call<SignInResponse> responseCall = RestClient.makeAPI().signIn(signInRequest);
        responseCall.enqueue(new Callback<SignInResponse>() {
            @Override
            public void onResponse(@NonNull Call<SignInResponse> call, @NonNull Response<SignInResponse> response) {
                if(response.isSuccessful()) {

                    SignInResponse signInResponse = response.body();
                    SignInData data = signInResponse.getData();

                    if(response.body().getStatus()==200) {

                        Toast.makeText(SignInActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        SharedPreferences sf = SF.getSignInSF(SignInActivity.this);
                        SharedPreferences.Editor editor = sf.edit();
                        editor.putInt(Constant.ID_SI_SF, data.getId());
                        editor.putString(Constant.NAME_SI_SF, data.getName());
                        editor.putString(Constant.EMAIL_SI_SF, data.getEmail());
                        editor.putInt(Constant.USER_TYPE_SI_SF, data.getUserType());
                        editor.putLong(Constant.PHONE_SI_SF, data.getPhone());
                        editor.putString(Constant.PROFILE_SI_SF, data.getProfile());
                        editor.putString(Constant.GENDER_SI_SF, data.getGender());
                        editor.apply();


                        if(data.getUserType()==100) {
                            startActivity(new Intent(SignInActivity.this, UserDashboardActivity.class));
                        }else if(data.getUserType()==110) {
                            startActivity(new Intent(SignInActivity.this, ReviewerDashboardActivity.class));
                        }else if(data.getUserType()==111){
                            startActivity(new Intent(SignInActivity.this, AdminDashboardActivity.class));
                        }
                        finish();
                    }else{
                        Toast.makeText(SignInActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(SignInActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SignInResponse> call, @NonNull Throwable t) {
                Log.e("Error", t.getMessage());
                Toast.makeText(SignInActivity.this, "Error "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean validateuser() {
        boolean isValid = true;

        email    = binding.emailET.getText().toString();
        password = binding.passwordET.getText().toString();

        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.emailET.setError("Invalid email");
            isValid = false;
        }
        if(email.isEmpty()) {
            binding.emailET.setError("Email is required");
            isValid = false;
        }
        if(password.isEmpty()) {
            binding.passwordET.setError("Password is required");
            isValid = false;
        }
        return isValid;
    }
}