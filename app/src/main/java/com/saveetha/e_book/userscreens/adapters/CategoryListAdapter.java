package com.saveetha.e_book.userscreens.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.saveetha.e_book.userscreens.BooksListActivity;
import com.saveetha.e_book.R;
import com.saveetha.e_book.userscreens.dataclass.CategoryModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder> {

    private ArrayList<CategoryModel> categories;
    private Context context;

    public CategoryListAdapter(Context context, ArrayList<CategoryModel> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.categories_view_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryModel category = categories.get(position);
        holder.categoryName.setText(category.getName());
        Picasso.get()
                .load(category.getImageUrl())
                .into(holder.categoryImage);

        holder.category.setOnClickListener(v -> {
            Intent intent = new Intent(context, BooksListActivity.class);
            intent.putExtra("CATEGORY_TYPE", category.getName());
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
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
