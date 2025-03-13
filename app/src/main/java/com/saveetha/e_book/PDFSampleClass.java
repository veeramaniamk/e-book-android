package com.saveetha.e_book;

import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.saveetha.e_book.databinding.ActivityBookViewBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PDFSampleClass extends AppCompatActivity {
    private OkHttpClient client;
    private PdfRenderer pdfRenderer;

    private ParcelFileDescriptor fileDescriptor;
    private PdfRendererAdapter adapter;

    void mainMethod(){
        client = new OkHttpClient();

        String pdfUrl = "https://93e7-2409-4072-6ecd-f350-345e-43fc-b3e5-2854.ngrok-free.app/books/1724164922548-tempfile.pdf";  // Replace with your PDF URL
        downloadAndDisplayPdf(pdfUrl);
    }

    private void openPdfFile(File file) {
        try {
            fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
            pdfRenderer = new PdfRenderer(fileDescriptor);

            // Pass the renderer to the adapter
            adapter = new PdfRendererAdapter(pdfRenderer);
//            runOnUiThread(() -> binding.pdfRecyclerView.setAdapter(adapter));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void downloadAndDisplayPdf(String pdfUrl) {
        Request request = new Request.Builder().url(pdfUrl).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    return;
                }

                // Save PDF to file
                File pdfFile = new File(getCacheDir(), "temp.pdf");
                try (InputStream inputStream = response.body().byteStream();
                     FileOutputStream outputStream = new FileOutputStream(pdfFile)) {
                    byte[] buffer = new byte[2048];
                    int length;
                    while ((length = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, length);
                    }
                }

                // Open and render PDF
                openPdfFile(pdfFile);
            }
        });
    }
    class PdfRendererAdapter extends RecyclerView.Adapter<PdfRendererAdapter.PageViewHolder> {

        private final PdfRenderer pdfRenderer;

        public PdfRendererAdapter(PdfRenderer pdfRenderer) {
            this.pdfRenderer = pdfRenderer;
        }

        @NonNull
        @Override
        public PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.pdf_page_item, parent, false);
            return new PageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PageViewHolder holder, int position) {
            PdfRenderer.Page page = pdfRenderer.openPage(position);

            Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            holder.imageView.setImageBitmap(bitmap);

            page.close();
        }

        @Override
        public int getItemCount() {
            return pdfRenderer.getPageCount();
        }

        class PageViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            PageViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.pdf_page_image);
            }
        }
    }

}
