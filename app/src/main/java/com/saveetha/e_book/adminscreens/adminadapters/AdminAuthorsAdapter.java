package com.saveetha.e_book.adminscreens.adminadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.saveetha.e_book.R;
import com.saveetha.e_book.adminscreens.adminmodules.AdminAuthorsModule;

import java.util.ArrayList;

public class AdminAuthorsAdapter extends RecyclerView.Adapter<AdminAuthorsAdapter.ViewHolder> {

    ArrayList<AdminAuthorsModule> authors;
    Context context;

    public AdminAuthorsAdapter(ArrayList<AdminAuthorsModule> authors, Context context) {
        this.authors = authors;
        this.context = context;
    }

    @NonNull
    @Override
    public AdminAuthorsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_authors_layout, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull AdminAuthorsAdapter.ViewHolder holder, int position) {

        AdminAuthorsModule author = authors.get(position);

        holder.authorName.setText(author.getName());
        holder.authorEmail.setText(author.getEmail());
        holder.authorPhone.setText(author.getPhone());
        Glide.with(context)
                .load(author.getImageUrl())
                .placeholder(R.drawable.book_icon)
                .error(R.drawable.book_icon)
                .into(holder.authorImage);

    }

    @Override
    public int getItemCount() {
        return authors.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView authorName, authorEmail, authorPhone;
        ImageView authorImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            authorName = itemView.findViewById(R.id.nameTV);
            authorEmail = itemView.findViewById(R.id.emailTV);
            authorPhone = itemView.findViewById(R.id.phoneTV);
            authorImage = itemView.findViewById(R.id.imageView);
        }
    }
}
