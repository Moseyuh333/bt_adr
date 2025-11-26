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
import com.example.bookstore.adapters.OrderItemAdapter;
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
            // Strict validation
            if (getContext() == null) {
                android.util.Log.e("OrderDetail", "Context is null");
                return;
            }

            if (view == null) {
                android.util.Log.e("OrderDetail", "View is null");
                return;
            }

            android.util.Log.d("OrderDetail", "Starting onViewCreated");

            // Initialize OrderManager safely
            try {
                orderManager = OrderManager.getInstance(getContext());
            } catch (Exception e) {
                android.util.Log.e("OrderDetail", "Failed to get OrderManager", e);
                Toast.makeText(getContext(), "Lỗi khởi tạo OrderManager", Toast.LENGTH_SHORT).show();
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
                return;
            }

            // Get order ID from arguments
            int orderId = 0;
            if (getArguments() != null) {
                orderId = getArguments().getInt("orderId", 0);
                android.util.Log.d("OrderDetail", "Order ID: " + orderId);
            } else {
                android.util.Log.e("OrderDetail", "No arguments provided");
            }

            // Try to load order from OrderManager first
            if (orderManager != null) {
                order = orderManager.getOrderById(orderId);
            }

            if (order == null) {
                // If not found, create a safe placeholder to prevent crash
                order = new Order();
                order.id = "ORD" + orderId;
                order.orderDate = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
                order.status = "PENDING";
                order.customerName = "Khách hàng";
                order.customerPhone = "0900000000";
                order.customerEmail = "customer@example.com";
                order.deliveryAddress = "Địa chỉ đang cập nhật";
                order.shippingAddress = "Địa chỉ đang cập nhật";
                order.paymentMethod = "COD";
                order.subtotal = 0;
                order.tax = 0;
                order.discount = 0;
                order.shippingFee = 25000;
                order.total = 25000;
                order.totalAmount = 25000;
                order.items = new ArrayList<>();
                order.voucherCode = "";
                order.cancelReason = "";
                order.returnReason = "";
                order.review = null;

                if (getContext() != null) {
                    Toast.makeText(getContext(), "Không tìm thấy thông tin đơn hàng #" + orderId, Toast.LENGTH_SHORT).show();
                }
            }

            // Ensure ALL nullable fields are initialized to prevent any NPE
            if (order.items == null) {
                order.items = new ArrayList<>();
            }
            if (order.customerName == null) order.customerName = "Khách hàng";
            if (order.customerPhone == null) order.customerPhone = "N/A";
            if (order.customerEmail == null) order.customerEmail = "N/A";
            if (order.deliveryAddress == null) order.deliveryAddress = "N/A";
            if (order.shippingAddress == null) order.shippingAddress = order.deliveryAddress;
            if (order.paymentMethod == null) order.paymentMethod = "COD";
            if (order.status == null) order.status = "PENDING";
            if (order.orderDate == null) order.orderDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
            if (order.voucherCode == null) order.voucherCode = "";
            if (order.cancelReason == null) order.cancelReason = "";
            if (order.returnReason == null) order.returnReason = "";

            // Initialize views with null checks
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

            // Check if critical views are null
            if (orderIdText == null || orderStatusText == null || itemsRecycler == null) {
                Toast.makeText(getContext(), "Lỗi tải giao diện đơn hàng", Toast.LENGTH_SHORT).show();
                return;
            }

            // Set order data with null checks
            if (orderIdText != null) orderIdText.setText("Đơn hàng #" + (order.id != null ? order.id : "N/A"));
            if (orderDateText != null) orderDateText.setText(order.orderDate != null ? order.orderDate : "");
            if (orderStatusText != null) {
                orderStatusText.setText(getStatusText(order.status));
                orderStatusText.setTextColor(getStatusColor(order.status));
            }
            if (customerNameText != null) customerNameText.setText(order.customerName != null ? order.customerName : "");
            if (customerPhoneText != null) customerPhoneText.setText(order.customerPhone != null ? order.customerPhone : "");
            if (customerAddressText != null) customerAddressText.setText(order.deliveryAddress != null ? order.deliveryAddress : "");
            if (subtotalText != null) subtotalText.setText(String.format("%,.0f₫", order.subtotal));
            if (taxText != null) taxText.setText(String.format("%,.0f₫", order.tax));
            if (discountText != null) discountText.setText(String.format("-%,.0f₫", order.discount));
            if (shippingText != null) shippingText.setText(String.format("%,.0f₫", order.shippingFee));
            if (totalText != null) totalText.setText(String.format("%,.0f₫", order.total));
            if (paymentMethodText != null) paymentMethodText.setText(order.paymentMethod != null ? order.paymentMethod : "COD");

            if (voucherCodeText != null) {
                if (order.voucherCode != null && !order.voucherCode.isEmpty()) {
                    voucherCodeText.setText("Mã: " + order.voucherCode);
                    voucherCodeText.setVisibility(View.VISIBLE);
                } else {
                    voucherCodeText.setVisibility(View.GONE);
                }
            }

            // Setup items recycler with null checks
            if (itemsRecycler != null && getContext() != null) {
                itemsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
                // Create empty list if items is null
                List<?> itemsToDisplay = (order.items != null && !order.items.isEmpty())
                    ? order.items
                    : new ArrayList<>();
                // Sử dụng OrderItemAdapter thay vì CartAdapter để hiển thị order items
                itemsRecycler.setAdapter(new OrderItemAdapter(itemsToDisplay));
            }

            // Setup action buttons based on order status
            try {
                setupActionButtons();
            } catch (Exception e) {
                e.printStackTrace();
                // Hide action buttons if error
                if (actionButtonsLayout != null) {
                    actionButtonsLayout.setVisibility(View.GONE);
                }
            }

            // Display cancel reason if exists
            if (cancelReasonLayout != null && cancelReasonText != null) {
                if (order.cancelReason != null && !order.cancelReason.isEmpty()) {
                    cancelReasonLayout.setVisibility(View.VISIBLE);
                    cancelReasonText.setText("Lý do hủy: " + order.cancelReason);
                } else {
                    cancelReasonLayout.setVisibility(View.GONE);
                }
            }

            // Display return reason if exists
            if (returnReasonLayout != null && returnReasonText != null) {
                if (order.returnReason != null && !order.returnReason.isEmpty()) {
                    returnReasonLayout.setVisibility(View.VISIBLE);
                    returnReasonText.setText("Lý do hoàn trả: " + order.returnReason);
                } else {
                    returnReasonLayout.setVisibility(View.GONE);
                }
            }

            // Display review if exists
            if (reviewLayout != null && reviewSectionText != null) {
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
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (getContext() != null) {
                Toast.makeText(getContext(), "Lỗi hiển thị chi tiết đơn hàng: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
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
        try {
            if (getResources() == null) return 0xFF000000; // Black as default

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
        } catch (Exception e) {
            e.printStackTrace();
            return 0xFF000000; // Black as fallback
        }
    }

    private void setupActionButtons() {
        try {
            if (orderManager == null || order == null) return;

            // Show/hide buttons based on order status and permissions
            if (cancelOrderBtn != null) {
                try {
                    if (orderManager.canCancelOrder(order)) {
                        cancelOrderBtn.setVisibility(View.VISIBLE);
                        cancelOrderBtn.setOnClickListener(v -> {
                            try {
                                showCancelDialog();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        cancelOrderBtn.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    cancelOrderBtn.setVisibility(View.GONE);
                }
            }

            if (confirmReceiptBtn != null) {
                try {
                    if (orderManager.canConfirmReceipt(order)) {
                        confirmReceiptBtn.setVisibility(View.VISIBLE);
                        confirmReceiptBtn.setOnClickListener(v -> {
                            try {
                                confirmReceipt();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        confirmReceiptBtn.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    confirmReceiptBtn.setVisibility(View.GONE);
                }
            }

            if (returnOrderBtn != null) {
                try {
                    if (orderManager.canReturnOrder(order)) {
                        returnOrderBtn.setVisibility(View.VISIBLE);
                        returnOrderBtn.setOnClickListener(v -> {
                            try {
                                showReturnDialog();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        returnOrderBtn.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    returnOrderBtn.setVisibility(View.GONE);
                }
            }

            if (reviewOrderBtn != null) {
                try {
                    if (orderManager.canReview(order)) {
                        reviewOrderBtn.setVisibility(View.VISIBLE);
                        reviewOrderBtn.setOnClickListener(v -> {
                            try {
                                showReviewDialog();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        reviewOrderBtn.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    reviewOrderBtn.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Hide action buttons layout if no buttons are visible
        try {
            if (actionButtonsLayout != null) {
                boolean hasVisibleButton = false;
                if (cancelOrderBtn != null && cancelOrderBtn.getVisibility() == View.VISIBLE) hasVisibleButton = true;
                if (confirmReceiptBtn != null && confirmReceiptBtn.getVisibility() == View.VISIBLE) hasVisibleButton = true;
                if (returnOrderBtn != null && returnOrderBtn.getVisibility() == View.VISIBLE) hasVisibleButton = true;
                if (reviewOrderBtn != null && reviewOrderBtn.getVisibility() == View.VISIBLE) hasVisibleButton = true;

                actionButtonsLayout.setVisibility(hasVisibleButton ? View.VISIBLE : View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
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

