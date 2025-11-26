package com.example.bookstore.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore.R;
import com.example.bookstore.models.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orders;

    public OrderAdapter(List<Order> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);

        holder.orderIdText.setText("Đơn hàng: #" + order.id);
        holder.orderDateText.setText("Ngày đặt: " + order.orderDate);
        holder.orderStatusText.setText(getStatusText(order.status));
        holder.orderTotalText.setText(String.format("Tổng: %,.0f₫", order.total));

        // Set status color
        int statusColor = getStatusColor(holder.itemView, order.status);
        holder.orderStatusText.setTextColor(statusColor);

        holder.itemView.setOnClickListener(v -> {
            // Navigate to order detail
            Bundle bundle = new Bundle();
            bundle.putInt("orderId", Integer.parseInt(order.id.replace("ORD", "")));
            Navigation.findNavController(v).navigate(R.id.orderDetailFragment, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    private String getStatusText(String status) {
        switch (status) {
            case "PENDING": return "Chờ xác nhận";
            case "CONFIRMED": return "Đã xác nhận";
            case "SHIPPED": return "Đang giao hàng";
            case "DELIVERED": return "Đã giao hàng";
            case "CANCELLED": return "Đã hủy";
            case "RETURNED": return "Đã hoàn trả";
            default: return status;
        }
    }

    private int getStatusColor(View view, String status) {
        switch (status) {
            case "PENDING":
                return view.getContext().getResources().getColor(android.R.color.holo_orange_dark);
            case "CONFIRMED":
                return view.getContext().getResources().getColor(android.R.color.holo_blue_dark);
            case "SHIPPED":
                return view.getContext().getResources().getColor(android.R.color.holo_purple);
            case "DELIVERED":
                return view.getContext().getResources().getColor(android.R.color.holo_green_dark);
            case "CANCELLED":
            case "RETURNED":
                return view.getContext().getResources().getColor(android.R.color.holo_red_dark);
            default:
                return view.getContext().getResources().getColor(android.R.color.black);
        }
    }

    public void updateOrders(List<Order> newOrders) {
        this.orders = newOrders;
        notifyDataSetChanged();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderIdText, orderDateText, orderStatusText, orderTotalText;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdText = itemView.findViewById(R.id.order_id);
            orderDateText = itemView.findViewById(R.id.order_date);
            orderStatusText = itemView.findViewById(R.id.order_status);
            orderTotalText = itemView.findViewById(R.id.order_total);
        }
    }
}

