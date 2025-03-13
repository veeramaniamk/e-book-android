package com.saveetha.e_book.userscreens;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.saveetha.e_book.Constant;
import com.saveetha.e_book.RestClient;
import com.saveetha.e_book.SF;
import com.saveetha.e_book.databinding.ActivityReadBookBinding;
import com.saveetha.e_book.response.CommonResponse;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReadBookActivity extends AppCompatActivity {

    ActivityReadBookBinding binding;
    Context context;
    FragmentActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReadBookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context  = this;
        activity = this;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        binding.pdfView.setSwipeVertical(true);
        binding.pdfView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(context, "Long Click", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        binding.finish.setVisibility(View.GONE);
        Intent intent = getIntent();
        String url = intent.getStringExtra("book");
        String bookId = intent.getStringExtra("bookId");
        String bookUrl = "/books/Anbumani.pdf";
//        String linkUrl = Constant.BASE_URL + "/books/Anbumani.pdf";
        downloadPdf(url);
        Log.e("URL","Read book url before "+ url);
//        if (url != null && !url.isEmpty()) {
//            new DownloadPdf().execute(url); // Use the dynamic URL here
//        } else {
//            Toast.makeText(context, "Invalid book URL", Toast.LENGTH_SHORT).show();
//        }
//        new DownloadPdf().execute(url);

        binding.finish.setOnClickListener(v -> {
            finishCall(Integer.parseInt(SF.getSignInSFValue(activity).get(Constant.ID_SI_SF)), Integer.parseInt(bookId));
        });
    }

    public void downloadPdf(String fileUrl) {

        Call<ResponseBody> call = RestClient.makeAPI().downloadPdf(fileUrl);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    boolean written = writeResponseBodyToDisk(response.body());
                    if (written) {
                        // Load the PDF
                        File pdfFile = new File(getExternalFilesDir(null) + "/MyApp/downloaded_pdf.pdf");
                        if (pdfFile.exists()) {
                            binding.pdfView.fromFile(pdfFile)
                                    .defaultPage(0)
//                                    .enableSwipe(true)
//                                    .swipeHorizontal(false)
                                    .onPageChange((page, pageCount) -> {
                                        if(page == pageCount) {
                                            if(SF.getSignInSFValue(activity).get(Constant.USER_TYPE_SI_SF).equalsIgnoreCase("100")) {
                                                binding.finish.setVisibility(View.VISIBLE);
                                            }
//                                    Toast.makeText(context, "Last Page", Toast.LENGTH_SHORT).show();
                                    } else{
                                        binding.finish.setVisibility(View.GONE);
                                    }
                                    })
                                    .load();
                        }
                    } else {
                        Toast.makeText(context, "Failed to save PDF", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Server returned error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(context, "Download failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("DownloadPdf", "Error: ", t);
            }
        });
    }

    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            File pdfDir = new File(getExternalFilesDir(null), "MyApp");
            if (!pdfDir.exists()) {
                pdfDir.mkdirs();
            }

            File pdfFile = new File(pdfDir, "downloaded_pdf.pdf");

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[10096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(pdfFile);

                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;

                    Log.d("DownloadPdf", "Downloaded " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();
                return true;
            } catch (IOException e) {
                Log.e("DownloadPdf", "Error writing PDF to disk", e);
                return false;
            } finally {
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
            }
        } catch (IOException e) {
            Log.e("DownloadPdf", "Error writing PDF to disk", e);
            return false;
        }
    }

    public class DownloadPdf extends AsyncTask<String, Void, File> {

        @Override
        protected File doInBackground(String... params) {
            String fileUrl = params[0];
//            Toast.makeText(context, "after asynck", Toast.LENGTH_SHORT).show();// PDF URL to download
            Log.e("URL","Read book url async "+ fileUrl);
            String fileName = "downloaded_pdf.pdf"; // Name of the downloaded file

            String name = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
            Log.d("file Name","file name    "+ name);

            File pdfFile = null;
            try {
                URL url = new URL(fileUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);
                urlConnection.connect();

                // Create a directory for the PDF file
                File pdfDir = new File(Environment.getExternalStorageDirectory(), "MyApp");
                if (!pdfDir.exists()) {
                    pdfDir.mkdirs();
                }

                // Create the file in the specified directory
                pdfFile = new File(pdfDir, fileName);

                // Download the file
                InputStream inputStream = new BufferedInputStream(url.openStream(), 10000);
                FileOutputStream outputStream = new FileOutputStream(pdfFile);

                byte[] data = new byte[10000];
                int count;
                while ((count = inputStream.read(data)) != -1) {
                    outputStream.write(data, 0, count);
                }

                outputStream.flush();
                outputStream.close();
                inputStream.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return pdfFile;
        }

        @Override
        protected void onPostExecute(File pdfFile) {
            // Handle the downloaded file here (e.g., open it or use it with your PDF viewer)
            if (pdfFile != null && pdfFile.exists()) {

                binding.pdfView.fromFile(pdfFile).load();

            }else {
                Toast.makeText(context, "File Not Load " , Toast.LENGTH_SHORT).show();
            }
        }
    }

    void finishCall(int userId, int bookId) {
        Call<CommonResponse> responseCall = RestClient.makeAPI().finishBook(bookId, userId);
        responseCall.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                if(response.isSuccessful()) {
                    if(response.body().getStatus() == 200) {
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

    private class DownloadPDFTask extends AsyncTask<String, Void, File> {

        @Override
        protected File doInBackground(String... strings) {
            File pdfFile = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    pdfFile = new File(getCacheDir(), "Anbumani.pdf");

                    OutputStream outputStream = new FileOutputStream(pdfFile);

                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    outputStream.close();
                    inputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return pdfFile;
        }

        @Override
        protected void onPostExecute(File pdfFile) {
            if (pdfFile != null) {
                 binding.pdfView.fromFile(pdfFile).load();
//                         .defaultPage(0).enableSwipe(true)
//                        .onPageChange(new OnPageChangeListener() {
//                            @Override
//                            public void onPageChanged(int page, int pageCount) {
//                                // This method is called when the page is changed
//                                if(page == pageCount) {
//                                    binding.finish.setVisibility(View.VISIBLE);
////                                    Toast.makeText(context, "Last Page", Toast.LENGTH_SHORT).show();
//                                } else{
//                                    binding.finish.setVisibility(View.GONE);
//                                }
//                            }
//                        }).load();
            }else {
                Toast.makeText(context, "File Not Load " , Toast.LENGTH_SHORT).show();
            }
        }
    }
}