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
import com.example.bookstore.utils.DataManager;

public class AdminDashboardFragment extends Fragment {

    private SharedPreferences sharedPreferences;
    private DataManager dataManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_dashboard, container, false);

        sharedPreferences = requireActivity().getSharedPreferences("BookstorePrefs", requireContext().MODE_PRIVATE);
        dataManager = DataManager.getInstance(requireContext());

        // Thống kê (thực tế từ DataManager)
        TextView totalOrdersText = view.findViewById(R.id.total_orders_text);
        TextView totalUsersText = view.findViewById(R.id.total_users_text);
        TextView totalProductsText = view.findViewById(R.id.total_products_text);
        TextView totalRevenueText = view.findViewById(R.id.total_revenue_text);

        if (totalOrdersText != null) totalOrdersText.setText(String.valueOf(dataManager.getAllOrders().size()));
        if (totalUsersText != null) totalUsersText.setText(String.valueOf(dataManager.getAllUsers().size()));
        if (totalProductsText != null) totalProductsText.setText(String.valueOf(dataManager.getAllBooks().size()));

        // Tính tổng doanh thu
        double totalRevenue = 0;
        for (var order : dataManager.getAllOrders()) {
            if (!order.getStatus().equals("Cancelled")) {
                totalRevenue += order.getTotalAmount();
            }
        }
        if (totalRevenueText != null) {
            totalRevenueText.setText(String.format("%.1fM₫", totalRevenue / 1000000));
        }

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
