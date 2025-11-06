package com.example.bookstore.ui.fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bookstore.R;
import com.example.bookstore.adapters.CartAdapter;
import com.example.bookstore.models.CartItem;
import com.example.bookstore.models.Book;
import java.util.ArrayList;
import java.util.List;
public class CartFragment extends Fragment {
    private List<CartItem> items = new ArrayList<>();
    private TextView totalPrice;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            RecyclerView recycler = view.findViewById(R.id.cart_recycler);
            totalPrice = view.findViewById(R.id.total_price);
            if (recycler != null && totalPrice != null) {
                recycler.setLayoutManager(new LinearLayoutManager(getContext()));
                items.add(new CartItem(new Book(1, "Book 1", "Author 1", 19.99, "Desc", "https://via.placeholder.com/200x300", 4.5, 100, "Fiction", true), 2));
                items.add(new CartItem(new Book(2, "Book 2", "Author 2", 24.99, "Desc", "https://via.placeholder.com/200x300", 4.0, 80, "Non-Fiction", true), 1));
                recycler.setAdapter(new CartAdapter(items, this::updateTotal));
                updateTotal();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void updateTotal() {
        try {
            double total = 0;
            for (CartItem item : items) {
                total += item.getTotalPrice();
            }
            if (totalPrice != null) {
                totalPrice.setText(String.format("$%.2f", total));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

