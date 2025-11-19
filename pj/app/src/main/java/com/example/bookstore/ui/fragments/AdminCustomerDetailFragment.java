package com.example.bookstore.ui.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.bookstore.R;
import com.example.bookstore.models.User;

public class AdminCustomerDetailFragment extends Fragment {
    private User user;
    private TextView nameText, emailText, roleText, statusText;
    private Button toggleBanBtn, viewOrdersBtn, deleteCustomerBtn, backBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_customer_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) user = (User) getArguments().getSerializable("user");
        nameText = view.findViewById(R.id.customer_name);
        emailText = view.findViewById(R.id.customer_email);
        roleText = view.findViewById(R.id.customer_role);
        statusText = view.findViewById(R.id.customer_status);
        toggleBanBtn = view.findViewById(R.id.toggle_ban_btn);
        viewOrdersBtn = view.findViewById(R.id.view_orders_btn);
        deleteCustomerBtn = view.findViewById(R.id.delete_customer_btn);
        backBtn = view.findViewById(R.id.back_btn);
        if (user != null) {
            nameText.setText(user.name);
            emailText.setText(user.email);
            roleText.setText(user.role.equals("admin") ? "Quản trị viên" : "Khách hàng");
            updateStatusDisplay();
        }
        backBtn.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());
        toggleBanBtn.setOnClickListener(v -> handleToggleBan());
        viewOrdersBtn.setOnClickListener(v -> Toast.makeText(getContext(), "Xem đơn hàng của khách hàng", Toast.LENGTH_SHORT).show());
        deleteCustomerBtn.setOnClickListener(v -> handleDeleteCustomer());
    }

    private void updateStatusDisplay() {
        if (user.isBanned) {
            statusText.setText("🔒 Đã khóa");
            statusText.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            toggleBanBtn.setText("🔓 Mở Khóa Tài Khoản");
            toggleBanBtn.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
        } else {
            statusText.setText("✅ Hoạt động");
            statusText.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            toggleBanBtn.setText("🔒 Khóa Tài Khoản");
            toggleBanBtn.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
        }
    }

    private void handleToggleBan() {
        String action = user.isBanned ? "mở khóa" : "khóa";
        new AlertDialog.Builder(getContext()).setTitle("Xác nhận").setMessage("Bạn có chắc muốn " + action + " tài khoản này?")
            .setPositiveButton("Xác nhận", (dialog, which) -> {
                user.isBanned = !user.isBanned;
                updateStatusDisplay();
                Toast.makeText(getContext(), user.isBanned ? "Đã khóa tài khoản" : "Đã mở khóa tài khoản", Toast.LENGTH_SHORT).show();
            }).setNegativeButton("Hủy", null).show();
    }

    private void handleDeleteCustomer() {
        new AlertDialog.Builder(getContext()).setTitle("Xác nhận xóa")
            .setMessage("Bạn có chắc muốn xóa tài khoản này? Hành động này không thể hoàn tác!")
            .setPositiveButton("Xóa", (dialog, which) -> {
                Toast.makeText(getContext(), "Đã xóa tài khoản", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(requireView()).popBackStack();
            }).setNegativeButton("Hủy", null).show();
    }
}

