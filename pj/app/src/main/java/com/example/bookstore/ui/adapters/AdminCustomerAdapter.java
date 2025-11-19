package com.example.bookstore.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bookstore.R;
import com.example.bookstore.models.Customer;
import java.util.List;

public class AdminCustomerAdapter extends RecyclerView.Adapter<AdminCustomerAdapter.ViewHolder> {

    private List<Customer> customers;
    private OnCustomerActionListener listener;

    public interface OnCustomerActionListener {
        void onViewDetails(Customer customer);
        void onBanAccount(Customer customer);
        void onUnbanAccount(Customer customer);
        void onDeleteAccount(Customer customer);
    }

    public AdminCustomerAdapter(List<Customer> customers, OnCustomerActionListener listener) {
        this.customers = customers;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_customer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Customer customer = customers.get(position);

        holder.customerNameText.setText(customer.getName());
        holder.customerEmailText.setText(customer.getEmail());
        holder.customerPhoneText.setText(customer.getPhone());
        holder.totalOrdersText.setText(customer.getTotalOrders() + " đơn hàng");
        holder.registrationDateText.setText("Đăng ký: " + customer.getRegistrationDate());

        // Status styling
        if (customer.isActive()) {
            holder.statusText.setText("✅ Hoạt động");
            holder.statusText.setTextColor(0xFF4CAF50);
            holder.statusText.setBackgroundColor(0xFFE8F5E8);

            holder.banBtn.setText("🚫 Khóa");
            holder.banBtn.setBackgroundColor(0xFFF44336);
            holder.banBtn.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onBanAccount(customer);
                }
            });
        } else {
            holder.statusText.setText("🔒 Đã khóa");
            holder.statusText.setTextColor(0xFFF44336);
            holder.statusText.setBackgroundColor(0xFFFFEBEE);

            holder.banBtn.setText("🔓 Mở khóa");
            holder.banBtn.setBackgroundColor(0xFF4CAF50);
            holder.banBtn.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onUnbanAccount(customer);
                }
            });
        }

        // Button actions
        holder.viewDetailsBtn.setOnClickListener(v -> {
            if (listener != null) {
                listener.onViewDetails(customer);
            }
        });

        holder.deleteBtn.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteAccount(customer);
            }
        });
    }

    @Override
    public int getItemCount() {
        return customers.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView customerNameText, customerEmailText, customerPhoneText, totalOrdersText, registrationDateText, statusText;
        Button viewDetailsBtn, banBtn, deleteBtn;

        ViewHolder(View itemView) {
            super(itemView);
            customerNameText = itemView.findViewById(R.id.admin_customer_name);
            customerEmailText = itemView.findViewById(R.id.admin_customer_email);
            customerPhoneText = itemView.findViewById(R.id.admin_customer_phone);
            totalOrdersText = itemView.findViewById(R.id.admin_customer_orders);
            registrationDateText = itemView.findViewById(R.id.admin_customer_reg_date);
            statusText = itemView.findViewById(R.id.admin_customer_status);
            viewDetailsBtn = itemView.findViewById(R.id.admin_view_customer_btn);
            banBtn = itemView.findViewById(R.id.admin_ban_customer_btn);
            deleteBtn = itemView.findViewById(R.id.admin_delete_customer_btn);
        }
    }
}
