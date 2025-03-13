package com.saveetha.e_book.userscreens;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.saveetha.e_book.RestClient;
import com.saveetha.e_book.databinding.ActivityBooksListBinding;
import com.saveetha.e_book.response.GetCategoryResponse;
import com.saveetha.e_book.response.admin.GetBooksData;
import com.saveetha.e_book.response.admin.GetBooksResponse;
import com.saveetha.e_book.response.admin.GetSingleBookResponse;
import com.saveetha.e_book.userscreens.adapters.BookListAdapter;
import com.saveetha.e_book.userscreens.adapters.CategoryListAdapter;
import com.saveetha.e_book.userscreens.dataclass.BookModel;
import com.saveetha.e_book.userscreens.dataclass.CategoryModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BooksListActivity extends AppCompatActivity {

    ActivityBooksListBinding binding;
    ArrayList<BookModel> books;
    Context context = this;
    String categoryType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBooksListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backCV.setOnClickListener(v -> finish());

        if (getIntent() != null) {
            categoryType = getIntent().getStringExtra("CATEGORY_TYPE");
            loadHome();
        }   else{
            Toast.makeText(this, "Error Fetching Category", Toast.LENGTH_SHORT).show();
        }

    }

    private void loadHome() {

        binding.bookCategoryTV.setText(categoryType);

        books = new ArrayList<>();

        Call<GetBooksResponse> responseCall = RestClient.makeAPI().getBooksByCategories(categoryType);
        responseCall.enqueue(new Callback<GetBooksResponse>() {
            @Override
            public void onResponse(@NonNull Call<GetBooksResponse> call, @NonNull Response<GetBooksResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 200) {
                        List<GetBooksData> item = response.body().getData();
                        if (item.size() > 0) {
                            for (GetBooksData data : item) {
                                books.add(new BookModel(data.getBook_title(), data.getBook_cover_image(), data.getBook_id(), data.getPublisher_id(), data.getPublisher_name(), data.getBook_cover_image(), data.getAuther_name(), data.getYear_of_the_book(), data.getBook_submit_date(), data.getBook_description(), data.getBook_approval_status(), data.getBook_approval_date(), data.getBook_cancelled_msg(), data.getBook_title(), data.getBook_pdf(), data.getDemo_book(), data.getPrice()));
                            }
                            BookListAdapter bookAdapter = new BookListAdapter(context, books);
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false);
                            binding.bookListRV.setLayoutManager(gridLayoutManager);
                            binding.bookListRV.setAdapter(bookAdapter);
                        } else {
                            Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetBooksResponse> call, @NonNull Throwable t) {
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Error", t.getMessage());
            }
        });


    }

}