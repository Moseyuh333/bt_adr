package com.example.bookstore.ui.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore.R;
import com.example.bookstore.adapters.CartAdapter;
import com.example.bookstore.models.Order;
import com.example.bookstore.models.OrderReview;
import com.example.bookstore.utils.OrderManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderDetailFragment extends Fragment {

    private Order order;
    private TextView orderIdText, orderDateText, orderStatusText;
    private TextView customerNameText, customerPhoneText, customerAddressText;
    private TextView subtotalText, taxText, discountText, shippingText, totalText;
    private TextView paymentMethodText, voucherCodeText;
    private TextView cancelReasonText, returnReasonText, reviewSectionText;
    private RecyclerView itemsRecycler;
    private Button cancelOrderBtn, confirmReceiptBtn, returnOrderBtn, reviewOrderBtn;
    private LinearLayout actionButtonsLayout, cancelReasonLayout, returnReasonLayout, reviewLayout;
    private OrderManager orderManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            orderManager = OrderManager.getInstance(getContext());

            // Get order ID from arguments
            int orderId = 0;
            if (getArguments() != null) {
                orderId = getArguments().getInt("orderId", 0);
            }

            // Load order
            order = orderManager.getOrderById(orderId);

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

            // Action buttons
            actionButtonsLayout = view.findViewById(R.id.action_buttons_layout);
            cancelOrderBtn = view.findViewById(R.id.cancel_order_btn);
            confirmReceiptBtn = view.findViewById(R.id.confirm_receipt_btn);
            returnOrderBtn = view.findViewById(R.id.return_order_btn);
            reviewOrderBtn = view.findViewById(R.id.review_order_btn);

            // Additional info sections
            cancelReasonLayout = view.findViewById(R.id.cancel_reason_layout);
            cancelReasonText = view.findViewById(R.id.cancel_reason_text);
            returnReasonLayout = view.findViewById(R.id.return_reason_layout);
            returnReasonText = view.findViewById(R.id.return_reason_text);
            reviewLayout = view.findViewById(R.id.review_layout);
            reviewSectionText = view.findViewById(R.id.review_text);

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
            orderStatusText.setTextColor(getStatusColor(order.status));

            // Setup items recycler
            itemsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
            itemsRecycler.setAdapter(new CartAdapter(order.items, () -> {}));

            // Setup action buttons based on order status
            setupActionButtons();

            // Display cancel reason if exists
            if (order.cancelReason != null && !order.cancelReason.isEmpty()) {
                cancelReasonLayout.setVisibility(View.VISIBLE);
                cancelReasonText.setText("Lý do hủy: " + order.cancelReason);
            } else {
                cancelReasonLayout.setVisibility(View.GONE);
            }

            // Display return reason if exists
            if (order.returnReason != null && !order.returnReason.isEmpty()) {
                returnReasonLayout.setVisibility(View.VISIBLE);
                returnReasonText.setText("Lý do hoàn trả: " + order.returnReason);
            } else {
                returnReasonLayout.setVisibility(View.GONE);
            }

            // Display review if exists
            if (order.review != null) {
                reviewLayout.setVisibility(View.VISIBLE);
                reviewSectionText.setText(
                    "Đánh giá: " + order.review.rating + "⭐\n" +
                    "Nhận xét: " + order.review.comment + "\n" +
                    "Ngày: " + order.review.reviewDate
                );
            } else {
                reviewLayout.setVisibility(View.GONE);
            }

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
            case "RETURNED": return "Đã hoàn";
            default: return status;
        }
    }

    private int getStatusColor(String status) {
        switch (status) {
            case "PENDING":
                return getResources().getColor(android.R.color.holo_orange_dark);
            case "CONFIRMED":
                return getResources().getColor(android.R.color.holo_blue_dark);
            case "SHIPPED":
                return getResources().getColor(android.R.color.holo_purple);
            case "DELIVERED":
                return getResources().getColor(android.R.color.holo_green_dark);
            case "CANCELLED":
                return getResources().getColor(android.R.color.holo_red_dark);
            case "RETURNED":
                return getResources().getColor(android.R.color.holo_orange_light);
            default:
                return getResources().getColor(android.R.color.darker_gray);
        }
    }

    private void setupActionButtons() {
        // Show/hide buttons based on order status and permissions
        if (orderManager.canCancelOrder(order)) {
            cancelOrderBtn.setVisibility(View.VISIBLE);
            cancelOrderBtn.setOnClickListener(v -> showCancelDialog());
        } else {
            cancelOrderBtn.setVisibility(View.GONE);
        }

        if (orderManager.canConfirmReceipt(order)) {
            confirmReceiptBtn.setVisibility(View.VISIBLE);
            confirmReceiptBtn.setOnClickListener(v -> confirmReceipt());
        } else {
            confirmReceiptBtn.setVisibility(View.GONE);
        }

        if (orderManager.canReturnOrder(order)) {
            returnOrderBtn.setVisibility(View.VISIBLE);
            returnOrderBtn.setOnClickListener(v -> showReturnDialog());
        } else {
            returnOrderBtn.setVisibility(View.GONE);
        }

        if (orderManager.canReview(order)) {
            reviewOrderBtn.setVisibility(View.VISIBLE);
            reviewOrderBtn.setOnClickListener(v -> showReviewDialog());
        } else {
            reviewOrderBtn.setVisibility(View.GONE);
        }

        // Hide action buttons layout if no buttons are visible
        if (cancelOrderBtn.getVisibility() == View.GONE &&
            confirmReceiptBtn.getVisibility() == View.GONE &&
            returnOrderBtn.getVisibility() == View.GONE &&
            reviewOrderBtn.getVisibility() == View.GONE) {
            actionButtonsLayout.setVisibility(View.GONE);
        } else {
            actionButtonsLayout.setVisibility(View.VISIBLE);
        }
    }

    private void showCancelDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_cancel_order, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        RadioGroup reasonsGroup = dialogView.findViewById(R.id.cancel_reasons_group);
        com.google.android.material.textfield.TextInputLayout customReasonLayout =
            dialogView.findViewById(R.id.custom_reason_layout);
        com.google.android.material.textfield.TextInputEditText customReasonInput =
            dialogView.findViewById(R.id.custom_reason_input);
        Button cancelBtn = dialogView.findViewById(R.id.cancel_btn);
        Button confirmBtn = dialogView.findViewById(R.id.confirm_btn);

        // Default selection
        reasonsGroup.check(R.id.reason_change_address);

        // Show/hide custom reason input when "Lý do khác" is selected
        reasonsGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.reason_other) {
                customReasonLayout.setVisibility(View.VISIBLE);
            } else {
                customReasonLayout.setVisibility(View.GONE);
            }
        });

        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        confirmBtn.setOnClickListener(v -> {
            int selectedId = reasonsGroup.getCheckedRadioButtonId();
            String reason = "";

            if (selectedId == R.id.reason_change_address) {
                reason = "Tôi muốn thay đổi địa chỉ giao hàng";
            } else if (selectedId == R.id.reason_change_product) {
                reason = "Tôi muốn thay đổi sản phẩm trong đơn hàng";
            } else if (selectedId == R.id.reason_better_price) {
                reason = "Tôi tìm thấy giá tốt hơn ở nơi khác";
            } else if (selectedId == R.id.reason_no_need) {
                reason = "Tôi không còn nhu cầu mua nữa";
            } else if (selectedId == R.id.reason_long_delivery) {
                reason = "Thời gian giao hàng quá lâu";
            } else if (selectedId == R.id.reason_other) {
                reason = customReasonInput.getText().toString().trim();
                if (reason.isEmpty()) {
                    Toast.makeText(getContext(), "Vui lòng nhập lý do của bạn", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            cancelOrder(reason);
            dialog.dismiss();
        });

        dialog.show();
    }


    private void cancelOrder(String reason) {
        order.status = "CANCELLED";
        order.cancelReason = reason;
        orderManager.updateOrder(order);

        Toast.makeText(getContext(), "Đã hủy đơn hàng", Toast.LENGTH_SHORT).show();

        // Refresh UI
        orderStatusText.setText(getStatusText(order.status));
        orderStatusText.setTextColor(getStatusColor(order.status));
        cancelReasonLayout.setVisibility(View.VISIBLE);
        cancelReasonText.setText("Lý do hủy: " + reason);
        setupActionButtons();
    }

    private void confirmReceipt() {
        new AlertDialog.Builder(getContext())
            .setTitle("Xác nhận đã nhận hàng")
            .setMessage("Bạn đã nhận được hàng và hài lòng với sản phẩm?")
            .setPositiveButton("Đã nhận", (dialog, which) -> {
                order.isConfirmedReceived = true;
                order.confirmedReceivedDate = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
                orderManager.updateOrder(order);

                Toast.makeText(getContext(), "Đã xác nhận nhận hàng", Toast.LENGTH_SHORT).show();
                setupActionButtons();
            })
            .setNegativeButton("Hủy", null)
            .show();
    }

    private void showReturnDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_return_order, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        RadioGroup reasonsGroup = dialogView.findViewById(R.id.return_reasons_group);
        com.google.android.material.textfield.TextInputLayout customReasonLayout =
            dialogView.findViewById(R.id.custom_return_reason_layout);
        com.google.android.material.textfield.TextInputEditText customReasonInput =
            dialogView.findViewById(R.id.custom_return_reason_input);
        Button addMediaBtn = dialogView.findViewById(R.id.add_media_btn);
        Button cancelBtn = dialogView.findViewById(R.id.cancel_return_btn);
        Button confirmBtn = dialogView.findViewById(R.id.confirm_return_btn);

        // Default selection
        reasonsGroup.check(R.id.reason_defective);

        // Show/hide custom reason input when "Lý do khác" is selected
        reasonsGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.reason_return_other) {
                customReasonLayout.setVisibility(View.VISIBLE);
            } else {
                customReasonLayout.setVisibility(View.GONE);
            }
        });

        addMediaBtn.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Chức năng tải ảnh/video sẽ được thêm vào sau", Toast.LENGTH_SHORT).show();
        });

        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        confirmBtn.setOnClickListener(v -> {
            int selectedId = reasonsGroup.getCheckedRadioButtonId();
            String reason = "";

            if (selectedId == R.id.reason_defective) {
                reason = "Sản phẩm bị lỗi/hư hỏng";
            } else if (selectedId == R.id.reason_wrong_description) {
                reason = "Sản phẩm không đúng mô tả";
            } else if (selectedId == R.id.reason_missing_parts) {
                reason = "Sản phẩm bị thiếu phụ kiện";
            } else if (selectedId == R.id.reason_wrong_item) {
                reason = "Sản phẩm không đúng với đơn đặt hàng";
            } else if (selectedId == R.id.reason_poor_quality) {
                reason = "Chất lượng sản phẩm không tốt";
            } else if (selectedId == R.id.reason_return_other) {
                reason = customReasonInput.getText().toString().trim();
                if (reason.isEmpty()) {
                    Toast.makeText(getContext(), "Vui lòng nhập lý do của bạn", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            returnOrder(reason);
            dialog.dismiss();
        });

        dialog.show();
    }


    private void returnOrder(String reason) {
        order.status = "RETURNED";
        order.returnReason = reason;
        orderManager.updateOrder(order);

        Toast.makeText(getContext(), "Đã gửi yêu cầu hoàn trả", Toast.LENGTH_SHORT).show();

        // Refresh UI
        orderStatusText.setText(getStatusText(order.status));
        orderStatusText.setTextColor(getStatusColor(order.status));
        returnReasonLayout.setVisibility(View.VISIBLE);
        returnReasonText.setText("Lý do hoàn trả: " + reason);
        setupActionButtons();
    }

    private void showReviewDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_review, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        RatingBar ratingBar = dialogView.findViewById(R.id.rating_bar);
        com.google.android.material.textfield.TextInputEditText commentInput =
            dialogView.findViewById(R.id.comment_input);
        Button addMediaBtn = dialogView.findViewById(R.id.add_media_btn);
        Button cancelBtn = dialogView.findViewById(R.id.cancel_review_btn);
        Button submitBtn = dialogView.findViewById(R.id.submit_review_btn);

        ratingBar.setRating(5);

        addMediaBtn.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Chức năng tải ảnh/video sẽ được thêm vào sau", Toast.LENGTH_SHORT).show();
        });

        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        submitBtn.setOnClickListener(v -> {
            int rating = (int) ratingBar.getRating();
            String comment = commentInput.getText().toString().trim();

            if (rating < 1) {
                Toast.makeText(getContext(), "Vui lòng chọn số sao", Toast.LENGTH_SHORT).show();
                return;
            }

            if (comment.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập nhận xét", Toast.LENGTH_SHORT).show();
                return;
            }

            submitReview(rating, comment);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void submitReview(int rating, String comment) {
        OrderReview review = new OrderReview(rating, comment, order.customerName);
        review.reviewDate = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());

        order.review = review;
        orderManager.updateOrder(order);

        Toast.makeText(getContext(), "Đã gửi đánh giá. Cảm ơn bạn!", Toast.LENGTH_SHORT).show();

        // Refresh UI
        reviewLayout.setVisibility(View.VISIBLE);
        reviewSectionText.setText(
            "Đánh giá: " + review.rating + "⭐\n" +
            "Nhận xét: " + review.comment + "\n" +
            "Ngày: " + review.reviewDate
        );
        setupActionButtons();
    }
}

