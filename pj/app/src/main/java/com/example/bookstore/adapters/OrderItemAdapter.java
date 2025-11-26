package com.example.bookstore.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookstore.R;
import com.example.bookstore.models.CartItem;
import com.example.bookstore.database.entities.OrderItem;

import java.util.List;

/**
 * Adapter để hiển thị danh sách items trong đơn hàng (OrderDetailFragment)
 * Hỗ trợ cả CartItem và OrderItem
 */
public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder> {
    private List<?> items; // Có thể là List<CartItem> hoặc List<OrderItem>

    public OrderItemAdapter(List<?> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_product, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        try {
            if (items.get(position) instanceof OrderItem) {
                bindOrderItem(holder, (OrderItem) items.get(position));
            } else if (items.get(position) instanceof CartItem) {
                bindCartItem(holder, (CartItem) items.get(position));
            }
        } catch (Exception e) {
            e.printStackTrace();
            holder.title.setText("Lỗi hiển thị sản phẩm");
        }
    }

    private void bindOrderItem(@NonNull OrderItemViewHolder holder, OrderItem item) {
        if (item == null) return;

        // Hiển thị thông tin sản phẩm từ OrderItem
        String title = item.getBookTitle() != null ? item.getBookTitle() : "Sản phẩm không tên";
        String author = item.getBookAuthor() != null ? item.getBookAuthor() : "Tác giả";
        String imageUrl = item.getBookImageUrl() != null ? item.getBookImageUrl() : "";
        double price = item.getPrice();
        int quantity = item.getQuantity();
        double subtotal = item.getSubtotal();

        holder.title.setText(title);
        holder.author.setText("Tác giả: " + author);
        holder.price.setText(String.format("%,.0f₫ × %d = %,.0f₫", price, quantity, subtotal));
        holder.quantity.setText("SL: " + quantity);

        // Load ảnh
        if (!imageUrl.isEmpty()) {
            Glide.with(holder.image.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.book_placeholder)
                    .error(R.drawable.book_placeholder)
                    .centerCrop()
                    .into(holder.image);
        } else {
            holder.image.setImageResource(R.drawable.book_placeholder);
        }
    }

    private void bindCartItem(@NonNull OrderItemViewHolder holder, CartItem item) {
        if (item == null || item.book == null) {
            holder.title.setText("Sản phẩm không tìm thấy");
            return;
        }

        // Hiển thị thông tin sản phẩm từ CartItem
        holder.title.setText(item.book.title != null ? item.book.title : "Tên sách");
        holder.author.setText("Tác giả: " + (item.book.author != null ? item.book.author : "Tác giả"));
        holder.price.setText(String.format("%,.0f₫ × %d = %,.0f₫",
                item.book.price, item.quantity, item.getTotalPrice()));
        holder.quantity.setText("SL: " + item.quantity);

        // Load ảnh
        if (item.book.coverImage != null && !item.book.coverImage.isEmpty()) {
            Glide.with(holder.image.getContext())
                    .load(item.book.coverImage)
                    .placeholder(R.drawable.book_placeholder)
                    .error(R.drawable.book_placeholder)
                    .centerCrop()
                    .into(holder.image);
        } else {
            holder.image.setImageResource(R.drawable.book_placeholder);
        }
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public static class OrderItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView title, author, price, quantity;

        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.item_image);
            title = itemView.findViewById(R.id.item_title);
            author = itemView.findViewById(R.id.item_author);
            price = itemView.findViewById(R.id.item_price);
            quantity = itemView.findViewById(R.id.item_quantity);
        }
    }
}

