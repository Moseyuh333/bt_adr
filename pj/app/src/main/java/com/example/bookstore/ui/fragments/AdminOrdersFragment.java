package com.example.bookstore.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore.R;
import com.example.bookstore.adapters.AdminOrderAdapter;
import com.example.bookstore.models.Order;
import com.example.bookstore.utils.OrderManager;

import java.util.ArrayList;
import java.util.List;

public class AdminOrdersFragment extends Fragment {
    private RecyclerView recyclerView;
    private AdminOrderAdapter adapter;
    private Button backBtn, filterAll, filterPending, filterProcessing, filterShipping, filterCompleted, filterCancelled;
    private List<Order> allOrders, filteredOrders;
    private String currentFilter = "Tất cả";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.admin_orders_recycler);
        backBtn = view.findViewById(R.id.back_btn);
        filterAll = view.findViewById(R.id.filter_all);
        filterPending = view.findViewById(R.id.filter_pending);
        filterProcessing = view.findViewById(R.id.filter_processing);
        filterShipping = view.findViewById(R.id.filter_shipping);
        filterCompleted = view.findViewById(R.id.filter_completed);
        filterCancelled = view.findViewById(R.id.filter_cancelled);

        OrderManager orderManager = OrderManager.getInstance(requireContext());
        allOrders = orderManager.getAllOrders();
        filteredOrders = new ArrayList<>(allOrders);

        adapter = new AdminOrderAdapter(filteredOrders, this::onOrderClick);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        backBtn.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());

        filterAll.setOnClickListener(v -> applyFilter("Tất cả", filterAll));
        filterPending.setOnClickListener(v -> applyFilter("Chờ xác nhận", filterPending));
        filterProcessing.setOnClickListener(v -> applyFilter("Đang xử lý", filterProcessing));
        filterShipping.setOnClickListener(v -> applyFilter("Đang giao hàng", filterShipping));
        filterCompleted.setOnClickListener(v -> applyFilter("Đã giao hàng", filterCompleted));
        filterCancelled.setOnClickListener(v -> applyFilter("Đã hủy", filterCancelled));
    }

    private void applyFilter(String status, Button selectedButton) {
        currentFilter = status;

        resetButtonStyle(filterAll);
        resetButtonStyle(filterPending);
        resetButtonStyle(filterProcessing);
        resetButtonStyle(filterShipping);
        resetButtonStyle(filterCompleted);
        resetButtonStyle(filterCancelled);

        selectedButton.setBackgroundColor(getResources().getColor(R.color.amber_600));
        selectedButton.setTextColor(getResources().getColor(R.color.white));

        filteredOrders.clear();
        if (status.equals("Tất cả")) {
            filteredOrders.addAll(allOrders);
        } else {
            for (Order order : allOrders) {
                if (order.status.equals(status)) {
                    filteredOrders.add(order);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void resetButtonStyle(Button button) {
        button.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        button.setTextColor(getResources().getColor(R.color.black));
    }

    private void onOrderClick(Order order) {
        Bundle bundle = new Bundle();
        bundle.putInt("orderId", order.id);
        bundle.putBoolean("isAdmin", true);
        Navigation.findNavController(requireView()).navigate(R.id.orderDetailFragment, bundle);
    }
}
