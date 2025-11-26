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
        try {
            if (orders == null || position >= orders.size()) return;

            Order order = orders.get(position);
            if (order == null) return;

            // Set order ID with null check
            if (holder.orderIdText != null) {
                String orderId = order.id != null ? order.id : "N/A";
                holder.orderIdText.setText("Đơn hàng: #" + orderId);
            }

            // Set order date with null check
            if (holder.orderDateText != null) {
                String orderDate = order.orderDate != null ? order.orderDate : "N/A";
                holder.orderDateText.setText("Ngày đặt: " + orderDate);
            }

            // Set status with null check
            if (holder.orderStatusText != null) {
                String status = order.status != null ? order.status : "PENDING";
                holder.orderStatusText.setText(getStatusText(status));

                // Set status color safely
                try {
                    int statusColor = getStatusColor(holder.itemView, status);
                    holder.orderStatusText.setTextColor(statusColor);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // Set total with null check
            if (holder.orderTotalText != null) {
                double total = order.total > 0 ? order.total : (order.totalAmount > 0 ? order.totalAmount : 0);
                holder.orderTotalText.setText(String.format("Tổng: %,.0f₫", total));
            }

            // Set click listener with try-catch
            if (holder.itemView != null) {
                holder.itemView.setOnClickListener(v -> {
                    try {
                        // Navigate to order detail
                        Bundle bundle = new Bundle();

                        // Parse orderId safely
                        int orderId = 0;
                        if (order.id != null) {
                            try {
                                String idStr = order.id.replace("ORD", "").trim();
                                orderId = Integer.parseInt(idStr);
                            } catch (NumberFormatException e) {
                                // If parsing fails, use position as ID
                                orderId = position + 1;
                            }
                        } else {
                            orderId = position + 1;
                        }

                        bundle.putInt("orderId", orderId);

                        // Navigate safely
                        try {
                            Navigation.findNavController(v).navigate(R.id.orderDetailFragment, bundle);
                        } catch (Exception navEx) {
                            navEx.printStackTrace();
                            // Show toast if navigation fails
                            android.widget.Toast.makeText(v.getContext(),
                                "Không thể mở chi tiết đơn hàng",
                                android.widget.Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        android.widget.Toast.makeText(v.getContext(),
                            "Lỗi: " + e.getMessage(),
                            android.widget.Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        try {
            if (view == null || view.getContext() == null || view.getContext().getResources() == null) {
                return 0xFF000000; // Black as default
            }

            if (status == null) status = "PENDING";

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
        } catch (Exception e) {
            e.printStackTrace();
            return 0xFF000000; // Black as fallback
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

