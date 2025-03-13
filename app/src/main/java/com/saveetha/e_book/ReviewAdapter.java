package com.saveetha.e_book;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.saveetha.e_book.databinding.CommonReviewsLayoutBinding;

import java.util.List;


public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<ReviewModule> reviewModelList;
    private Context context;
    CommonReviewsLayoutBinding binding;

    public ReviewAdapter(List<ReviewModule> reviewModelList, Context context) {
        this.reviewModelList = reviewModelList;
        this.context         = context;
    }

    @NonNull
    @Override
    public ReviewAdapter.ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = CommonReviewsLayoutBinding.inflate(LayoutInflater.from(context), parent, false);

        return new ReviewViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ReviewViewHolder holder, int position) {

        ReviewModule reviewModule = reviewModelList.get(position);

        Glide.with(context)
                .load(reviewModule.getProfile())
                .placeholder(R.drawable.book_icon)
                .error(R.drawable.book_icon)
                .into(holder.profileImage);

        holder.commenterName.setText(reviewModule.getName());
        holder.descriptionText.setText(reviewModule.getReview());
        holder.dateText.setText(reviewModule.getdTime());

    }

    @Override
    public int getItemCount() {
        return reviewModelList.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        ImageView profileImage;
        TextView commenterName, descriptionText, dateText;
        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = binding.profileImage;
            commenterName = binding.commenterName;
            descriptionText = binding.descriptionText;
            dateText = binding.dateText;
        }
    }
}
