package com.saveetha.e_book.reviewerscrees;

import static com.saveetha.e_book.Constant.BASE_URL;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.saveetha.e_book.API;
import com.saveetha.e_book.Constant;
import com.saveetha.e_book.RestClient;
import com.saveetha.e_book.SF;
import com.saveetha.e_book.databinding.ActivityReviewerAddBookBinding;
import com.saveetha.e_book.response.CommonResponse;
import com.saveetha.e_book.response.GetCategoryResponse;
import com.saveetha.e_book.response.NewCategoryResponse;
import com.saveetha.e_book.utils.UriToFile;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReviewerAddBookActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    ActivityReviewerAddBookBinding binding;

    private String uploadImageET, titleET, uploadPDFET, authorET, publisherET, yearET, category, priceET;
    private String publisher_name, description;
    private int publisher_id;
    String str[];

    private RequestBody publisher_id_req, title_req, upload_pdf_req, author_req, publisher_name_req, year_req, category_req, price_req, description_req;
    private MultipartBody.Part image_req, pdf_req, demo_req;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityReviewerAddBookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backCard.setOnClickListener(view -> finish());


        SharedPreferences sf = SF.getSignInSF(this);
        publisher_id = sf.getInt(Constant.ID_SI_SF, 0);
        publisher_name = sf.getString(Constant.NAME_SI_SF, null);
        permissionCheck();
        onClick();

//        try{
//
//            loadSpinner();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        loadSpinner();


    }

    private void permissionCheck() {
        if(ContextCompat.checkSelfPermission(ReviewerAddBookActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(ReviewerAddBookActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE));
        }
    }

    private void loadSpinner() {

            Call<NewCategoryResponse> res = RestClient.makeAPI().getAllCategoryForDropDown();

            res.enqueue(new Callback<NewCategoryResponse>() {
                @Override
                public void onResponse(Call<NewCategoryResponse> call, Response<NewCategoryResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus()==200) {

                            try {
                                str = new String[response.body().getData().size()];
                                for (int i = 0; i < response.body().getData().size(); i++) {
                                    GetCategoryResponse.data item = response.body().getData().get(i);
                                    str[i] = item.getCategory_name();
                                }
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(ReviewerAddBookActivity.this, android.R.layout.simple_spinner_item, str);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                binding.category.setAdapter(adapter);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }

                }

                @Override
                public void onFailure(Call<NewCategoryResponse> call, Throwable t) {

                    Toast.makeText(ReviewerAddBookActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

    }

    private void onClick() {

        binding.addBookBtn.setOnClickListener(v -> {
            if (validateData()) {
//                category = binding.category.getText().toString();

                if (changetexttoparts()) {
                    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(60, TimeUnit.SECONDS)
                            .writeTimeout(60, TimeUnit.SECONDS)
                            .readTimeout(60, TimeUnit.SECONDS)
                            .addInterceptor(interceptor)
                            .build();
                    Retrofit ret= new Retrofit.Builder().baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(client).build();
                    API api = ret.create(API.class);
                    Call<CommonResponse> res = api.addBook(publisher_id_req, publisher_name_req, title_req
                            , description_req, author_req, year_req, category_req, price_req, image_req, pdf_req, demo_req);

                    res.enqueue(new Callback<CommonResponse>() {
                        @Override
                        public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                            if (response.isSuccessful()) {
                                if (response.body().getStatus() == 200) {

                                    Toast.makeText(ReviewerAddBookActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    finish();
                                }else{
                                    Toast.makeText(ReviewerAddBookActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            } else if (response.errorBody() != null) {

                                try {
                                    Toast.makeText(ReviewerAddBookActivity.this, "" + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<CommonResponse> call, Throwable t) {

                            Toast.makeText(ReviewerAddBookActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            }
        });

        binding.uploadImageET.setOnClickListener(v -> {

            Intent intent = new Intent();
            intent.setType("*/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
           pickImageLauncher.launch(Intent.createChooser(intent, "Select Picture"));

        });



        binding.uploadPDFET.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(intent.CATEGORY_OPENABLE);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            pickPDFLauncher.launch(Intent.createChooser(intent, "Select PDF"));

        });

        binding.uploadDemoPDF.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(intent.CATEGORY_OPENABLE);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            pickDemoPDFLauncher.launch(Intent.createChooser(intent, "Select Demo PDF"));
        });


    }

    private boolean changetexttoparts() {

        publisher_id_req = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(publisher_id));
        publisher_name_req = RequestBody.create(MediaType.parse("text/plain"), publisher_name);
        title_req = RequestBody.create(MediaType.parse("text/plain"), titleET);
        description_req = RequestBody.create(MediaType.parse("text/plain"), description);
        author_req = RequestBody.create(MediaType.parse("text/plain"), authorET);
        year_req = RequestBody.create(MediaType.parse("text/plain"), yearET);
        category_req = RequestBody.create(MediaType.parse("text/plain"), category);
        price_req = RequestBody.create(MediaType.parse("text/plain"), priceET);

        return true;
    }


    private boolean validateData() {

        boolean isValid = true;

        titleET = binding.titleET.getText().toString();
        authorET = binding.authorET.getText().toString();
        publisherET = binding.publisherET.getText().toString();
        yearET = binding.yearET.getText().toString();
        category = binding.category.getSelectedItem().toString();
        priceET = binding.priceET.getText().toString();
        uploadImageET = binding.uploadImageET.getText().toString();
        uploadPDFET = binding.uploadPDFET.getText().toString();
        description = binding.descriptionET.getText().toString();


        if (uploadImageET.isEmpty()) {
            binding.uploadImageET.setError("Image is required");
            isValid = false;
        }

        if (uploadPDFET.isEmpty()) {
            binding.uploadPDFET.setError("PDF is required");
            isValid = false;
        }


        if (titleET.isEmpty()) {
            binding.titleET.setError("Title is required");
            isValid = false;
        }

        if (authorET.isEmpty()) {
            binding.authorET.setError("Author is required");
            isValid = false;
        }

        if (publisherET.isEmpty()) {
            binding.publisherET.setError("Publisher is required");
            isValid = false;
        }

        if (yearET.isEmpty()) {
            binding.yearET.setError("Year is required");
            isValid = false;
        }


        if (priceET.isEmpty()) {
            binding.priceET.setError("Price is required");
            isValid = false;
        }

        if (category.isEmpty()) {
            Toast.makeText(this, "Choose Category", Toast.LENGTH_SHORT).show();
            isValid = false;
        }


        return isValid;
    }

    private ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    try {
                        File file = UriToFile.uriToFile(imageUri, this);
                        binding.uploadImageET.setText(file.getName());
                        RequestBody requestFile = RequestBody.create(MediaType.parse("*/*"), file);
                        image_req = MultipartBody.Part.createFormData("cover_image", file.getName(), requestFile);
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        binding.imageView.setImageBitmap(bitmap);
                        // Use the bitmap or upload it
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
    );

    private ActivityResultLauncher<Intent> pickPDFLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri PDFUri = result.getData().getData();

                    try {
                        File file = UriToFile.uriToFile(PDFUri, this);
                        binding.uploadPDFET.setText(file.getName());
                        RequestBody requestFile = RequestBody.create(MediaType.parse("*/*"), file);
                        pdf_req = MultipartBody.Part.createFormData("book", file.getName(), requestFile);

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
    );

    private ActivityResultLauncher<Intent> pickDemoPDFLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri demoPDFUri = result.getData().getData();

                    try {
                        File file = UriToFile.uriToFile(demoPDFUri, this);
                        binding.uploadDemoPDF.setText(file.getName());
                        RequestBody requestFile = RequestBody.create(MediaType.parse("*/*"), file);
                        demo_req = MultipartBody.Part.createFormData("demo_file", file.getName(), requestFile);

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
    );

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        } else {
            permissionCheck();
            Toast.makeText(this, "Permission Denied! Give permission to continue", Toast.LENGTH_SHORT).show();
        }
    }
}