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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.saveetha.e_book.R;
import com.saveetha.e_book.RestClient;
import com.saveetha.e_book.adminscreens.adminmodules.BookReviewsModel;
import com.saveetha.e_book.databinding.ActivityAdminReviewedBinding;
import com.saveetha.e_book.request.Signin;
import com.saveetha.e_book.response.SignInResponse;
import com.saveetha.e_book.response.admin.GetAllReviewResponse;
import com.saveetha.e_book.response.admin.GetBooksData;
import com.saveetha.e_book.response.admin.GetReviewData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminReviewedActivity extends AppCompatActivity {

    ActivityAdminReviewedBinding binding;
    ArrayList<BookReviewsModel> adminBooksModuleList;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityAdminReviewedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        apiCall();

        binding.backCard.setOnClickListener(v -> finish());

    }

    private void apiCall() {
        Call<GetAllReviewResponse> responseCall = RestClient.makeAPI().getAllReview();
        responseCall.enqueue(new Callback<GetAllReviewResponse>() {
            @Override
            public void onResponse(@NonNull Call<GetAllReviewResponse> call, @NonNull Response<GetAllReviewResponse> response) {
                if(response.isSuccessful()) {
                    if(response.body().getStatus() ==200) {
                        adminBooksModuleList = new ArrayList<>();
                        GetAllReviewResponse getAllReviewResponse = response.body();
                        for(GetReviewData data : getAllReviewResponse.getData()){
                            adminBooksModuleList.add(new BookReviewsModel(data.getBook_id(),data.getName(),data.getProfile(),data.getReview_text(),data.getUser_id()));
                        }
                        AdminManageBooksAdapter adapter = new AdminManageBooksAdapter(context,adminBooksModuleList);

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
            public void onFailure(@NonNull Call<GetAllReviewResponse> call, @NonNull Throwable t) {
                Toast.makeText(context, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Error", t.getMessage());
            }
        });
    }


    public class AdminManageBooksAdapter extends RecyclerView.Adapter<AdminManageBooksAdapter.MyViewHolder> {

        List<BookReviewsModel> reviewBookList;
        Context context;

        public AdminManageBooksAdapter(Context context, List<BookReviewsModel> adminBooksModuleList) {
            this.reviewBookList = adminBooksModuleList;
            this.context = context;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_review_layout, parent, false);

            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            BookReviewsModel adminBooksModule = reviewBookList.get(position);

            Glide.with(context)
                    .load(adminBooksModule.getUserProfile())
                    .placeholder(R.drawable.book_icon)
                    .error(R.drawable.book_icon)
                    .into(holder.imageUrl);

            holder.userName.setText(adminBooksModule.getUserName());
            holder.reviewText.setText(adminBooksModule.getReviewText());
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
            return reviewBookList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView userName, reviewText;
            ShapeableImageView imageUrl;
            CardView cardView;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                imageUrl = itemView.findViewById(R.id.userImage);
                reviewText = itemView.findViewById(R.id.reviewText);
                userName = itemView.findViewById(R.id.userName);
                cardView = itemView.findViewById(R.id.cardView);
            }
        }
    }
}