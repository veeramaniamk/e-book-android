package com.saveetha.e_book.userscreens.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.saveetha.e_book.R;
import com.saveetha.e_book.userscreens.BookDetailsActivity;
import com.saveetha.e_book.userscreens.dataclass.BookModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.ViewHolder>{

    Context context;
    List<BookModel> books;

    public BookListAdapter(Context context, List<BookModel> books) {
        this.context = context;
        this.books = books;
    }

    @NonNull
    @Override
    public BookListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.categories_view_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookListAdapter.ViewHolder holder, int position) {
        BookModel book = books.get(position);
        holder.bookName.setText(book.getName());
        Picasso.get().load(book.getImageUrl()).into(holder.bookImage);
        holder.book.setOnClickListener(v->{


            Intent intent = new Intent(context, BookDetailsActivity.class);
            intent.putExtra("book_id", ""+book.getBook_id());
            intent.putExtra("book_publisher_id", ""+book.getPublisher_id());

            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bookImage;
        TextView bookName;
        ConstraintLayout book;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookImage = itemView.findViewById(R.id.categoryIV);
            bookName = itemView.findViewById(R.id.categoryTV);
            book = itemView.findViewById(R.id.categoryCL);
            cardView = itemView.findViewById(R.id.cardlayout);
        }
    }
}
