package com.saveetha.e_book.adminscreens;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.saveetha.e_book.R;
import com.saveetha.e_book.RestClient;
import com.saveetha.e_book.adminscreens.adminadapters.AdminHomeBooksAdapter;
import com.saveetha.e_book.adminscreens.adminadapters.AdminManageBooksAdapter;
import com.saveetha.e_book.adminscreens.adminmodules.AdminBooksModule;
import com.saveetha.e_book.adminscreens.adminmodules.BookReviewsModel;
import com.saveetha.e_book.databinding.ActivityAdminPurchasedBinding;
import com.saveetha.e_book.response.admin.GetAllReviewResponse;
import com.saveetha.e_book.response.admin.GetPurchasedBooksData;
import com.saveetha.e_book.response.admin.GetPurchesedBooksResponse;
import com.saveetha.e_book.response.admin.GetReviewData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminPurchasedActivity extends AppCompatActivity {

    ActivityAdminPurchasedBinding binding;
    ArrayList<AdminBooksModule> adminBooksModuleList;
    Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityAdminPurchasedBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        binding.backCard.setOnClickListener(v -> finish());
        apiCall();

    }

    private void apiCall() {
        Call<GetPurchesedBooksResponse> responseCall = RestClient.makeAPI().getPurchasedBooks();
        responseCall.enqueue(new Callback<GetPurchesedBooksResponse>() {
            @Override
            public void onResponse(@NonNull Call<GetPurchesedBooksResponse> call, @NonNull Response<GetPurchesedBooksResponse> response) {
                if(response.isSuccessful()) {
                    if(response.body().getStatus() ==200) {
                        GetPurchesedBooksResponse reviewResponse = response.body();
                        List<PurchasedBookData> list = new ArrayList<>();
                        for(GetPurchasedBooksData data : reviewResponse.getData()) {
                            list.add(new PurchasedBookData(data.getBook_id(),data.getBook_description(),data.getBook_cover_image(),data.getBook_price(),data.getBook_titile()));
                        }
                        AdminManageBooksAdapter adapter = new AdminManageBooksAdapter(list,context);
                        binding.recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        binding.recyclerView.setAdapter(adapter);
                    }else {
                        Toast.makeText(context, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, ""+response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetPurchesedBooksResponse> call, @NonNull Throwable t) {
                Toast.makeText(context, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Error", t.getMessage());
            }
        });
    }

    private class PurchasedBookData {
        private int bookId;
        private String bookDescription;
        private String coverImage;
        private double price;
        private String bookTitle;

        public int getBookId() {
            return bookId;
        }

        public String getBookDescription() {
            return bookDescription;
        }

        public String getCoverImage() {
            return coverImage;
        }

        public double getPrice() {
            return price;
        }

        public String getBookTitle() {
            return bookTitle;
        }

        public PurchasedBookData(int bookId, String bookDescription, String coverImage, double price, String bookTitle) {
            this.bookId = bookId;
            this.bookDescription = bookDescription;
            this.coverImage = coverImage;
            this.price = price;
            this.bookTitle = bookTitle;
        }
    }


    public class AdminManageBooksAdapter extends RecyclerView.Adapter<AdminManageBooksAdapter.MyViewHolder> {

        List<PurchasedBookData> adminBooksModuleList;
        Context context;

        public AdminManageBooksAdapter(List<PurchasedBookData> adminBooksModuleList, Context context) {
            this.adminBooksModuleList = adminBooksModuleList;
            this.context = context;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_new_books_layout, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            PurchasedBookData adminBooksModule = adminBooksModuleList.get(position);

            Glide.with(context)
                    .load(adminBooksModule.getCoverImage())
                    .placeholder(R.drawable.book_icon)
                    .error(R.drawable.book_icon)
                    .into(holder.imageUrl);

            holder.bookName.setText(adminBooksModule.getBookTitle());
            holder.descrption.setText(adminBooksModule.getBookDescription());
            holder.price.setText("₹" + adminBooksModule.getPrice());
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AdminBookDetailsActivity.class);
                    intent.putExtra("bookId", adminBooksModule.getBookId());
                    context.startActivity(intent);

                }
            });

        }

        @Override
        public int getItemCount() {
            return adminBooksModuleList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView bookName, descrption,price;
            ShapeableImageView imageUrl;
            CardView cardView;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                imageUrl = itemView.findViewById(R.id.imageBook);
                bookName = itemView.findViewById(R.id.title);
                descrption = itemView.findViewById(R.id.descriptionText);
                price = itemView.findViewById(R.id.price);

                cardView = itemView.findViewById(R.id.cardView);
            }
        }
    }
}