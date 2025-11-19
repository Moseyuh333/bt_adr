package com.example.bookstore.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore.R;
import com.example.bookstore.models.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<Review> reviews;

    public ReviewAdapter(List<Review> reviews) {
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.reviewerName.setText(review.reviewerName);
        holder.reviewRating.setRating(review.rating);
        holder.reviewDate.setText(review.date);
        holder.reviewContent.setText(review.content);
    }

    @Override
    public int getItemCount() {
        return reviews != null ? reviews.size() : 0;
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView reviewerName, reviewDate, reviewContent;
        RatingBar reviewRating;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            reviewerName = itemView.findViewById(R.id.reviewer_name);
            reviewRating = itemView.findViewById(R.id.review_rating);
            reviewDate = itemView.findViewById(R.id.review_date);
            reviewContent = itemView.findViewById(R.id.review_content);
        }
    }
}

