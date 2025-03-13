package com.saveetha.e_book.userscreens;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentActivity;

import com.saveetha.e_book.Constant;
import com.saveetha.e_book.R;
import com.saveetha.e_book.RestClient;
import com.saveetha.e_book.SF;
import com.saveetha.e_book.StaticMethods;
import com.saveetha.e_book.databinding.ActivityUpdateUserBinding;
import com.saveetha.e_book.openingscreens.SignInActivity;
import com.saveetha.e_book.request.Request;
import com.saveetha.e_book.request.Signin;
import com.saveetha.e_book.response.CommonResponse;
import com.saveetha.e_book.response.SignInResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateUserActivity extends AppCompatActivity {

    ActivityUpdateUserBinding binding;
    Context context;
    FragmentActivity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityUpdateUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        try {
            context  = this;
            activity = this;
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        setProfileInfo();
        binding.backCard.setOnClickListener(v -> finish());
        binding.updateProfileBTN.setOnClickListener(v -> getText());
    }

    private void setProfileInfo(){

        Map<String, String> key = SF.getSignInSFValue(activity);
        binding.userNameET.setText(key.get(Constant.NAME_SI_SF));
        binding.emailET.setText(key.get(Constant.EMAIL_SI_SF));
        binding.phoneET.setText(key.get(Constant.PHONE_SI_SF));
        StaticMethods.setGlide(activity,binding.profileSIV,key.get(Constant.PROFILE_SI_SF));

        binding.genderS.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item,Constant.GENDER));
        if(key.get(Constant.GENDER_SI_SF).equalsIgnoreCase("male"))
        {
            binding.genderS.setSelection(0);
        }else {
            binding.genderS.setSelection(1);
        }

    }
    String gender ;
    private void getText(){
        String name = binding.userNameET.getText().toString();
        String email = binding.emailET.getText().toString();
        String phone = binding.phoneET.getText().toString();
        gender = binding.genderS.getSelectedItem().toString();

        binding.genderS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gender = Constant.GENDER[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if(name.isEmpty() || email.isEmpty() || phone.isEmpty() || gender.isEmpty()){
            Log.d("tag",name+" "+email+" "+phone+" gender "+gender);
            Toast.makeText(context, "File all the values", Toast.LENGTH_SHORT).show();
        } else {
            apiCall(name,email,phone,gender);
        }


    }

    private void apiCall(String name, String email, String phone, String gender) {
        Map<String, String> key = SF.getSignInSFValue(activity);
        String userId = key.get(Constant.ID_SI_SF);
        Request.UpdateProfile request = new Request.UpdateProfile(
                name,phone,email,gender,userId
        );
        Call<CommonResponse> responseCall = RestClient.makeAPI().updateUserInfo(request);
        responseCall.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                if(response.isSuccessful()) {
                    if(response.body().getStatus() == 200) {
                        Toast.makeText(context, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        SharedPreferences sf = SF.getSignInSF(activity);
                        SharedPreferences.Editor editor = sf.edit();
                        editor.putString(Constant.NAME_SI_SF, name);
                        editor.putString(Constant.EMAIL_SI_SF, email);
                        editor.putLong(Constant.PHONE_SI_SF, Long.parseLong(phone));
                        editor.putString(Constant.GENDER_SI_SF, gender);
                        editor.apply();
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