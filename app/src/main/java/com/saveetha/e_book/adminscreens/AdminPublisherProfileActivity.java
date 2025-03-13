package com.saveetha.e_book.adminscreens;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.saveetha.e_book.R;
import com.saveetha.e_book.RestClient;
import com.saveetha.e_book.StaticMethods;
import com.saveetha.e_book.databinding.ActivityAdminPublisherProfileBinding;
import com.saveetha.e_book.databinding.CategoriesViewLayoutBinding;
import com.saveetha.e_book.request.Request;
import com.saveetha.e_book.request.Signin;
import com.saveetha.e_book.response.SignInResponse;
import com.saveetha.e_book.response.admin.GetPublisherBookData;
import com.saveetha.e_book.response.admin.GetPublisherBookResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminPublisherProfileActivity extends AppCompatActivity {

    ActivityAdminPublisherProfileBinding binding;
    FragmentActivity context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAdminPublisherProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if(getIntent()==null) return;
        int publisherId = getIntent().getIntExtra("publisherId",0);
        String profile = getIntent().getStringExtra("publisherProfile");
        String publisherName  = getIntent().getStringExtra("publisherName");
        Glide.with(context).load(profile).placeholder(R.drawable.book_icon).error(R.drawable.profile).into(binding.publisherProfile);
//        StaticMethods.setGlide(context,binding.publisherProfile,"https://images.ctfassets.net/h6goo9gw1hh6/2sNZtFAWOdP1lmQ33VwRN3/24e953b920a9cd0ff2e1d587742a2472/1-intro-photo-final.jpg?w=1200&h=992&q=70&fm=webp");
        binding.publisherName.setText(publisherName);
        binding.backCard.setOnClickListener(v -> finish());
        apiCall(publisherId);

    }
    private void apiCall(int publisherId) {
        Request.GetPublisherBook request = new Request.GetPublisherBook(publisherId);
        Call<GetPublisherBookResponse> responseCall = RestClient.makeAPI().getPublisherBook(request);
        responseCall.enqueue(new Callback<GetPublisherBookResponse>() {
            @Override
            public void onResponse(@NonNull Call<GetPublisherBookResponse> call, @NonNull Response<GetPublisherBookResponse> response) {
                if(response.isSuccessful()) {
                    if(response.body().getStatus() ==200) {
                        GetPublisherBookResponse response1= response.body();
                        ArrayList<PublishedBookData> list = new ArrayList<>();
                        for(GetPublisherBookData data: response1.getData()){
                            list.add(new PublishedBookData(data.getBook_title(),data.getBook_cover_image(),data.getBook_id()));
                        }
                        binding.recyclerView.setLayoutManager(new GridLayoutManager(context,3));
                        binding.recyclerView.setAdapter(new AdminAuthorsAdapter(list,context));
                    }else {
                        Toast.makeText(context, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, ""+response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetPublisherBookResponse> call, @NonNull Throwable t) {
                Toast.makeText(context, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Error", t.getMessage());
            }
        });
    }

    class PublishedBookData {
        private String bookTitle;
        private String bookImage;
        private int bookId;

        public PublishedBookData(String bookTitle, String bookImage, int bookId) {
            this.bookTitle = bookTitle;
            this.bookImage = bookImage;
            this.bookId = bookId;
        }

        public String getBookTitle() {
            return bookTitle;
        }

        public String getBookImage() {
            return bookImage;
        }

        public int getBookId() {
            return bookId;
        }
    }

    public class AdminAuthorsAdapter extends RecyclerView.Adapter<AdminAuthorsAdapter.MyViewHolder> {

        ArrayList<PublishedBookData> authors;
        FragmentActivity context;

        public AdminAuthorsAdapter(ArrayList<PublishedBookData> authors, FragmentActivity context) {
            this.authors = authors;
            this.context = context;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            CategoriesViewLayoutBinding binding1 = CategoriesViewLayoutBinding.inflate(context.getLayoutInflater(),parent,false);
            return new MyViewHolder(binding1);

        }

        @Override
        public void onBindViewHolder(@NonNull AdminAuthorsAdapter.MyViewHolder holder, int position) {

            PublishedBookData author = authors.get(position);

            holder.bookTitle.setText(author.getBookTitle());
            Glide.with(context)
                    .load(author.getBookImage())
                    .placeholder(R.drawable.book_icon)
                    .error(R.drawable.book_icon)
                    .into(holder.bookImage);
            holder.cl.setOnClickListener(v -> {
                Intent intent = new Intent(context,AdminBookDetailsActivity.class);
                intent.putExtra("bookId", author.getBookId());
                context.startActivity(intent);
            });

        }

        @Override
        public int getItemCount() {
            return authors.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView bookTitle;
            ShapeableImageView bookImage;
            ConstraintLayout cl;

            public MyViewHolder(@NonNull CategoriesViewLayoutBinding itemView) {
                super(itemView.getRoot());
                bookImage = (ShapeableImageView) itemView.categoryIV;
                cl= itemView.categoryCL;
                bookTitle = itemView.categoryTV;

            }
        }
    }

}