package com.saveetha.e_book.adminscreens.adminadapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.saveetha.e_book.R;
import com.saveetha.e_book.adminscreens.AdminBookDetailsActivity;
import com.saveetha.e_book.adminscreens.adminmodules.AdminBooksModule;

import java.util.List;

public class AdminHomeBooksAdapter extends RecyclerView.Adapter<AdminHomeBooksAdapter.MyViewHolder> {

    List<AdminBooksModule> adminBooksModuleList;
    Context context;

    public AdminHomeBooksAdapter(List<AdminBooksModule> adminBooksModuleList, Context context) {
        this.adminBooksModuleList = adminBooksModuleList;
        this.context = context;
    }

    @NonNull
    @Override
    public AdminHomeBooksAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_new_books_layout, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminHomeBooksAdapter.MyViewHolder holder, int position) {

        AdminBooksModule adminBooksModule = adminBooksModuleList.get(position);
        Glide.with(context)
                .load(adminBooksModule.getCoverImage())
                .placeholder(R.drawable.book_icon)
                .error(R.drawable.book_icon)
                .into(holder.imageUrl);
//        Toast.makeText(context, ""+adminBooksModule.getBookId(), Toast.LENGTH_SHORT).show();
        holder.bookName.setText(adminBooksModule.getBookName());
        holder.description.setText(adminBooksModule.getDescription());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AdminBookDetailsActivity.class);
                intent.putExtra("bookId", adminBooksModule.getBookId());
                intent.putExtra("status",adminBooksModule.getStatus());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return adminBooksModuleList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView bookName, description;
        ShapeableImageView imageUrl;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageUrl = itemView.findViewById(R.id.imageBook);
            bookName = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.descriptionText);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
