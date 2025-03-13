package com.saveetha.e_book;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentActivity;

import com.saveetha.e_book.databinding.ActivityDummyPdfviewBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DummyPDFViewActivity extends AppCompatActivity {

    ActivityDummyPdfviewBinding binding;

    FragmentActivity activity;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDummyPdfviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        try {
            activity = this;
            context  = this;
        } catch (Exception e) {
            e.printStackTrace();
        }

//        String pdfUrl = "science_book_original.pdf";
        String pdfUrl = "document/sample.pdf";
        downloadPdf(pdfUrl);
    }

    private void downloadPdf(String url) {
        // Initialize Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://icseindia.org/")
                .build();

        API apiService = retrofit.create(API.class);

        // Download the PDF
        apiService.downloadPdf(url).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    boolean isSaved = savePdfToFile(response.body());
                    if (isSaved) {
                        File pdfFile = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "downloaded.pdf");
                        loadPdf(pdfFile);
                    } else {
                        showToast("Failed to save the PDF file.");
                    }
                } else {
                    showToast("Failed to download the PDF.");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                showToast("Error: " + t.getMessage());
            }
        });
    }

    private boolean savePdfToFile(ResponseBody body) {
        try {
            File pdfFile = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "downloaded.pdf");
            InputStream inputStream = body.byteStream();
            FileOutputStream outputStream = new FileOutputStream(pdfFile);

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void loadPdf(File pdfFile) {
        binding.pdfView.fromFile(pdfFile)
                .enableSwipe(true) // Enable vertical swipe
                .swipeHorizontal(false) // Set to vertical scrolling
                .enableDoubletap(true) // Enable double-tap to zoom
                .defaultPage(0) // Start from the first page
                .enableAntialiasing(true) // Improve rendering quality
                .spacing(10) // Space between pages
                .load();
    }

    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(context, message, Toast.LENGTH_SHORT).show());
    }

}