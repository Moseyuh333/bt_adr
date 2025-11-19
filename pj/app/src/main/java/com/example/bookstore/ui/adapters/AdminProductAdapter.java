package com.example.bookstore.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bookstore.R;
import com.example.bookstore.models.Book;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdminProductAdapter extends RecyclerView.Adapter<AdminProductAdapter.ViewHolder> {

    private List<Book> books;
    private OnProductActionListener listener;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = books.get(position);

        holder.titleText.setText(book.getTitle());
        holder.authorText.setText(book.getAuthor());
        holder.categoryText.setText(book.getCategory());

        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        holder.priceText.setText(formatter.format(book.getPrice()));

        holder.stockText.setText("Kho: " + book.getStock());

        holder.editBtn.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEdit(book);
            }
        });

        holder.deleteBtn.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDelete(book);
            }
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bookImage;
        TextView titleText, authorText, priceText, categoryText, stockText;
        Button editBtn, deleteBtn;

        ViewHolder(View itemView) {
            super(itemView);
            bookImage = itemView.findViewById(R.id.admin_book_image);
            titleText = itemView.findViewById(R.id.admin_book_title);
            authorText = itemView.findViewById(R.id.admin_book_author);
            priceText = itemView.findViewById(R.id.admin_book_price);
            categoryText = itemView.findViewById(R.id.admin_book_category);
            stockText = itemView.findViewById(R.id.admin_book_stock);
            editBtn = itemView.findViewById(R.id.admin_edit_btn);
            deleteBtn = itemView.findViewById(R.id.admin_delete_btn);
        }
    }
}
