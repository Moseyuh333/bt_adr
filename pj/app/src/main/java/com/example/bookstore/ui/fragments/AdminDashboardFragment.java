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
import com.example.bookstore.MainActivity;
import com.example.bookstore.R;

public class AdminDashboardFragment extends Fragment {

    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_dashboard, container, false);

        sharedPreferences = requireActivity().getSharedPreferences("BookstorePrefs", requireContext().MODE_PRIVATE);

        // Thống kê (mock data)
        TextView totalOrdersText = view.findViewById(R.id.total_orders_text);
        TextView totalUsersText = view.findViewById(R.id.total_users_text);
        TextView totalProductsText = view.findViewById(R.id.total_products_text);
        TextView totalRevenueText = view.findViewById(R.id.total_revenue_text);

        if (totalOrdersText != null) totalOrdersText.setText("127");
        if (totalUsersText != null) totalUsersText.setText("45");
        if (totalProductsText != null) totalProductsText.setText("89");
        if (totalRevenueText != null) totalRevenueText.setText("2.5M₫");

        // Card quản lý sản phẩm
        View manageProductsCard = view.findViewById(R.id.manage_products_card);
        if (manageProductsCard != null) {
            manageProductsCard.setOnClickListener(v ->
                Toast.makeText(getContext(), "📚 Quản lý sản phẩm: Thêm, sửa, xóa sách", Toast.LENGTH_SHORT).show()
            );
        }

        // Card quản lý đơn hàng
        View manageOrdersCard = view.findViewById(R.id.manage_orders_card);
        if (manageOrdersCard != null) {
            manageOrdersCard.setOnClickListener(v ->
                Toast.makeText(getContext(), "📦 Quản lý đơn hàng: Thay đổi trạng thái, hủy đơn", Toast.LENGTH_SHORT).show()
            );
        }

        // Card quản lý khách hàng
        View manageCustomersCard = view.findViewById(R.id.manage_customers_card);
        if (manageCustomersCard != null) {
            manageCustomersCard.setOnClickListener(v ->
                Toast.makeText(getContext(), "👥 Quản lý khách hàng: Xem profile, khóa tài khoản", Toast.LENGTH_SHORT).show()
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
