package com.example.bookstore.ui.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bookstore.R;
import com.example.bookstore.adapters.AdminCustomerAdapter;
import com.example.bookstore.models.User;
import com.example.bookstore.utils.DataManager;
import java.util.List;

public class AdminCustomersFragment extends Fragment {

    private DataManager dataManager;
    private AdminCustomerAdapter adapter;
    private List<User> allUsers;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_customers, container, false);

        dataManager = DataManager.getInstance(requireContext());
        allUsers = dataManager.getAllUsers();

        // Setup RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.admin_customers_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new AdminCustomerAdapter(new AdminCustomerAdapter.OnCustomerActionListener() {
            @Override
            public void onViewProfile(User user) {
                showUserProfileDialog(user);
            }

            @Override
            public void onToggleBan(User user) {
                toggleBanUser(user);
            }

            @Override
            public void onEdit(User user) {
                showEditUserDialog(user);
            }
        });

        recyclerView.setAdapter(adapter);
        adapter.setUsers(allUsers);

        return view;
    }

    private void showUserProfileDialog(User user) {
        String profile = "👤 Tên: " + user.name + "\n" +
                        "📧 Email: " + user.email + "\n" +
                        "📱 SĐT: " + (user.phone != null ? user.phone : "Chưa cập nhật") + "\n" +
                        "📍 Địa chỉ: " + (user.address != null ? user.address : "Chưa cập nhật") + "\n" +
                        "👑 Vai trò: " + user.role + "\n" +
                        "📊 Trạng thái: " + (user.isBanned ? "🚫 Đã khóa" : "✅ Hoạt động");

        new AlertDialog.Builder(requireContext())
            .setTitle("Thông Tin Khách Hàng")
            .setMessage(profile)
            .setPositiveButton("OK", null)
            .show();
    }

    private void toggleBanUser(User user) {
        String action = user.isBanned ? "mở khóa" : "khóa";
        String message = user.isBanned ?
            "Bạn có chắc muốn mở khóa tài khoản \"" + user.name + "\"?" :
            "Bạn có chắc muốn khóa tài khoản \"" + user.name + "\"?\n\nNgười dùng sẽ không thể đăng nhập và mua hàng.";

        new AlertDialog.Builder(requireContext())
            .setTitle((user.isBanned ? "🔓 " : "🚫 ") + "Xác nhận " + action)
            .setMessage(message)
            .setPositiveButton(action.toUpperCase(), (dialog, which) -> {
                dataManager.toggleUserBan(user.id);
                allUsers = dataManager.getAllUsers();
                adapter.setUsers(allUsers);
                Toast.makeText(getContext(), "✅ Đã " + action + " tài khoản: " + user.name, Toast.LENGTH_SHORT).show();
            })
            .setNegativeButton("Hủy", null)
            .show();
    }

    private void showEditUserDialog(User user) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_customer, null);
        EditText nameInput = dialogView.findViewById(R.id.input_customer_name);
        EditText emailInput = dialogView.findViewById(R.id.input_customer_email);
        EditText phoneInput = dialogView.findViewById(R.id.input_customer_phone);
        EditText addressInput = dialogView.findViewById(R.id.input_customer_address);

        // Pre-fill current values
        nameInput.setText(user.name);
        emailInput.setText(user.email);
        phoneInput.setText(user.phone != null ? user.phone : "");
        addressInput.setText(user.address != null ? user.address : "");

        new AlertDialog.Builder(requireContext())
            .setTitle("✏️ Chỉnh Sửa Khách Hàng")
            .setView(dialogView)
            .setPositiveButton("Lưu", (dialog, which) -> {
                user.name = nameInput.getText().toString().trim();
                user.email = emailInput.getText().toString().trim();
                user.phone = phoneInput.getText().toString().trim();
                user.address = addressInput.getText().toString().trim();

                dataManager.updateUser(user);
                allUsers = dataManager.getAllUsers();
                adapter.setUsers(allUsers);

                Toast.makeText(getContext(), "✅ Đã cập nhật thông tin: " + user.name, Toast.LENGTH_SHORT).show();
            })
            .setNegativeButton("Hủy", null)
            .show();
    }
}
