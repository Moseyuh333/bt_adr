package com.example.bookstore.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.bookstore.R;
import com.example.bookstore.models.Book;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private List<Book> books;
    public BookAdapter(List<Book> books) { this.books = books; }
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BookViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false));
    }
    public void onBindViewHolder(BookViewHolder holder, int position) {
        Book b = books.get(position);
        holder.title.setText(b.title);
        holder.author.setText(b.author);
        holder.price.setText("$"+b.price);
        holder.rating.setRating((float)b.rating);
        holder.reviews.setText("("+b.reviews+")");

        // Load image with Glide with better error handling
        Glide.with(holder.image.getContext())
            .load(b.coverImage)
            .placeholder(R.drawable.book_placeholder)
            .error(R.drawable.book_placeholder)
            .centerCrop()
            .into(holder.image);
    }
    public int getItemCount() { return books.size(); }
    public static class BookViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView title, author, price, reviews;
        public RatingBar rating;
        public BookViewHolder(View v) {
            super(v);
            image = v.findViewById(R.id.book_image);
            title = v.findViewById(R.id.book_title);
            author = v.findViewById(R.id.book_author);
            price = v.findViewById(R.id.book_price);
            rating = v.findViewById(R.id.book_rating);
            reviews = v.findViewById(R.id.review_count);
        }
    }
}

