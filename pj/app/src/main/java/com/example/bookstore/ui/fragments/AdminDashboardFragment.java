package com.example.bookstore.ui.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.bookstore.MainActivity;
import com.example.bookstore.R;
import com.example.bookstore.database.AppDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AdminDashboardFragment extends Fragment {

    private SharedPreferences sharedPreferences;
    private AppDatabase database;
    private ExecutorService executorService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_dashboard, container, false);

        sharedPreferences = requireActivity().getSharedPreferences("BookstorePrefs", requireContext().MODE_PRIVATE);
        database = AppDatabase.getInstance(requireContext());
        executorService = Executors.newSingleThreadExecutor();

        // Thống kê từ database thật
        TextView totalOrdersText = view.findViewById(R.id.total_orders_text);
        TextView totalUsersText = view.findViewById(R.id.total_users_text);
        TextView totalProductsText = view.findViewById(R.id.total_products_text);
        TextView totalRevenueText = view.findViewById(R.id.total_revenue_text);

        executorService.execute(() -> {
            int totalOrders = database.orderDao().getTotalOrderCount();
            int totalUsers = database.userDao().getAllUsers().size();
            int totalProducts = database.bookDao().getAllBooks().size();
            Double revenue = database.orderDao().getTotalRevenue();
            double totalRevenue = revenue != null ? revenue : 0.0;

            requireActivity().runOnUiThread(() -> {
                if (totalOrdersText != null) totalOrdersText.setText(String.valueOf(totalOrders));
                if (totalUsersText != null) totalUsersText.setText(String.valueOf(totalUsers));
                if (totalProductsText != null) totalProductsText.setText(String.valueOf(totalProducts));
                if (totalRevenueText != null) {
                    totalRevenueText.setText(String.format("%.1fM₫", totalRevenue / 1000000));
                }
            });
        });

        // Card quản lý sản phẩm
        View manageProductsCard = view.findViewById(R.id.manage_products_card);
        if (manageProductsCard != null) {
            manageProductsCard.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_adminDashboardFragment_to_adminProductsFragment)
            );
        }

        // Card quản lý đơn hàng
        View manageOrdersCard = view.findViewById(R.id.manage_orders_card);
        if (manageOrdersCard != null) {
            manageOrdersCard.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_adminDashboardFragment_to_adminOrdersFragment)
            );
        }

        // Card quản lý khách hàng
        View manageCustomersCard = view.findViewById(R.id.manage_customers_card);
        if (manageCustomersCard != null) {
            manageCustomersCard.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_adminDashboardFragment_to_adminCustomersFragment)
            );
        }

        // Nút logout admin
        Button logoutBtn = view.findViewById(R.id.admin_logout_btn);
        if (logoutBtn != null) {
            logoutBtn.setOnClickListener(v -> {
                sharedPreferences.edit()
                        .putBoolean("is_logged_in", false)
                        .putBoolean("is_admin", false)
                        .apply();

                Toast.makeText(getContext(), "🚪 Đã đăng xuất khỏi admin!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(requireActivity(), MainActivity.class);
                startActivity(intent);
                requireActivity().finish();
            });
        }

        return view;
    }
}
