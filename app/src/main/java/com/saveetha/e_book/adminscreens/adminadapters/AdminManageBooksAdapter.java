package com.saveetha.e_book.adminscreens.adminadapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.saveetha.e_book.R;
import com.saveetha.e_book.adminscreens.AdminBookDetailsActivity;
import com.saveetha.e_book.adminscreens.adminmodules.AdminBooksModule;

import java.util.List;

public class AdminManageBooksAdapter extends RecyclerView.Adapter<AdminManageBooksAdapter.MyViewHolder> {

    List<AdminBooksModule> adminBooksModuleList;
    Context context;

    public AdminManageBooksAdapter(List<AdminBooksModule> adminBooksModuleList, Context context) {
        this.adminBooksModuleList = adminBooksModuleList;
        this.context = context;
    }

    @NonNull
    @Override
    public AdminManageBooksAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_new_books_layout, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminManageBooksAdapter.MyViewHolder holder, int position) {

        AdminBooksModule adminBooksModule = adminBooksModuleList.get(position);

        Glide.with(context)
                .load(adminBooksModule.getCoverImage())
                .placeholder(R.drawable.book_icon)
                .error(R.drawable.book_icon)
                .into(holder.imageUrl);

        holder.bookName.setText(adminBooksModule.getBookName());
        holder.descrption.setText(adminBooksModule.getDescription());
        holder.price.setText("₹" + adminBooksModule.getPrice());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                context.startActivity(new Intent(context, AdminBookDetailsActivity.class));

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
