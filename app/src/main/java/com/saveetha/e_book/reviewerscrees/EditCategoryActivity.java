package com.saveetha.e_book.reviewerscrees;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentActivity;

import com.saveetha.e_book.R;
import com.saveetha.e_book.RestClient;
import com.saveetha.e_book.StaticMethods;
import com.saveetha.e_book.databinding.ActivityEditCategoryBinding;
import com.saveetha.e_book.response.CommonResponse;
import com.saveetha.e_book.utils.UriToFile;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditCategoryActivity extends AppCompatActivity {

    ActivityEditCategoryBinding binding;
    Context context;
    FragmentActivity activity;
    String categoryName;
    RequestBody categoryBody;
    String categoryId;
    MultipartBody.Part categoryImagePart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityEditCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context = this;
        activity = this;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        Intent intent = getIntent();
        String category      = intent.getStringExtra("CATEGORY_NAME");
        String categoryImage = intent.getStringExtra("CATEGORY_IMAGE");
        categoryId    = intent.getStringExtra("CATEGORY_ID");
        binding.categoryNameET.setText(category);
        StaticMethods.setGlide(activity,binding.uploadImageView, categoryImage);

        binding.addCategoryBtn.setOnClickListener(v -> {
            addCategory();
        });

        binding.backCard.setOnClickListener(view -> finish());

        binding.uploadImageView.setOnClickListener(v -> {
            Intent intent1 = new Intent(Intent.ACTION_PICK);
            intent1.setType("*/*");
            intent1.setAction(Intent.ACTION_GET_CONTENT);
            pickImage.launch(Intent.createChooser(intent1, "Select Picture"));
        });

        binding.deleteCategoryBtn.setOnClickListener(v -> deleteCategory(categoryId));

    }
    private void deleteCategory(String id) {
        Call<CommonResponse> res = RestClient.makeAPI().deleteCategory(id);

        res.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response.isSuccessful()) {
                    if(response.body().getStatus()==200){
                        Toast.makeText(EditCategoryActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(EditCategoryActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else if(response.errorBody()!=null){
                    try {
                        Toast.makeText(EditCategoryActivity.this, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(EditCategoryActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("TAG", "onResponse: "+e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                Toast.makeText(EditCategoryActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void addCategory() {
        if (getData()) {

            Call<CommonResponse> res = RestClient.makeAPI().updateCategory(categoryBody, RequestBody.create(MediaType.parse("text/plain"), categoryId), categoryImagePart);

            res.enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getStatus()==200){
                            Toast.makeText(EditCategoryActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(EditCategoryActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else if(response.errorBody()!=null){
                        try {
                            Toast.makeText(EditCategoryActivity.this, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(EditCategoryActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("TAG", "onResponse: "+e.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t) {
                    Toast.makeText(EditCategoryActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }


    private boolean getData() {
        boolean isValid = true;

        categoryName = binding.categoryNameET.getText().toString();

        if (categoryName.isEmpty()) {
            isValid = false;
        }

        categoryBody = RequestBody.create(MediaType.parse("text/plain"), categoryName);

        return isValid;
    }

    private ActivityResultLauncher<Intent> pickImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

        if (result.getResultCode() == RESULT_OK && result.getData() != null) {

            Uri imageUri = result.getData().getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                binding.uploadImageView.setImageBitmap(bitmap);
                File file = UriToFile.uriToFile(imageUri, this);

                RequestBody requestFile = RequestBody.create(MediaType.parse("*/*"), file);
                categoryImagePart = MultipartBody.Part.createFormData("category_image", file.getName(), requestFile);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }

    });


}