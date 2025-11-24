package com.example.bookstore.adapters;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdminProductAdapter extends RecyclerView.Adapter<AdminProductAdapter.ViewHolder> {

    private List<Book> books;
    private OnProductActionListener listener;

    public interface OnProductActionListener {
        void onEdit(Book book);
        void onDelete(Book book);
    }

    public AdminProductAdapter(OnProductActionListener listener) {
        this.books = new ArrayList<>();
        this.listener = listener;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
        notifyDataSetChanged();
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

        holder.titleText.setText(book.title);
        holder.authorText.setText("Tác giả: " + book.author);
        holder.categoryText.setText("📚 " + book.category);

        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        holder.priceText.setText(formatter.format(book.price) + "₫");

        holder.stockText.setText("Tồn kho: " + book.quantity);
        holder.stockText.setTextColor(book.quantity > 10 ? 0xFF4CAF50 : 0xFFF44336);

        holder.editBtn.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(book);
        });

        holder.deleteBtn.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(book);
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleText, authorText, categoryText, priceText, stockText;
        Button editBtn, deleteBtn;

        ViewHolder(View view) {
            super(view);
            titleText = view.findViewById(R.id.product_title);
            authorText = view.findViewById(R.id.product_author);
            categoryText = view.findViewById(R.id.product_category);
            priceText = view.findViewById(R.id.product_price);
            stockText = view.findViewById(R.id.product_stock);
            editBtn = view.findViewById(R.id.btn_edit_product);
            deleteBtn = view.findViewById(R.id.btn_delete_product);
        }
    }
}

