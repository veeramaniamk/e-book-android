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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.saveetha.e_book.R;
import com.saveetha.e_book.RestClient;
import com.saveetha.e_book.adminscreens.adminmodules.AdminAuthorsModule;
import com.saveetha.e_book.databinding.ActivityAdminAuthorsBinding;
import com.saveetha.e_book.request.Signin;
import com.saveetha.e_book.response.SignInResponse;
import com.saveetha.e_book.response.admin.GetPublisherData;
import com.saveetha.e_book.response.admin.GetPublisherResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminAuthorsActivity extends AppCompatActivity {

    ActivityAdminAuthorsBinding binding;
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityAdminAuthorsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        apiCall();

        binding.backCard.setOnClickListener(v -> finish());
    }

private void apiCall() {
    Call<GetPublisherResponse> responseCall = RestClient.makeAPI().getPublisher();
    responseCall.enqueue(new Callback<GetPublisherResponse>() {
        @Override
        public void onResponse(@NonNull Call<GetPublisherResponse> call, @NonNull Response<GetPublisherResponse> response) {
            if(response.isSuccessful()) {
                if(response.body().getStatus() ==200) {
                    GetPublisherResponse response1 = response.body();
                    if(response1.getData()==null){
                        Toast.makeText(context, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    ArrayList<PublisherData> list = new ArrayList<>();
                    for(GetPublisherData data : response1.getData()){
                        list.add(new PublisherData(data.getName(),data.getEmail(),data.getId(),data.getPhone(),data.getProfile()));
                    }
                    AdminAuthorsAdapter adapter = new AdminAuthorsAdapter(list,context);
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
        public void onFailure(@NonNull Call<GetPublisherResponse> call, @NonNull Throwable t) {
            Toast.makeText(context, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("Error", t.getMessage());
        }
    });
}

    public class PublisherData {
        private String publisherName;
        private String publisherEmail;
        private int publisherId;
        private long publisherPhone;
        private String publisherProfile;

        public PublisherData(String publisherName, String publisherEmail, int publisherId, long publisherPhone, String publisherProfile) {
            this.publisherName = publisherName;
            this.publisherEmail = publisherEmail;
            this.publisherId = publisherId;
            this.publisherPhone = publisherPhone;
            this.publisherProfile = publisherProfile;
        }

        public String getPublisherName() {
            return publisherName;
        }

        public String getPublisherEmail() {
            return publisherEmail;
        }

        public int getPublisherId() {
            return publisherId;
        }

        public long getPublisherPhone() {
            return publisherPhone;
        }

        public String getPublisherProfile() {
            return publisherProfile;
        }
    }

    public class AdminAuthorsAdapter extends RecyclerView.Adapter<AdminAuthorsAdapter.ViewHolder> {

        ArrayList<PublisherData> authors;
        Context context;

        public AdminAuthorsAdapter(ArrayList<PublisherData> authors, Context context) {
            this.authors = authors;
            this.context = context;
        }

        @NonNull
        @Override
        public AdminAuthorsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_authors_layout, parent, false);
            return new AdminAuthorsAdapter.ViewHolder(view);

        }

        @Override
        public void onBindViewHolder(@NonNull AdminAuthorsAdapter.ViewHolder holder, int position) {

            PublisherData author = authors.get(position);

            holder.authorName.setText(author.getPublisherName());
            holder.authorEmail.setText(author.getPublisherEmail());
            holder.authorPhone.setText(""+author.getPublisherPhone());
            Glide.with(context)
                    .load(author.getPublisherProfile())
                    .placeholder(R.drawable.book_icon)
                    .error(R.drawable.book_icon)
                    .into(holder.authorImage);
            holder.cv.setOnClickListener(v -> {
                Intent intent = new Intent(context,AdminPublisherProfileActivity.class);
                intent.putExtra("publisherId", author.getPublisherId());
                intent.putExtra("publisherProfile",author.getPublisherProfile());
                intent.putExtra("publisherName",author.getPublisherName());
                context.startActivity(intent);
            });

        }

        @Override
        public int getItemCount() {
            return authors.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView authorName, authorEmail, authorPhone;
            ImageView authorImage;
            CardView cv;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                cv = itemView.findViewById(R.id.card);
                authorName = itemView.findViewById(R.id.nameTV);
                authorEmail = itemView.findViewById(R.id.emailTV);
                authorPhone = itemView.findViewById(R.id.phoneTV);
                authorImage = itemView.findViewById(R.id.imageView);
            }
        }
    }

}