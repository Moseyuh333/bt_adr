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
import com.example.bookstore.database.AppDatabase;
import com.example.bookstore.models.Order;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AdminOrdersFragment extends Fragment {

    private AppDatabase database;
    private ExecutorService executorService;
    private AdminOrderAdapter adapter;
    private TabLayout tabLayout;
    private String currentStatusFilter = "All";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_orders, container, false);

        database = AppDatabase.getInstance(requireContext());
        executorService = Executors.newSingleThreadExecutor();

        tabLayout = view.findViewById(R.id.order_status_tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Tất cả"));
        tabLayout.addTab(tabLayout.newTab().setText("Chờ xử lý"));
        tabLayout.addTab(tabLayout.newTab().setText("Đang xử lý"));
        tabLayout.addTab(tabLayout.newTab().setText("Đang giao"));
        tabLayout.addTab(tabLayout.newTab().setText("Đã giao"));
        tabLayout.addTab(tabLayout.newTab().setText("Đã hủy"));

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
        executorService.execute(() -> {
            List<com.example.bookstore.database.entities.Order> dbOrders;

            if (status.equals("All")) {
                dbOrders = database.orderDao().getAllOrders();
            } else {
                dbOrders = database.orderDao().getOrdersByStatus(mapStatusToDb(status));
            }

            final List<com.example.bookstore.database.entities.Order> finalOrders = dbOrders;
            requireActivity().runOnUiThread(() -> {
                List<Order> orders = convertToOldOrders(finalOrders != null ? finalOrders : new java.util.ArrayList<>());
                adapter.setOrders(orders);
            });
        });
    }

    private String mapStatusToDb(String displayStatus) {
        switch (displayStatus) {
            case "Pending": return "PENDING";
            case "Processing": return "CONFIRMED";
            case "Shipped": return "SHIPPING";
            case "Delivered": return "DELIVERED";
            case "Cancelled": return "CANCELLED";
            default: return displayStatus;
        }
    }

    private List<Order> convertToOldOrders(List<com.example.bookstore.database.entities.Order> dbOrders) {
        List<Order> orders = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

        for (com.example.bookstore.database.entities.Order db : dbOrders) {
            Order order = new Order();
            order.id = db.getOrderNumber();
            order.orderDate = sdf.format(new Date(db.getCreatedAt()));
            order.status = mapDbStatusToDisplay(db.getStatus());
            order.total = db.getTotalAmount();
            order.customerName = db.getRecipientName();
            order.customerPhone = db.getRecipientPhone();
            order.deliveryAddress = db.getShippingAddress();
            order.paymentMethod = db.getPaymentMethod();
            orders.add(order);
        }

        return orders;
    }

    private String mapDbStatusToDisplay(String dbStatus) {
        switch (dbStatus) {
            case "PENDING": return "Pending";
            case "CONFIRMED": return "Processing";
            case "SHIPPING": return "Shipped";
            case "DELIVERED": return "Delivered";
            case "CANCELLED": return "Cancelled";
            default: return dbStatus;
        }
    }

    private void showChangeStatusDialog(Order order) {
        String[] statuses = {"Pending", "Processing", "Shipped", "Delivered", "Cancelled"};
        String[] displayStatuses = {"⏳ Chờ xử lý", "📦 Đang xử lý", "🚚 Đang giao", "✅ Đã giao", "❌ Đã hủy"};

        new AlertDialog.Builder(requireContext())
            .setTitle("🔄 Thay Đổi Trạng Thái Đơn: " + order.id)
            .setItems(displayStatuses, (dialog, which) -> {
                order.status = statuses[which];
                adapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "✅ Đã cập nhật trạng thái!", Toast.LENGTH_SHORT).show();
            })
            .setNegativeButton("Hủy", null)
            .show();
    }

    private void showOrderDetailsDialog(Order order) {
        String details = "📦 Đơn hàng: " + order.id + "\n\n" +
                "👤 Khách hàng: " + order.customerName + "\n" +
                "📞 SĐT: " + order.customerPhone + "\n" +
                "📍 Địa chỉ: " + order.deliveryAddress + "\n" +
                "💰 Tổng tiền: " + String.format("%,.0f₫", order.total) + "\n" +
                "💳 Thanh toán: " + order.paymentMethod + "\n" +
                "📅 Ngày đặt: " + order.orderDate + "\n" +
                "📊 Trạng thái: " + order.status;

        new AlertDialog.Builder(requireContext())
            .setTitle("Chi Tiết Đơn Hàng")
            .setMessage(details)
            .setPositiveButton("Đóng", null)
            .show();
    }
}
