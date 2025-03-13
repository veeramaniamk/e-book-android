package com.saveetha.e_book;


import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

public class StaticMethods {

    public static void setGlide(FragmentActivity activity, ShapeableImageView imageView, String url) {
        Glide.with(activity)
                .load(url)
                .placeholder(R.drawable.book_icon)
                .error(R.drawable.book_icon)
                .into(imageView);
    }

}
