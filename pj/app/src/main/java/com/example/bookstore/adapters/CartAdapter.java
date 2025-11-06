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
import com.example.bookstore.models.CartItem;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartItem> items;
    private Runnable onUpdate;
    public CartAdapter(List<CartItem> items, Runnable onUpdate) { this.items = items; this.onUpdate = onUpdate; }
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CartViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false));
    }
    public void onBindViewHolder(CartViewHolder holder, int pos) {
        CartItem item = items.get(pos);
        holder.title.setText(item.book.title);
        holder.price.setText("$"+item.book.price);
        holder.qty.setText(""+item.quantity);
        Glide.with(holder.image).load(item.book.coverImage).placeholder(R.drawable.ic_launcher_background).into(holder.image);
        holder.inc.setOnClickListener(v -> { item.quantity++; holder.qty.setText(""+item.quantity); onUpdate.run(); });
        holder.dec.setOnClickListener(v -> { if(item.quantity>1) { item.quantity--; holder.qty.setText(""+item.quantity); onUpdate.run(); } });
        holder.rem.setOnClickListener(v -> { items.remove(pos); notifyItemRemoved(pos); onUpdate.run(); });
    }
    public int getItemCount() { return items.size(); }
    public static class CartViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView title, price, qty;
        public Button inc, dec, rem;
        public CartViewHolder(View v) {
            super(v);
            image = v.findViewById(R.id.cart_book_image);
            title = v.findViewById(R.id.cart_book_title);
            price = v.findViewById(R.id.cart_item_price);
            qty = v.findViewById(R.id.quantity);
            inc = v.findViewById(R.id.increase_btn);
            dec = v.findViewById(R.id.decrease_btn);
            rem = v.findViewById(R.id.remove_btn);
        }
    }
}

