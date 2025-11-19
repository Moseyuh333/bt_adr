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
    private Runnable onRefreshCallback;

    public OrderAdapter(List<Order> orders) {
        this.orders = orders;
    }

    public OrderAdapter(List<Order> orders, Runnable onRefreshCallback) {
        this.orders = orders;
        this.onRefreshCallback = onRefreshCallback;
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

        holder.orderId.setText("Đơn hàng #" + order.id);
        holder.orderDate.setText(order.orderDate);
        holder.orderTotal.setText(String.format("%,.0f₫", order.total));
        holder.orderStatus.setText(getStatusText(order.status));
        holder.orderItems.setText(order.items.size() + " sản phẩm");

        // Set status color
        int statusColor;
        switch (order.status) {
            case "PENDING":
                statusColor = holder.itemView.getContext().getResources().getColor(android.R.color.holo_orange_dark);
                break;
            case "CONFIRMED":
                statusColor = holder.itemView.getContext().getResources().getColor(android.R.color.holo_blue_dark);
                break;
            case "SHIPPED":
                statusColor = holder.itemView.getContext().getResources().getColor(android.R.color.holo_purple);
                break;
            case "DELIVERED":
                statusColor = holder.itemView.getContext().getResources().getColor(android.R.color.holo_green_dark);
                break;
            case "CANCELLED":
                statusColor = holder.itemView.getContext().getResources().getColor(android.R.color.holo_red_dark);
                break;
            case "RETURNED":
                statusColor = holder.itemView.getContext().getResources().getColor(android.R.color.holo_orange_light);
                break;
            default:
                statusColor = holder.itemView.getContext().getResources().getColor(android.R.color.darker_gray);
        }
        holder.orderStatus.setTextColor(statusColor);

        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("orderId", order.id);
            Navigation.findNavController(v).navigate(R.id.orderDetailFragment, bundle);
        });
    }

    private String getStatusText(String status) {
        switch (status) {
            case "PENDING": return "Chờ xác nhận";
            case "CONFIRMED": return "Đã xác nhận";
            case "SHIPPED": return "Đang giao";
            case "DELIVERED": return "Đã giao";
            case "CANCELLED": return "Đã hủy";
            case "RETURNED": return "Đã hoàn";
            default: return status;
        }
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderId, orderDate, orderTotal, orderStatus, orderItems;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.order_id);
            orderDate = itemView.findViewById(R.id.order_date);
            orderTotal = itemView.findViewById(R.id.order_total);
            orderStatus = itemView.findViewById(R.id.order_status);
            orderItems = itemView.findViewById(R.id.order_items_count);
        }
    }
}

