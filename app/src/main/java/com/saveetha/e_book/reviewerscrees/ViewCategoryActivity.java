package com.saveetha.e_book.reviewerscrees;

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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.saveetha.e_book.R;
import com.saveetha.e_book.RestClient;
import com.saveetha.e_book.databinding.ActivityViewCategoryBinding;
import com.saveetha.e_book.response.GetCategoryResponse;
import com.saveetha.e_book.userscreens.BooksListActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewCategoryActivity extends AppCompatActivity {

    Context context;
    FragmentActivity activity;
    CategoryListAdapter categoryAdapter;
    ActivityViewCategoryBinding binding;
    ArrayList<CategoryModel> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityViewCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        try {
            context = this;
            activity = this;
        } catch (Exception e) {
            e.printStackTrace();
        }

        binding.backCard.setOnClickListener(v -> finish());

        loadHome();


    }

    private void loadHome() {

         categories= new ArrayList<>();

        Call<GetCategoryResponse> responseCall = RestClient.makeAPI().getCategories();
        responseCall.enqueue(new Callback<GetCategoryResponse>() {
            @Override
            public void onResponse(@NonNull Call<GetCategoryResponse> call, @NonNull Response<GetCategoryResponse> response) {
                if(response.isSuccessful()) {
                    if(response.body().getStatus().equals("200")) {
                        ArrayList<GetCategoryResponse.data> item = response.body().getData();
                        if(item.size()>0){
                            for(GetCategoryResponse.data data : item) {
                                categories.add(new CategoryModel(data.getCategory_name(), data.getCategory_image(), data.getCategory_id()));
                            }
                            categoryAdapter= new CategoryListAdapter(context, categories);
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL,false);
                            binding.categoryRV.setLayoutManager(gridLayoutManager);
                            binding.categoryRV.setAdapter(categoryAdapter);
                        } else {
                            Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        Toast.makeText(context, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, ""+response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetCategoryResponse> call, @NonNull Throwable t) {
                Toast.makeText(context, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Error", t.getMessage());

            }
        });


    }

    class CategoryModel {
        private String name,imageUrl,id;

        public CategoryModel(String name, String imageUrl,String id) {
            this.name = name;
            this.id = id;
            this.imageUrl = imageUrl;
        }

        public String getId() {
            return id;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public String getName() {
            return name;
        }
    }

    class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder> {

        private ArrayList<CategoryModel> categories;
        private Context context;

        public CategoryListAdapter(Context context, ArrayList<CategoryModel> categories) {
            this.context = context;
            this.categories = categories;
        }

        @NonNull
        @Override
        public CategoryListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.categories_view_layout, parent, false);
            return new CategoryListAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryListAdapter.ViewHolder holder, int position) {
            CategoryModel category = categories.get(position);
            holder.categoryName.setText(category.getName());
            Picasso.get()
                    .load(category.getImageUrl())
                    .into(holder.categoryImage);

            holder.category.setOnClickListener(v -> {
                Intent intent = new Intent(context, EditCategoryActivity.class);
                intent.putExtra("CATEGORY_NAME", category.getName());
                intent.putExtra("CATEGORY_IMAGE", category.getImageUrl());
                intent.putExtra("CATEGORY_ID", category.getId());
                context.startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return categories.size();
        }

        public void filterList(ArrayList<CategoryModel> filteredlist) {
            categories = filteredlist;
            notifyDataSetChanged();
        }

        public  class ViewHolder extends RecyclerView.ViewHolder {
            ImageView categoryImage;
            TextView categoryName;
            ConstraintLayout category;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                categoryImage = itemView.findViewById(R.id.categoryIV);
                categoryName = itemView.findViewById(R.id.categoryTV);
                category = itemView.findViewById(R.id.categoryCL);
            }
        }
    }



}