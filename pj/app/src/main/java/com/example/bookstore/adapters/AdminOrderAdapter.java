package com.example.bookstore.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore.R;
import com.example.bookstore.models.Order;

import java.util.List;

public class AdminOrderAdapter extends RecyclerView.Adapter<AdminOrderAdapter.ViewHolder> {
    private final List<Order> orders;
    private final OnOrderClickListener listener;

    public interface OnOrderClickListener {
        void onClick(Order order);
    }

    public AdminOrderAdapter(List<Order> orders, OnOrderClickListener listener) {
        this.orders = orders;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orders.get(position);

        holder.orderId.setText("Đơn hàng #" + order.id);
        holder.orderDate.setText(order.orderDate);
        holder.totalAmount.setText(String.format("%,.0f₫", order.total));
        holder.status.setText(order.status);

        int statusColor;
        switch (order.status) {
            case "Chờ xác nhận":
                statusColor = holder.itemView.getContext().getColor(android.R.color.holo_orange_dark);
                break;
            case "Đang xử lý":
                statusColor = holder.itemView.getContext().getColor(android.R.color.holo_blue_dark);
                break;
            case "Đang giao hàng":
                statusColor = holder.itemView.getContext().getColor(android.R.color.holo_purple);
                break;
            case "Đã giao hàng":
                statusColor = holder.itemView.getContext().getColor(android.R.color.holo_green_dark);
                break;
            case "Đã hủy":
                statusColor = holder.itemView.getContext().getColor(android.R.color.holo_red_dark);
                break;
            default:
                statusColor = holder.itemView.getContext().getColor(android.R.color.darker_gray);
        }
        holder.status.setTextColor(statusColor);

        holder.itemView.setOnClickListener(v -> listener.onClick(order));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderId, orderDate, totalAmount, status;

        ViewHolder(@NonNull View view) {
            super(view);
            orderId = view.findViewById(R.id.order_id);
            orderDate = view.findViewById(R.id.order_date);
            totalAmount = view.findViewById(R.id.order_total);
            status = view.findViewById(R.id.order_status);
        }
    }
}
