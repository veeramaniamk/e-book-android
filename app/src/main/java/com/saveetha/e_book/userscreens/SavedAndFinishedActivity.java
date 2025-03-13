package com.saveetha.e_book.userscreens;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.saveetha.e_book.Constant;
import com.saveetha.e_book.RestClient;
import com.saveetha.e_book.SF;
import com.saveetha.e_book.StaticMethods;
import com.saveetha.e_book.databinding.ActivitySavedAndFinishedBinding;
import com.saveetha.e_book.databinding.CategoriesViewLayoutBinding;
import com.saveetha.e_book.databinding.RejectBookDialogBinding;
import com.saveetha.e_book.databinding.RemoveSavedBookLayoutBinding;
import com.saveetha.e_book.request.ApproveBookRequest;
import com.saveetha.e_book.response.CommonResponse;
import com.saveetha.e_book.response.user.GetFinishedBookData;
import com.saveetha.e_book.response.user.GetFinishedBookResponse;
import com.saveetha.e_book.response.user.GetSavedBookData;
import com.saveetha.e_book.response.user.GetSavedBooksReponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SavedAndFinishedActivity extends AppCompatActivity {

    private Context context;
    private FragmentActivity activity;
    ActivitySavedAndFinishedBinding binding;
    String userId;
    String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySavedAndFinishedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        try {
            context = this;
            activity = this;
             title = getIntent().getStringExtra("title");
            binding.title.setText(title);
            userId = SF.getSignInSFValue(activity).get(Constant.ID_SI_SF);
            if(title.equalsIgnoreCase("Saved List")){
                savedBookApi(userId);
            }else{
                finishedBookApi(userId);
            }
            if(title.equalsIgnoreCase("My Books")){
                myBooksApi(userId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        clickListener();
    }

    private void myBooksApi(String userId) {
        Call<GetFinishedBookResponse> responseCall = RestClient.makeAPI().getFinishedBooks(userId);
        responseCall.enqueue(new Callback<GetFinishedBookResponse>() {
            @Override
            public void onResponse(@NonNull Call<GetFinishedBookResponse> call, @NonNull Response<GetFinishedBookResponse> response) {
                if(response.isSuccessful()) {
                    if(response.body().getStatus() == 200) {
                        if(response.body().getData() == null){
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        List<BookData> list = new ArrayList<>();
                        for (GetFinishedBookData data : response.body().getData()) {
                            list.add(new BookData(data.getBook_title(),data.getBook_id(),data.getBook_cover_image(),data.getSaved_id(),title,data.getPublisher_id()));
                        }
                        binding.recyclerView.setLayoutManager(new GridLayoutManager(context,3));
                        binding.recyclerView.setAdapter(new SavedAndFinishedAdapter(activity,list));
                    }else {
                        Toast.makeText(context, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, ""+response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetFinishedBookResponse> call, @NonNull Throwable t) {
                Toast.makeText(context, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Error", t.getMessage());
            }
        });
    }

    private void finishedBookApi(String userId) {
        Call<GetFinishedBookResponse> responseCall = RestClient.makeAPI().getFinishedBooks(userId);
        responseCall.enqueue(new Callback<GetFinishedBookResponse>() {
            @Override
            public void onResponse(@NonNull Call<GetFinishedBookResponse> call, @NonNull Response<GetFinishedBookResponse> response) {
                if(response.isSuccessful()) {
                    if(response.body().getStatus() == 200) {
                        if(response.body().getData() == null){
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        List<BookData> list = new ArrayList<>();
                        for (GetFinishedBookData data : response.body().getData()) {
                            list.add(new BookData(data.getBook_title(),data.getBook_id(),data.getBook_cover_image(),data.getSaved_id(),title,data.getPublisher_id()));
                        }
                        binding.recyclerView.setLayoutManager(new GridLayoutManager(context,3));
                        binding.recyclerView.setAdapter(new SavedAndFinishedAdapter(activity,list));
                    }else {
                        Toast.makeText(context, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, ""+response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetFinishedBookResponse> call, @NonNull Throwable t) {
                Toast.makeText(context, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Error", t.getMessage());
            }
        });
    }

    private void savedBookApi(String userId) {
        Call<GetSavedBooksReponse> responseCall = RestClient.makeAPI().getSavedBooks(userId);
        responseCall.enqueue(new Callback<GetSavedBooksReponse>() {
            @Override
            public void onResponse(@NonNull Call<GetSavedBooksReponse> call, @NonNull Response<GetSavedBooksReponse> response) {
                if(response.isSuccessful()) {
                    if(response.body().getStatus() == 200) {
                        if(response.body().getData() == null){
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        List<BookData> list = new ArrayList<>();
                        for (GetSavedBookData data : response.body().getData()) {
                            list.add(new BookData(data.getBook_title(),data.getBook_id(),data.getBook_cover_image(),data.getSaved_id(),title, data.getPublisher_id()));

                        }
                        binding.recyclerView.setLayoutManager(new GridLayoutManager(context,3));
                        binding.recyclerView.setAdapter(new SavedAndFinishedAdapter(activity,list));
                    }else {
                        Toast.makeText(context, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, ""+response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetSavedBooksReponse> call, @NonNull Throwable t) {
                Toast.makeText(context, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Error", t.getMessage());
            }
        });
    }

    private void clickListener() {
        binding.backCV.setOnClickListener(v -> finish());
    }

    private class BookData {
        private String title;
        private int bookId;
        private String bookCoverImage;
        private int bookSavedId;
        private String from;
        private int publisherId;

        public BookData(String title, int bookId, String bookCoverImage, int bookSavedId,String from, int publisherId) {
            this.title = title;
            this.bookId = bookId;
            this.bookCoverImage = bookCoverImage;
            this.bookSavedId = bookSavedId;
            this.from = from;

        }
        public int getPublisherId() {
            return publisherId;
        }

        public String getFrom() {
            return from;
        }

        public int getBookSavedId() {
            return bookSavedId;
        }

        public String getTitle() {
            return title;
        }

        public int getBookId() {
            return bookId;
        }

        public String getBookCoverImage() {
            return bookCoverImage;
        }
    }

    private class SavedAndFinishedAdapter extends RecyclerView.Adapter<SavedAndFinishedAdapter.MyViewHolder> {

        FragmentActivity activity;
        List<BookData> list;

        public SavedAndFinishedAdapter(FragmentActivity activity, List<BookData> list) {
            this.activity = activity;
            this.list     = list;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            CategoriesViewLayoutBinding binding = CategoriesViewLayoutBinding.inflate(getLayoutInflater(), parent, false);
            return new MyViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            BookData data = list.get(position);
            StaticMethods.setGlide(activity, holder.binding.categoryIV, data.getBookCoverImage());
            holder.binding.categoryTV.setText(data.getTitle());
            if(data.getFrom().equalsIgnoreCase("Saved List")){
                holder.binding.cardlayout.setOnLongClickListener(v -> {
                    bookRejectApiCall(""+data.getBookId(),userId);
                    return false;
                });
            }
            holder.binding.cardlayout.setOnClickListener(v -> {
                Intent intent = new Intent(context, BookDetailsActivity.class);
                intent.putExtra("book_id", ""+data.getBookId());
                intent.putExtra("book_publisher_id", ""+data.getPublisherId());
                context.startActivity(intent);
            });
        }

        private void bookRejectApiCall(String saveId, String userId) {
            Dialog dialog = new Dialog(context);
            RemoveSavedBookLayoutBinding binding = RemoveSavedBookLayoutBinding.inflate(dialog.getLayoutInflater());
            dialog.setContentView(binding.getRoot());
            Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.show();
            binding.cancelButton.setOnClickListener(view ->  dialog.dismiss());
            binding.removeB.setOnClickListener(view -> {

                Call<CommonResponse> responseCall = RestClient.makeAPI().removeSavedBooks(userId,saveId);
                responseCall.enqueue(new Callback<CommonResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                        dialog.dismiss();
                        if(response.isSuccessful()) {
                            if(response.body().getStatus() ==200) {
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
                        dialog.dismiss();
                        Toast.makeText(context, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("Error", t.getMessage());
                    }
                });
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            CategoriesViewLayoutBinding binding;
            MyViewHolder(CategoriesViewLayoutBinding itemView) {
                super(itemView.getRoot());
                binding = itemView;
            }
        }
    }

}