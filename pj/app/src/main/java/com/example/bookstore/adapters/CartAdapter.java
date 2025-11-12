package com.example.bookstore.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.bookstore.R;
import com.example.bookstore.models.Cart;
import com.example.bookstore.models.CartItem;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartItem> items;
    private Runnable onUpdate;
    private Cart cart;

    public CartAdapter(List<CartItem> items, Runnable onUpdate) {
        this.items = items;
        this.onUpdate = onUpdate;
        this.cart = Cart.getInstance();
    }

    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CartViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false));
    }

    public void onBindViewHolder(CartViewHolder holder, int pos) {
        try {
            CartItem item = items.get(pos);
            holder.title.setText(item.book.title);
            holder.author.setText("By: " + item.book.author);
            holder.price.setText(String.format("%,.0f₫", item.book.price));
            holder.qty.setText(String.valueOf(item.quantity));
            holder.totalPrice.setText(String.format("%,.0f₫", item.getTotalPrice()));

            Glide.with(holder.image.getContext())
                .load(item.book.coverImage)
                .placeholder(R.drawable.book_placeholder)
                .error(R.drawable.book_placeholder)
                .centerCrop()
                .into(holder.image);

            // Increase quantity
            holder.inc.setOnClickListener(v -> {
                item.quantity++;
                holder.qty.setText(String.valueOf(item.quantity));
                holder.totalPrice.setText(String.format("$%.2f", item.getTotalPrice()));
                cart.updateQuantity(item.book.id, item.quantity);
                onUpdate.run();
            });

            // Decrease quantity
            holder.dec.setOnClickListener(v -> {
                if (item.quantity > 1) {
                    item.quantity--;
                    holder.qty.setText(String.valueOf(item.quantity));
                    holder.totalPrice.setText(String.format("$%.2f", item.getTotalPrice()));
                    cart.updateQuantity(item.book.id, item.quantity);
                    onUpdate.run();
                }
            });

            // Remove item
            holder.rem.setOnClickListener(v -> {
                cart.removeItem(item.book.id);
                items.remove(pos);
                notifyItemRemoved(pos);
                onUpdate.run();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getItemCount() {
        return items.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView title, author, price, qty, totalPrice;
        public Button inc, dec, rem;

        public CartViewHolder(View v) {
            super(v);
            image = v.findViewById(R.id.cart_book_image);
            title = v.findViewById(R.id.cart_book_title);
            author = v.findViewById(R.id.cart_book_author);
            price = v.findViewById(R.id.cart_item_price);
            qty = v.findViewById(R.id.quantity);
            totalPrice = v.findViewById(R.id.cart_item_total);
            inc = v.findViewById(R.id.increase_btn);
            dec = v.findViewById(R.id.decrease_btn);
            rem = v.findViewById(R.id.remove_btn);
        }
    }
}

