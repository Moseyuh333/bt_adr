package com.example.bookstore.ui.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bookstore.R;
import com.example.bookstore.adapters.AdminCustomerAdapter;
import com.example.bookstore.database.AppDatabase;
import com.example.bookstore.models.User;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AdminCustomersFragment extends Fragment {
    private AppDatabase database;
    private ExecutorService executorService;
    private AdminCustomerAdapter adapter;
    private List<User> allUsers;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_customers, container, false);
        database = AppDatabase.getInstance(requireContext());
        executorService = Executors.newSingleThreadExecutor();
        allUsers = new ArrayList<>();

        RecyclerView recyclerView = view.findViewById(R.id.admin_customers_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new AdminCustomerAdapter(new AdminCustomerAdapter.OnCustomerActionListener() {
            @Override
            public void onViewProfile(User user) {
                String profile = "👤 Tên: " + user.name + "\n📧 Email: " + user.email + "\n📱 SĐT: " + (user.phone != null ? user.phone : "Chưa cập nhật");
                new AlertDialog.Builder(requireContext()).setTitle("Thông Tin Khách Hàng").setMessage(profile).setPositiveButton("OK", null).show();
            }

            @Override
            public void onToggleBan(User user) {
                executorService.execute(() -> {
                    int userId = Integer.parseInt(user.address);
                    database.userDao().updateUserStatus(userId, user.isBanned);
                    requireActivity().runOnUiThread(() -> {
                        user.isBanned = !user.isBanned;
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "✅ Đã cập nhật!", Toast.LENGTH_SHORT).show();
                    });
                });
            }

            @Override
            public void onEdit(User user) {
                Toast.makeText(getContext(), "Chức năng đang phát triển", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setAdapter(adapter);

        executorService.execute(() -> {
            List<com.example.bookstore.database.entities.User> dbUsers = database.userDao().getAllUsers();
            List<User> users = new ArrayList<>();

            for (com.example.bookstore.database.entities.User db : dbUsers) {
                if (!db.isAdmin()) {
                    User user = new User();
                    user.name = db.getFullName();
                    user.email = db.getEmail();
                    user.phone = db.getPhone();
                    user.role = "Customer";
                    user.isBanned = !db.isActive();
                    user.address = String.valueOf(db.getId());
                    users.add(user);
                }
            }

            requireActivity().runOnUiThread(() -> {
                allUsers.clear();
                allUsers.addAll(users);
                adapter.setUsers(allUsers);
            });
        });

        return view;
    }
}

