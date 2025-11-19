package com.example.bookstore.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bookstore.R;
import com.example.bookstore.models.User;
import java.util.List;

public class AdminCustomerAdapter extends RecyclerView.Adapter<AdminCustomerAdapter.ViewHolder> {
    private List<User> customers;
    private OnCustomerClickListener listener;

    public interface OnCustomerClickListener {
        void onClick(User user);
    }

    public AdminCustomerAdapter(List<User> customers, OnCustomerClickListener listener) {
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
        User user = customers.get(position);
        holder.name.setText(user.name);
        holder.email.setText(user.email);
        holder.role.setText(user.role.equals("admin") ? "Quản trị viên" : "Khách hàng");
        holder.status.setText(user.isBanned ? "🔒 Đã khóa" : "✅ Hoạt động");
        holder.status.setTextColor(holder.itemView.getContext().getColor(user.isBanned ? android.R.color.holo_red_dark : android.R.color.holo_green_dark));
        holder.itemView.setOnClickListener(v -> listener.onClick(user));
    }

    @Override
    public int getItemCount() { return customers.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, email, role, status;
        ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.customer_name);
            email = view.findViewById(R.id.customer_email);
            role = view.findViewById(R.id.customer_role);
            status = view.findViewById(R.id.customer_status);
        }
    }
}
