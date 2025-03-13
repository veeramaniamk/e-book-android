package com.saveetha.e_book;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.saveetha.e_book.databinding.ActivityAddReviewBinding;
import com.saveetha.e_book.request.Request;
import com.saveetha.e_book.response.ReviewResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddReviewActivity extends AppCompatActivity {

    List<ReviewModule> reviewModules = new ArrayList<>();

    ActivityAddReviewBinding binding;

    private String  book_id, book_publisher_id;
    private int user_id;
    private String reviewText;
    ReviewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddReviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String userId = SF.getSignInSFValue(this).get(Constant.ID_SI_SF);
        if (userId != null) {
            this.user_id = Integer.parseInt(userId);
        } else {
            Toast.makeText(this, "Error in getting user id", Toast.LENGTH_SHORT).show();
        }

        binding.backCV.setOnClickListener(v -> finish());
        if (getIntent() != null) {
            book_id = getIntent().getStringExtra("book_id");
            book_publisher_id = getIntent().getStringExtra("book_publisher_id");

        } else {
            Toast.makeText(this, "Error in getting data", Toast.LENGTH_SHORT).show();
        }

        if(SF.getSignInSFValue(this).get(Constant.USER_TYPE_SI_SF).equalsIgnoreCase("100")) {
            binding.addReviewBtn.setVisibility(View.VISIBLE);
        }else{
            binding.addReviewBtn.setVisibility(View.GONE);
        }

        onClick();
        loadData();

    }

    private void loadData() {

        Request.GetBookReview request = new Request.GetBookReview(book_id);
        Call<ReviewResponse> res = RestClient.makeAPI().getBookReview(request);

        res.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                if (response.isSuccessful()) {
//                    Toast.makeText(AddReviewActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    if (response.body().getStatus().equals("200")) {

//                        Toast.makeText(AddReviewActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        List<ReviewResponse.Review> reviews = response.body().getData();
                        if (!reviews.isEmpty()) {

                            for (ReviewResponse.Review review : reviews) {
                                reviewModules.add(new ReviewModule(review.getProfile(), review.getUser_name(), review.getReview_text(), review.getDate()));
                            }
                            Toast.makeText(AddReviewActivity.this, ""+reviews.size(), Toast.LENGTH_SHORT).show();
                            adapter = new ReviewAdapter(reviewModules, AddReviewActivity.this);
                            binding.reviewsRV.setLayoutManager(new LinearLayoutManager(AddReviewActivity.this));
                            binding.reviewsRV.setAdapter(adapter);

                        } else {
                            Toast.makeText(AddReviewActivity.this, "no reviews found", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(AddReviewActivity.this, "something went wrong" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                Toast.makeText(AddReviewActivity.this, "Check Your Internet Connection" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void onClick() {
        binding.addReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPopupWindow(v);
            }
        });
    }

    private void showPopupWindow(View anchorView) {
        // Inflate the custom layout
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.add_review_layout, null);

        // Create the PopupWindow
        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        EditText review = popupView.findViewById(R.id.reviewET);
        FloatingActionButton send = popupView.findViewById(R.id.sendBtn);


        send.setOnClickListener(v -> {
            String reviewText = review.getText().toString();
            if (reviewText.isEmpty()) {
                Toast.makeText(this, "write something!", Toast.LENGTH_SHORT).show();
            } else {
                if (sendReview(review)) {
                    popupWindow.dismiss();
                }
            }
        });

        popupWindow.showAtLocation(anchorView, Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 1000); // Adjust gravity and position as needed

    }

    private boolean sendReview(EditText review) {

        final boolean[] isAdded = {true};
        reviewText = review.getText().toString();

        if (reviewText.isEmpty()) {
            Toast.makeText(this, "write something!", Toast.LENGTH_SHORT).show();
            isAdded[0] = false;
        }

        Request.SendReview request = new Request.SendReview(book_id, user_id, book_publisher_id, reviewText);

        Call<ReviewResponse> res = RestClient.makeAPI().sendReview(request);

        res.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("200")) {
                        Toast.makeText(AddReviewActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(AddReviewActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddReviewActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {

                isAdded[0] = false;
                Toast.makeText(AddReviewActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        return isAdded[0];
    }

    @Override
    protected void onResume() {
        super.onResume();
//        loadData();
    }
}