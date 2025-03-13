package com.saveetha.e_book.userscreens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.saveetha.e_book.AddReviewActivity;
import com.saveetha.e_book.Constant;
import com.saveetha.e_book.R;
import com.saveetha.e_book.RestClient;
import com.saveetha.e_book.SF;
import com.saveetha.e_book.databinding.ActivityBookDetailsBinding;
import com.saveetha.e_book.request.Request;
import com.saveetha.e_book.response.CommonResponse;
import com.saveetha.e_book.response.admin.GetSingleBookResponse;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookDetailsActivity extends AppCompatActivity implements PaymentResultListener {

    ActivityBookDetailsBinding binding;
    private String  book_id, book_publisher_id ,book_price, book_pdf;
    private int user_id;
    private String userName,userEmail,phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityBookDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String userId = SF.getSignInSFValue(this).get(Constant.ID_SI_SF);
        userName = SF.getSignInSFValue(this).get(Constant.NAME_SI_SF);
        userEmail = SF.getSignInSFValue(this).get(Constant.EMAIL_SI_SF);
        phone = SF.getSignInSFValue(this).get(Constant.PHONE_SI_SF);
        if (userId != null) {
            this.user_id = Integer.parseInt(userId);
        } else {
            Toast.makeText(this, "Error in getting user id", Toast.LENGTH_SHORT).show();
        }

        if (getIntent() != null) {

//            book_name = getIntent().getStringExtra("book_name");
//            book_image = getIntent().getStringExtra("book_image");
//            book_description = getIntent().getStringExtra("book_description");
            book_id = getIntent().getStringExtra("book_id");
//            book_author = getIntent().getStringExtra("book_author");
            book_publisher_id = getIntent().getStringExtra("book_publisher_id");
//            book_publisher_name = getIntent().getStringExtra("book_publisher_name");
//            book_submit_date = getIntent().getStringExtra("book_submit_date");
//            book_year = getIntent().getStringExtra("book_year");
//            book_approval_status = getIntent().getStringExtra("book_approval_status");
//            book_approval_date = getIntent().getStringExtra("book_approval_date");
//            book_cancelled_msg = getIntent().getStringExtra("book_cancelled_msg");
//            book_demo_book = getIntent().getStringExtra("book_demo_book");
//            book_pdf = getIntent().getStringExtra("book_pdf");
//            book_cover_image = getIntent().getStringExtra("book_cover_image");
//            book_title = getIntent().getStringExtra("book_title");
        }

        loadBookDetails();
        onClick();
    }



    private void onClick() {
        binding.reviewCV.setOnClickListener(v-> {
            Intent intent = new Intent(this, AddReviewActivity.class);
            intent.putExtra("book_id", book_id);
            intent.putExtra("book_publisher_id", book_publisher_id);
            startActivity(intent);
        });

        binding.backCV.setOnClickListener(v-> finish());

        binding.saveBookCV.setOnClickListener(v-> saveBook());
    }

    private void saveBook() {

        Call<CommonResponse> res = RestClient.makeAPI().saveBook(Integer.parseInt(book_id),user_id);

        res.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if(response.isSuccessful()) {
                    if(response.body().getStatus() == 200){
                        Toast.makeText(BookDetailsActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(BookDetailsActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(BookDetailsActivity.this, ""+response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                Toast.makeText(BookDetailsActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadBookDetails() {
        Call<GetSingleBookResponse> call = RestClient.makeAPI().getUserSingleBook(Integer.parseInt(book_id),user_id);
        call.enqueue(new Callback<GetSingleBookResponse>() {
            @Override
            public void onResponse(Call<GetSingleBookResponse> call, Response<GetSingleBookResponse> response) {

                if(response.isSuccessful()){
                    if(response.body().getStatus() == 200){
                        GetSingleBookResponse getSingleBookResponse = response.body();
                        if(getSingleBookResponse != null) {
                            binding.bookNameTV.setText("Book Name: "+getSingleBookResponse.getData().getBook_title());
                            binding.autherNameTV.setText("Auther Name: "+getSingleBookResponse.getData().getAuther_name());
                            binding.bookDescription.setText(getSingleBookResponse.getData().getBook_description());
                            book_price = getSingleBookResponse.getData().getPrice();
                            book_pdf = getSingleBookResponse.getData().getBook_pdf();
                            binding.readDemoBTN.setOnClickListener(view -> {
                                Intent intent = new Intent(BookDetailsActivity.this, ReadBookActivity.class);
//                                        Intent intent = new Intent(BookDetailsActivity.this, ReadBookActivity.class);
                                intent.putExtra("book", getSingleBookResponse.getData().getDemo_book());
//                                        Toast.makeText(BookDetailsActivity.this, book_pdf, Toast.LENGTH_SHORT).show();
                                Log.e("TAG", "onClick: "+ book_pdf);
                                intent.putExtra("bookId", book_id);
                                startActivity(intent);
                            });
                            binding.bookPriceTV.setText("₹"+book_price);
                            Glide.with(BookDetailsActivity.this)
                                    .load(getSingleBookResponse.getData().getBook_cover_image())
                                    .placeholder(R.drawable.book_icon)
                                    .error(R.drawable.book_icon)
                                    .into(binding.imageBookSIV);
                            if(getSingleBookResponse.getData().isPayment_status()){
                                binding.buyReadBTN.setText("Read");
                                binding.readDemoBTN.setVisibility(View.GONE);
                            }

                            binding.buyReadBTN.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(getSingleBookResponse.getData().isPayment_status()){
                                        Intent intent = new Intent(BookDetailsActivity.this, ReadBookActivity.class);
//                                        Intent intent = new Intent(BookDetailsActivity.this, ReadBookActivity.class);
                                        intent.putExtra("book", book_pdf);
//                                        Toast.makeText(BookDetailsActivity.this, book_pdf, Toast.LENGTH_SHORT).show();
                                        Log.e("TAG", "onClick: "+ book_pdf);
                                        intent.putExtra("bookId", book_id);
                                        startActivity(intent);
                                    } else {
                                        long amount = Long.parseLong(book_price);
                                        startPayment(amount,userName,userEmail);
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(BookDetailsActivity.this, "Error in getting book details", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(BookDetailsActivity.this, ""+response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetSingleBookResponse> call, Throwable t) {
                Toast.makeText(BookDetailsActivity.this, "Error in getting book details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startPayment( long amount,String username,String userEmail) {
        // initialize Razorpay account.
        Checkout checkout = new Checkout();

        // set your id as below
        checkout.setKeyID("rzp_test_U2XWpODmhRkL0l" + "");

        // set image
        checkout.setImage(R.drawable.profile);

        // initialize json object
        JSONObject object = new JSONObject();
        try {
            // to put name
            object.put("name", username);

            // put description
            object.put("description", "Test payment");

            // put the currency
            object.put("currency", "INR");

            // put amount
            object.put("amount", (amount*100));

            // put mobile number
            object.put("prefill.contact", phone);
            // put email
            object.put("prefill.email", userEmail);

            // open razorpay to checkout activity
            checkout.open(this, object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onPaymentSuccess(String transactionId) {
        finish();
        Request.BuyBook request = new Request.BuyBook(user_id, book_publisher_id, book_id, book_price, transactionId);
        Call<CommonResponse> call = RestClient.makeAPI().buyBook(request);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response.isSuccessful()) {
                    if(response.body().getStatus()==200) {
                        Toast.makeText(BookDetailsActivity.this,"Payment Success",Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(BookDetailsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(BookDetailsActivity.this, "server busy", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                Toast.makeText(BookDetailsActivity.this,"check your internet connection",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment failed", Toast.LENGTH_SHORT).show();
        Log.e("TAG", "onPaymentError:"+ s);
    }
}