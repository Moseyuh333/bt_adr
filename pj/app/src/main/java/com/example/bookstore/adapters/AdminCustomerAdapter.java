package com.example.bookstore.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bookstore.R;
import com.example.bookstore.models.User;
import java.util.ArrayList;
import java.util.List;

public class AdminCustomerAdapter extends RecyclerView.Adapter<AdminCustomerAdapter.ViewHolder> {

    private List<User> users;
    private OnCustomerActionListener listener;

    public interface OnCustomerActionListener {
        void onViewProfile(User user);
        void onToggleBan(User user);
        void onEdit(User user);
    }

    public AdminCustomerAdapter(OnCustomerActionListener listener) {
        this.users = new ArrayList<>();
        this.listener = listener;
    }

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_customer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);

        holder.nameText.setText(user.name);
        holder.emailText.setText("📧 " + user.email);
        holder.roleText.setText(user.role.equals("admin") ? "👑 Admin" : "👤 Khách hàng");

        // Ban status
        if (user.isBanned) {
            holder.statusText.setText("🚫 Đã khóa");
            holder.statusText.setTextColor(0xFFF44336);
            holder.banBtn.setText("Mở khóa");
        } else {
            holder.statusText.setText("✅ Hoạt động");
            holder.statusText.setTextColor(0xFF4CAF50);
            holder.banBtn.setText("Khóa TK");
        }

        holder.viewBtn.setOnClickListener(v -> {
            if (listener != null) listener.onViewProfile(user);
        });

        holder.banBtn.setOnClickListener(v -> {
            if (listener != null) listener.onToggleBan(user);
        });

        holder.editBtn.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(user);
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, emailText, roleText, statusText;
        Button viewBtn, banBtn, editBtn;

        ViewHolder(View view) {
            super(view);
            nameText = view.findViewById(R.id.customer_name);
            emailText = view.findViewById(R.id.customer_email);
            roleText = view.findViewById(R.id.customer_role);
            statusText = view.findViewById(R.id.customer_status);
            viewBtn = view.findViewById(R.id.btn_view_customer);
            banBtn = view.findViewById(R.id.btn_ban_customer);
            editBtn = view.findViewById(R.id.btn_edit_customer);
        }
    }
}

