package com.example.bookstore.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore.R;
import com.example.bookstore.adapters.OrderAdapter;
import com.example.bookstore.models.Order;
import com.example.bookstore.utils.OrderManager;

import java.util.List;

public class OrdersFragment extends Fragment {

    private RecyclerView ordersRecycler;
    private TextView emptyOrdersText;
    private OrderAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            ordersRecycler = view.findViewById(R.id.orders_recycler);
            emptyOrdersText = view.findViewById(R.id.empty_orders_text);

            ordersRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

            loadOrders();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadOrders() {
        try {
            SharedPreferences prefs = requireActivity().getSharedPreferences("BookstorePrefs", Context.MODE_PRIVATE);
            String userEmail = prefs.getString("user_email", "");

            List<Order> orders = OrderManager.getInstance(getContext()).getOrdersByUser(userEmail);

            if (orders.isEmpty()) {
                emptyOrdersText.setVisibility(View.VISIBLE);
                ordersRecycler.setVisibility(View.GONE);
            } else {
                emptyOrdersText.setVisibility(View.GONE);
                ordersRecycler.setVisibility(View.VISIBLE);
                adapter = new OrderAdapter(orders);
                ordersRecycler.setAdapter(adapter);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadOrders(); // Refresh when coming back
    }
}

