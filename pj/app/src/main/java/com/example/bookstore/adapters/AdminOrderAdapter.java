package com.example.bookstore.adapters;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdminOrderAdapter extends RecyclerView.Adapter<AdminOrderAdapter.ViewHolder> {

    private List<Order> orders;
    private OnOrderActionListener listener;

    public interface OnOrderActionListener {
        void onChangeStatus(Order order);
        void onViewDetails(Order order);
    }

    public AdminOrderAdapter(OnOrderActionListener listener) {
        this.orders = new ArrayList<>();
        this.listener = listener;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
        notifyDataSetChanged();
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

        holder.orderIdText.setText("Mã: " + order.getId());
        holder.customerText.setText("👤 " + order.getCustomerName());
        holder.addressText.setText("📍 " + order.getShippingAddress());
        holder.dateText.setText("📅 " + order.getOrderDate());

        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        holder.amountText.setText(formatter.format(order.getTotalAmount()) + "₫");

        // Status styling
        holder.statusText.setText(getStatusDisplay(order.getStatus()));
        holder.statusText.setBackgroundResource(getStatusBackground(order.getStatus()));

        holder.changeStatusBtn.setOnClickListener(v -> {
            if (listener != null) listener.onChangeStatus(order);
        });

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onViewDetails(order);
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    private String getStatusDisplay(String status) {
        switch (status) {
            case "Pending": return "⏳ Chờ xử lý";
            case "Processing": return "📦 Đang xử lý";
            case "Shipped": return "🚚 Đang giao";
            case "Delivered": return "✅ Đã giao";
            case "Cancelled": return "❌ Đã hủy";
            default: return status;
        }
    }

    private int getStatusBackground(String status) {
        switch (status) {
            case "Pending": return R.drawable.status_pending_bg;
            case "Processing": return R.drawable.status_processing_bg;
            case "Shipped": return R.drawable.status_shipped_bg;
            case "Delivered": return R.drawable.status_delivered_bg;
            case "Cancelled": return R.drawable.status_cancelled_bg;
            default: return R.drawable.status_pending_bg;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderIdText, customerText, addressText, dateText, amountText, statusText;
        Button changeStatusBtn;

        ViewHolder(View view) {
            super(view);
            orderIdText = view.findViewById(R.id.order_id_text);
            customerText = view.findViewById(R.id.order_customer_text);
            addressText = view.findViewById(R.id.order_address_text);
            dateText = view.findViewById(R.id.order_date_text);
            amountText = view.findViewById(R.id.order_amount_text);
            statusText = view.findViewById(R.id.order_status_text);
            changeStatusBtn = view.findViewById(R.id.btn_change_status);
        }
    }
}

