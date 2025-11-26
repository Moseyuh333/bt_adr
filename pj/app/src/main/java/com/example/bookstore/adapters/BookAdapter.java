package com.example.bookstore.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.navigation.Navigation;
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
        try {
            Book b = books.get(position);
            if (b == null) {
                holder.title.setText("Sản phẩm không tìm thấy");
                holder.author.setText("");
                return;
            }

            // title = tên sách (Book name)
            String title = (b.title != null && !b.title.isEmpty()) ? b.title : "Sách không tên";
            holder.title.setText(title);

            // author = tác giả (Author name)
            String author = (b.author != null && !b.author.isEmpty()) ? b.author : "Tác giả";
            holder.author.setText(author);

            holder.price.setText(String.format("%,.0f₫", b.price));
            holder.rating.setRating((float) (b.rating > 0 ? b.rating : 4.0));
            holder.reviews.setText("(" + (b.reviews > 0 ? b.reviews : 0) + ")");

            // Show discount badge if discount > 0
            if (b.discount > 0) {
                holder.discountBadge.setVisibility(View.VISIBLE);
                holder.discountBadge.setText("-" + b.discount + "%");
            } else {
                holder.discountBadge.setVisibility(View.GONE);
            }

            // Show original price with strikethrough if discount exists
            if (b.originalPrice > b.price) {
                holder.originalPrice.setVisibility(View.VISIBLE);
                holder.originalPrice.setText(String.format("%,.0f₫", b.originalPrice));
                holder.originalPrice.setPaintFlags(holder.originalPrice.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                holder.originalPrice.setVisibility(View.GONE);
            }

            // Show sold count
            if (b.soldCount > 0) {
                if (b.soldCount >= 1000) {
                    holder.soldCount.setText("Đã bán " + String.format("%.1fk", b.soldCount / 1000.0));
                } else {
                    holder.soldCount.setText("Đã bán " + b.soldCount);
                }
            } else {
                holder.soldCount.setText("");
            }

            // Load image with Glide with better error handling
            if (b.coverImage != null && !b.coverImage.isEmpty()) {
                Glide.with(holder.image.getContext())
                    .load(b.coverImage)
                    .placeholder(R.drawable.book_placeholder)
                    .error(R.drawable.book_placeholder)
                    .centerCrop()
                    .into(holder.image);
            } else {
                holder.image.setImageResource(R.drawable.book_placeholder);
            }

            // Click listener to open book details
            holder.itemView.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putSerializable("book", b);
                Navigation.findNavController(v).navigate(R.id.bookDetailFragment, bundle);
            });
        } catch (Exception e) {
            e.printStackTrace();
            holder.title.setText("Lỗi hiển thị");
        }
    }
    public int getItemCount() { return books.size(); }
    public static class BookViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView title, author, price, reviews, discountBadge, originalPrice, soldCount;
        public RatingBar rating;
        public BookViewHolder(View v) {
            super(v);
            image = v.findViewById(R.id.book_image);
            title = v.findViewById(R.id.book_title);
            author = v.findViewById(R.id.book_author);
            price = v.findViewById(R.id.book_price);
            rating = v.findViewById(R.id.book_rating);
            reviews = v.findViewById(R.id.review_count);
            discountBadge = v.findViewById(R.id.discount_badge);
            originalPrice = v.findViewById(R.id.original_price);
            soldCount = v.findViewById(R.id.sold_count);
        }
    }
}

