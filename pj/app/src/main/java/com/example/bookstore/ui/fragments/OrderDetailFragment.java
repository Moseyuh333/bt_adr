package com.example.bookstore.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore.R;
import com.example.bookstore.adapters.CartAdapter;
import com.example.bookstore.models.Order;
import com.example.bookstore.utils.OrderManager;

public class OrderDetailFragment extends Fragment {

    private Order order;
    private TextView orderIdText, orderDateText, orderStatusText;
    private TextView customerNameText, customerPhoneText, customerAddressText;
    private TextView subtotalText, taxText, discountText, shippingText, totalText;
    private TextView paymentMethodText, voucherCodeText;
    private RecyclerView itemsRecycler;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            // Get order ID from arguments
            int orderId = 0;
            if (getArguments() != null) {
                orderId = getArguments().getInt("orderId", 0);
            }

            // Load order
            order = OrderManager.getInstance(getContext()).getOrderById(orderId);

            if (order == null) {
                return;
            }

            // Initialize views
            orderIdText = view.findViewById(R.id.order_detail_id);
            orderDateText = view.findViewById(R.id.order_detail_date);
            orderStatusText = view.findViewById(R.id.order_detail_status);
            customerNameText = view.findViewById(R.id.order_detail_customer_name);
            customerPhoneText = view.findViewById(R.id.order_detail_customer_phone);
            customerAddressText = view.findViewById(R.id.order_detail_customer_address);
            subtotalText = view.findViewById(R.id.order_detail_subtotal);
            taxText = view.findViewById(R.id.order_detail_tax);
            discountText = view.findViewById(R.id.order_detail_discount);
            shippingText = view.findViewById(R.id.order_detail_shipping);
            totalText = view.findViewById(R.id.order_detail_total);
            paymentMethodText = view.findViewById(R.id.order_detail_payment_method);
            voucherCodeText = view.findViewById(R.id.order_detail_voucher);
            itemsRecycler = view.findViewById(R.id.order_detail_items_recycler);

            // Set order data
            orderIdText.setText("Đơn hàng #" + order.id);
            orderDateText.setText(order.orderDate);
            orderStatusText.setText(getStatusText(order.status));
            customerNameText.setText(order.customerName);
            customerPhoneText.setText(order.customerPhone);
            customerAddressText.setText(order.deliveryAddress);
            subtotalText.setText(String.format("%,.0f₫", order.subtotal));
            taxText.setText(String.format("%,.0f₫", order.tax));
            discountText.setText(String.format("-%,.0f₫", order.discount));
            shippingText.setText(String.format("%,.0f₫", order.shippingFee));
            totalText.setText(String.format("%,.0f₫", order.total));
            paymentMethodText.setText(order.paymentMethod);

            if (order.voucherCode != null && !order.voucherCode.isEmpty()) {
                voucherCodeText.setText("Mã: " + order.voucherCode);
                voucherCodeText.setVisibility(View.VISIBLE);
            } else {
                voucherCodeText.setVisibility(View.GONE);
            }

            // Set status color
            int statusColor;
            switch (order.status) {
                case "PENDING":
                    statusColor = getResources().getColor(android.R.color.holo_orange_dark);
                    break;
                case "CONFIRMED":
                    statusColor = getResources().getColor(android.R.color.holo_blue_dark);
                    break;
                case "SHIPPED":
                    statusColor = getResources().getColor(android.R.color.holo_purple);
                    break;
                case "DELIVERED":
                    statusColor = getResources().getColor(android.R.color.holo_green_dark);
                    break;
                case "CANCELLED":
                    statusColor = getResources().getColor(android.R.color.holo_red_dark);
                    break;
                default:
                    statusColor = getResources().getColor(android.R.color.darker_gray);
            }
            orderStatusText.setTextColor(statusColor);

            // Setup items recycler
            itemsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
            itemsRecycler.setAdapter(new CartAdapter(order.items, () -> {}));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getStatusText(String status) {
        switch (status) {
            case "PENDING": return "Chờ xác nhận";
            case "CONFIRMED": return "Đã xác nhận";
            case "SHIPPED": return "Đang giao";
            case "DELIVERED": return "Đã giao";
            case "CANCELLED": return "Đã hủy";
            default: return status;
        }
    }
}

