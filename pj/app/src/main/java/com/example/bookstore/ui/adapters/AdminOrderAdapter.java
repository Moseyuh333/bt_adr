package com.example.bookstore.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bookstore.R;
import com.example.bookstore.models.Order;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdminOrderAdapter extends RecyclerView.Adapter<AdminOrderAdapter.ViewHolder> {

    private List<Order> orders;
    private OnOrderActionListener listener;

    public interface OnOrderActionListener {
        void onChangeStatus(Order order);
        void onCancelOrder(Order order);
        void onViewDetails(Order order);
    }

    public AdminOrderAdapter(List<Order> orders, OnOrderActionListener listener) {
        this.orders = orders;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orders.get(position);

        holder.orderIdText.setText("#" + order.getId());
        holder.customerNameText.setText(order.getCustomerName());
        holder.orderDateText.setText(order.getOrderDate());

        // Format price
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        holder.totalAmountText.setText(formatter.format(order.getTotalAmount()));

        // Status styling
        holder.orderStatusText.setText(order.getStatus());
        int statusColor;
        int statusBgColor;

        switch (order.getStatus()) {
            case "Chờ xử lý":
                statusColor = 0xFFFF9800; // Orange
                statusBgColor = 0xFFFFF3E0;
                break;
            case "Đang giao":
                statusColor = 0xFF2196F3; // Blue
                statusBgColor = 0xFFE3F2FD;
                break;
            case "Hoàn thành":
                statusColor = 0xFF4CAF50; // Green
                statusBgColor = 0xFFE8F5E8;
                break;
            case "Đã hủy":
                statusColor = 0xFFF44336; // Red
                statusBgColor = 0xFFFFEBEE;
                break;
            default:
                statusColor = 0xFF757575; // Gray
                statusBgColor = 0xFFF5F5F5;
        }

        holder.orderStatusText.setTextColor(statusColor);
        holder.orderStatusText.setBackgroundColor(statusBgColor);

        // Button actions
        holder.changeStatusBtn.setOnClickListener(v -> {
            if (listener != null) {
                listener.onChangeStatus(order);
            }
        });

        holder.cancelOrderBtn.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCancelOrder(order);
            }
        });

        holder.viewDetailsBtn.setOnClickListener(v -> {
            if (listener != null) {
                listener.onViewDetails(order);
            }
        });

        // Disable cancel button for completed/cancelled orders
        boolean canCancel = !"Hoàn thành".equals(order.getStatus()) && !"Đã hủy".equals(order.getStatus());
        holder.cancelOrderBtn.setEnabled(canCancel);
        holder.cancelOrderBtn.setAlpha(canCancel ? 1.0f : 0.5f);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderIdText, customerNameText, orderDateText, totalAmountText, orderStatusText;
        Button changeStatusBtn, cancelOrderBtn, viewDetailsBtn;

        ViewHolder(View itemView) {
            super(itemView);
            orderIdText = itemView.findViewById(R.id.admin_order_id);
            customerNameText = itemView.findViewById(R.id.admin_customer_name);
            orderDateText = itemView.findViewById(R.id.admin_order_date);
            totalAmountText = itemView.findViewById(R.id.admin_order_total);
            orderStatusText = itemView.findViewById(R.id.admin_order_status);
            changeStatusBtn = itemView.findViewById(R.id.admin_change_status_btn);
            cancelOrderBtn = itemView.findViewById(R.id.admin_cancel_order_btn);
            viewDetailsBtn = itemView.findViewById(R.id.admin_view_details_btn);
        }
    }
}
