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
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class OrdersFragment extends Fragment {

    private RecyclerView ordersRecycler;
    private TextView emptyOrdersText;
    private TabLayout statusTabs;
    private OrderAdapter adapter;
    private String currentStatus = "ALL";

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
            statusTabs = view.findViewById(R.id.order_status_tabs);

            ordersRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

            // Setup tabs
            setupTabs();

            loadOrders();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupTabs() {
        statusTabs.addTab(statusTabs.newTab().setText("Tất cả"));
        statusTabs.addTab(statusTabs.newTab().setText("Chờ xác nhận"));
        statusTabs.addTab(statusTabs.newTab().setText("Đã xác nhận"));
        statusTabs.addTab(statusTabs.newTab().setText("Đang giao"));
        statusTabs.addTab(statusTabs.newTab().setText("Đã giao"));
        statusTabs.addTab(statusTabs.newTab().setText("Đã hủy"));
        statusTabs.addTab(statusTabs.newTab().setText("Đã hoàn"));

        statusTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0: currentStatus = "ALL"; break;
                    case 1: currentStatus = "PENDING"; break;
                    case 2: currentStatus = "CONFIRMED"; break;
                    case 3: currentStatus = "SHIPPED"; break;
                    case 4: currentStatus = "DELIVERED"; break;
                    case 5: currentStatus = "CANCELLED"; break;
                    case 6: currentStatus = "RETURNED"; break;
                }
                loadOrders();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void loadOrders() {
        try {
            SharedPreferences prefs = requireActivity().getSharedPreferences("BookstorePrefs", Context.MODE_PRIVATE);
            String userEmail = prefs.getString("user_email", "");

            List<Order> orders;
            if (currentStatus.equals("ALL")) {
                orders = OrderManager.getInstance(getContext()).getOrdersByUser(userEmail);
            } else {
                orders = OrderManager.getInstance(getContext()).getOrdersByStatus(userEmail, currentStatus);
            }

            if (orders.isEmpty()) {
                emptyOrdersText.setVisibility(View.VISIBLE);
                ordersRecycler.setVisibility(View.GONE);
            } else {
                emptyOrdersText.setVisibility(View.GONE);
                ordersRecycler.setVisibility(View.VISIBLE);
                adapter = new OrderAdapter(orders, () -> loadOrders());
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

