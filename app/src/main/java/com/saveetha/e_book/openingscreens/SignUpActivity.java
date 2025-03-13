package com.saveetha.e_book.openingscreens;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.saveetha.e_book.R;
import com.saveetha.e_book.RestClient;
import com.saveetha.e_book.databinding.ActivitySignUpBinding;
import com.saveetha.e_book.request.SignUpRequest;
import com.saveetha.e_book.request.Signin;
import com.saveetha.e_book.response.CommonResponse;
import com.saveetha.e_book.response.SignInResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;

    String name, email, password, confirmPassword, phoneNumber, gender, userType;

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        String[] userTypeAdapter = {"User", "Publisher"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userTypeAdapter);
        binding.userType.setAdapter(adapter);
        binding.userType.setSelection(0);

        binding.signInBtn.setOnClickListener(v -> {
            if(validatesignup()) {
                apiCall(name, email, phoneNumber, userType, gender, password);
            }

        });

    }

    private void apiCall(String name, String email, String phoneNumber, String user_type, String gender, String password) {

        SignUpRequest signUpRequest = new SignUpRequest(
                name,
                phoneNumber,
                email,
                user_type,
                gender, password
        );
        Call<CommonResponse> responseCall = RestClient.makeAPI().signUp(signUpRequest);
        responseCall.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                if(response.isSuccessful()) {

                    if(response.body().getStatus()==200){
                        Toast.makeText(context, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
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

    private boolean validatesignup() {
        boolean isValid = true;

        name = binding.nameET.getText().toString();
        email = binding.emailET.getText().toString();
        password = binding.passwordET.getText().toString();
        confirmPassword = binding.confirmPasswordET.getText().toString();
        phoneNumber = binding.phoneET.getText().toString();
        gender = binding.genderET.getText().toString();

        String userTypeString = binding.userType.getSelectedItem().toString();

        if(name.isEmpty()){
            binding.nameET.setError("Please enter your name");
            isValid = false;
        }

        if(email.isEmpty()){
            binding.emailET.setError("Please enter your email");
            isValid = false;
        }
        if(userTypeString.isEmpty()){
            Toast.makeText(context, "Select User Type", Toast.LENGTH_SHORT).show();
            isValid = false;
        }else {
            if(userTypeString.equalsIgnoreCase("User"))  userType = "100";
            else userType = "110";
        }
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.emailET.setError("Invalid email");
            isValid = false;
        }
        if(password.isEmpty()){
            binding.passwordET.setError("Please enter your password");
            isValid = false;
        }

        if(confirmPassword.isEmpty()){
            binding.confirmPasswordET.setError("Please enter confirm your password");
            isValid = false;
        }

        if(phoneNumber.isEmpty()){
            binding.phoneET.setError("Please enter your phone number");
            isValid = false;
        }

        if(gender.isEmpty()){
            binding.genderET.setError("Please enter your gender");
            isValid = false;
        }

        if(!password.equals(confirmPassword)){
            binding.confirmPasswordET.setError("Password does not match");
            isValid = false;
        }

        return isValid;
    }
}