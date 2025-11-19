package com.example.bookstore.adapters;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookstore.R;
import com.example.bookstore.models.Book;

import java.util.List;

public class AdminProductAdapter extends RecyclerView.Adapter<AdminProductAdapter.ViewHolder> {
    private final List<Book> books;
    private final OnProductActionListener listener;

    public interface OnProductActionListener {
        void onEdit(Book book);
        void onDelete(Book book);
    }

    public AdminProductAdapter(List<Book> books, OnProductActionListener listener) {
        this.books = books;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = books.get(position);

        holder.title.setText(book.title);
        holder.author.setText(book.author);
        holder.price.setText(String.format("%,.0f₫", book.price));
        holder.category.setText(book.category);
        holder.stock.setText("Kho: " + book.quantity);

        Glide.with(holder.image.getContext())
                .load(book.coverImage)
                .placeholder(R.drawable.book_placeholder)
                .error(R.drawable.book_placeholder)
                .centerCrop()
                .into(holder.image);

        holder.editBtn.setOnClickListener(v -> listener.onEdit(book));

        holder.deleteBtn.setOnClickListener(v ->
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc muốn xóa sản phẩm này?")
                        .setPositiveButton("Xóa", (dialog, which) -> listener.onDelete(book))
                        .setNegativeButton("Hủy", null)
                        .show()
        );
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, author, price, category, stock;
        Button editBtn, deleteBtn;

        ViewHolder(@NonNull View view) {
            super(view);
            image = view.findViewById(R.id.product_image);
            title = view.findViewById(R.id.product_title);
            author = view.findViewById(R.id.product_author);
            price = view.findViewById(R.id.product_price);
            category = view.findViewById(R.id.product_category);
            stock = view.findViewById(R.id.product_stock);
            editBtn = view.findViewById(R.id.edit_product_btn);
            deleteBtn = view.findViewById(R.id.delete_product_btn);
        }
    }
}

