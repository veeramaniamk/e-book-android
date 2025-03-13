package com.saveetha.e_book.userscreens.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.saveetha.e_book.Constant;
import com.saveetha.e_book.FileUtils;
import com.saveetha.e_book.R;
import com.saveetha.e_book.RestClient;
import com.saveetha.e_book.SF;
import com.saveetha.e_book.StaticMethods;
import com.saveetha.e_book.databinding.FragmentProfileBinding;
import com.saveetha.e_book.openingscreens.SignInActivity;
import com.saveetha.e_book.request.Signin;
import com.saveetha.e_book.response.CommonResponse;
import com.saveetha.e_book.response.SignInResponse;
import com.saveetha.e_book.userscreens.ChangePasswordActivity;
import com.saveetha.e_book.userscreens.UpdateUserActivity;

import java.io.File;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;
    Context context;
    FragmentActivity activity;

    private final ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    String path = FileUtils.getPath(context, uri);
                    File file = new File(path);
                    RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
                    MultipartBody.Part body = MultipartBody.Part.createFormData("profile", file.getName(), reqFile);
                    apiCall(SF.getSignInSFValue(activity).get(Constant.ID_SI_SF), body, uri);
                } else {
                    Toast.makeText(getContext(), "No media selected", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(getLayoutInflater());


        try {
            context = requireContext();
            activity = requireActivity();
        } catch (Exception e) {
            e.printStackTrace();
        }
        StaticMethods.setGlide(activity,binding.profileSIV,SF.getSignInSFValue(activity).get(Constant.PROFILE_SI_SF));
        binding.changePasswordBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        binding.cardedit.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), UpdateUserActivity.class);
            startActivity(intent);
        });

        setProfileInfo();

        binding.logoutIV.setOnClickListener(v -> {
            SharedPreferences sf = SF.getSignInSF(activity);
            sf.edit().clear().apply();
            startActivity(new Intent(requireContext(), SignInActivity.class));
            requireActivity().finish();
        });

        binding.profileSIV.setOnClickListener(v -> {
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build());
        });

        return binding.getRoot();
    }

    private void apiCall(String userId, MultipartBody.Part image, Uri uri) {

        Call<CommonResponse> responseCall = RestClient.makeAPI().updateUserProfile(Integer.parseInt(userId), image);
        responseCall.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 200) {
                        binding.profileSIV.setImageURI(uri);
                        SharedPreferences sf = SF.getSignInSF(activity);
                        SharedPreferences.Editor editor = sf.edit();
                        editor.putString(Constant.PROFILE_SI_SF, response.body().getImage());
                        editor.apply();
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Error", t.getMessage());
            }
        });
    }

    private void setProfileInfo() {

        Map<String, String> key = SF.getSignInSFValue(activity);
        binding.userNameET.setText(key.get(Constant.NAME_SI_SF));
        binding.emailET.setText(key.get(Constant.EMAIL_SI_SF));
        binding.phoneET.setText(key.get(Constant.PHONE_SI_SF));
        StaticMethods.setGlide(activity, binding.profileSIV, key.get(Constant.PROFILE_SI_SF));
        binding.genderS.setText(key.get(Constant.GENDER_SI_SF));

    }
}