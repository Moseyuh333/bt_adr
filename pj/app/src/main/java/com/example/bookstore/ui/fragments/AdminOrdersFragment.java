package com.example.bookstore.ui.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bookstore.R;
import com.example.bookstore.adapters.AdminOrderAdapter;
import com.example.bookstore.models.Order;
import com.example.bookstore.utils.DataManager;
import com.google.android.material.tabs.TabLayout;
import java.util.List;

public class AdminOrdersFragment extends Fragment {

    private DataManager dataManager;
    private AdminOrderAdapter adapter;
    private TabLayout tabLayout;
    private String currentStatusFilter = "All";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_orders, container, false);

        dataManager = DataManager.getInstance(requireContext());

        // Setup Tabs
        tabLayout = view.findViewById(R.id.order_status_tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Tất cả"));
        tabLayout.addTab(tabLayout.newTab().setText("Chờ xử lý"));
        tabLayout.addTab(tabLayout.newTab().setText("Đang xử lý"));
        tabLayout.addTab(tabLayout.newTab().setText("Đang giao"));
        tabLayout.addTab(tabLayout.newTab().setText("Đã giao"));
        tabLayout.addTab(tabLayout.newTab().setText("Đã hủy"));

        // Setup RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.admin_orders_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new AdminOrderAdapter(new AdminOrderAdapter.OnOrderActionListener() {
            @Override
            public void onChangeStatus(Order order) {
                showChangeStatusDialog(order);
            }

            @Override
            public void onViewDetails(Order order) {
                showOrderDetailsDialog(order);
            }
        });

        recyclerView.setAdapter(adapter);
        loadOrders("All");

        // Tab change listener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0: loadOrders("All"); break;
                    case 1: loadOrders("Pending"); break;
                    case 2: loadOrders("Processing"); break;
                    case 3: loadOrders("Shipped"); break;
                    case 4: loadOrders("Delivered"); break;
                    case 5: loadOrders("Cancelled"); break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        return view;
    }

    private void loadOrders(String status) {
        currentStatusFilter = status;
        List<Order> orders = status.equals("All") ?
            dataManager.getAllOrders() :
            dataManager.getOrdersByStatus(status);
        adapter.setOrders(orders);
    }

    private void showChangeStatusDialog(Order order) {
        String[] statuses = {"Pending", "Processing", "Shipped", "Delivered", "Cancelled"};
        String[] displayStatuses = {"⏳ Chờ xử lý", "📦 Đang xử lý", "🚚 Đang giao", "✅ Đã giao", "❌ Đã hủy"};

        new AlertDialog.Builder(requireContext())
            .setTitle("🔄 Thay Đổi Trạng Thái Đơn: " + order.getId())
            .setItems(displayStatuses, (dialog, which) -> {
                String newStatus = statuses[which];
                dataManager.updateOrderStatus(order.getId(), newStatus);
                loadOrders(currentStatusFilter);
                Toast.makeText(getContext(), "✅ Đã cập nhật trạng thái: " + displayStatuses[which], Toast.LENGTH_SHORT).show();
            })
            .setNegativeButton("Hủy", null)
            .show();
    }

    private void showOrderDetailsDialog(Order order) {
        String details = "📦 Mã đơn: " + order.getId() + "\n" +
                        "👤 Khách hàng: " + order.getCustomerName() + "\n" +
                        "📍 Địa chỉ: " + order.getShippingAddress() + "\n" +
                        "📅 Ngày đặt: " + order.getOrderDate() + "\n" +
                        "💰 Tổng tiền: " + String.format("%,.0f₫", order.getTotalAmount()) + "\n" +
                        "📊 Trạng thái: " + order.getStatus();

        new AlertDialog.Builder(requireContext())
            .setTitle("Chi Tiết Đơn Hàng")
            .setMessage(details)
            .setPositiveButton("OK", null)
            .show();
    }
}
