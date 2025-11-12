package com.example.bookstore.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore.R;
import com.example.bookstore.adapters.CartAdapter;
import com.example.bookstore.models.Cart;
import com.example.bookstore.models.Order;
import androidx.navigation.Navigation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CheckoutFragment extends Fragment {

    private Cart cart;
    private EditText voucherCodeInput, nameInput, emailInput, phoneInput, addressInput;
    private TextView subtotalText, taxText, discountText, shippingText, totalText, voucherAppliedText;
    private RadioGroup paymentMethodGroup;
    private RecyclerView itemsRecycler;
    private Button applyVoucherBtn, confirmOrderBtn;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_checkout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            cart = Cart.getInstance();
            sharedPreferences = requireActivity().getSharedPreferences("BookstorePrefs", Context.MODE_PRIVATE);

            if (cart.isEmpty()) {
                Toast.makeText(getContext(), "Cart is empty", Toast.LENGTH_SHORT).show();
                requireActivity().onBackPressed();
                return;
            }

            // Initialize views
            itemsRecycler = view.findViewById(R.id.checkout_items_recycler);
            subtotalText = view.findViewById(R.id.checkout_subtotal);
            taxText = view.findViewById(R.id.checkout_tax);
            discountText = view.findViewById(R.id.checkout_discount);
            shippingText = view.findViewById(R.id.checkout_shipping);
            totalText = view.findViewById(R.id.checkout_total);
            voucherCodeInput = view.findViewById(R.id.voucher_code_input);
            voucherAppliedText = view.findViewById(R.id.voucher_applied_text);
            applyVoucherBtn = view.findViewById(R.id.apply_voucher_btn);
            nameInput = view.findViewById(R.id.checkout_name);
            emailInput = view.findViewById(R.id.checkout_email);
            phoneInput = view.findViewById(R.id.checkout_phone);
            addressInput = view.findViewById(R.id.checkout_address);
            paymentMethodGroup = view.findViewById(R.id.payment_method_group);
            confirmOrderBtn = view.findViewById(R.id.confirm_order_btn);

            // Set up cart items recycler
            itemsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
            itemsRecycler.setAdapter(new CartAdapter(cart.getItems(), this::updateCheckoutSummary));

            // Load user information from SharedPreferences
            nameInput.setText(sharedPreferences.getString("user_name", ""));
            emailInput.setText(sharedPreferences.getString("user_email", ""));
            phoneInput.setText(sharedPreferences.getString("user_phone", ""));
            addressInput.setText(sharedPreferences.getString("user_address", ""));

            // Update summary
            updateCheckoutSummary();

            // Apply voucher button
            applyVoucherBtn.setOnClickListener(v -> applyVoucher());

            // Confirm order button
            confirmOrderBtn.setOnClickListener(v -> confirmOrder(view));

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error loading checkout", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateCheckoutSummary() {
        try {
            subtotalText.setText(String.format("%,.0f₫", cart.getSubtotal()));
            taxText.setText(String.format("%,.0f₫", cart.getTax()));
            discountText.setText(String.format("-%,.0f₫", cart.getDiscount()));
            shippingText.setText(String.format("%,.0f₫", cart.getShippingFee()));
            totalText.setText(String.format("%,.0f₫", cart.getTotal()));

            if (cart.getAppliedVoucher() != null) {
                voucherAppliedText.setVisibility(View.VISIBLE);
                voucherAppliedText.setText("✓ Voucher applied: " + cart.getAppliedVoucher().code);
            } else {
                voucherAppliedText.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void applyVoucher() {
        try {
            String code = voucherCodeInput.getText().toString().trim().toUpperCase();
            if (code.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập mã voucher", Toast.LENGTH_SHORT).show();
                return;
            }

            // Use VoucherManager to find voucher
            com.example.bookstore.utils.VoucherManager voucherManager =
                com.example.bookstore.utils.VoucherManager.getInstance();

            com.example.bookstore.models.Voucher voucher = voucherManager.findVoucher(code);

            if (voucher != null && voucher.isValid()) {
                // Check minimum order requirement
                if (cart.getSubtotal() < voucher.minOrderAmount) {
                    Toast.makeText(getContext(),
                        String.format("Đơn hàng tối thiểu %,.0f₫ để sử dụng voucher này", voucher.minOrderAmount),
                        Toast.LENGTH_LONG).show();
                    return;
                }

                cart.applyVoucher(voucher);
                Toast.makeText(getContext(), "Áp dụng voucher thành công!", Toast.LENGTH_SHORT).show();
                voucherCodeInput.setText("");
                updateCheckoutSummary();
            } else {
                Toast.makeText(getContext(), "Mã voucher không hợp lệ", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Lỗi khi áp dụng voucher", Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmOrder(View view) {
        try {
            // Validate inputs
            String name = nameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String phone = phoneInput.getText().toString().trim();
            String address = addressInput.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get selected payment method
            int selectedPaymentId = paymentMethodGroup.getCheckedRadioButtonId();
            if (selectedPaymentId == -1) {
                Toast.makeText(getContext(), "Please select payment method", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton selectedPayment = view.findViewById(selectedPaymentId);
            String paymentMethod = selectedPayment.getText().toString();

            // Create order
            Order order = new Order();
            order.items = cart.getItems();
            order.customerName = name;
            order.customerEmail = email;
            order.customerPhone = phone;
            order.deliveryAddress = address;
            order.paymentMethod = paymentMethod;
            order.voucherCode = cart.getAppliedVoucher() != null ? cart.getAppliedVoucher().code : "";
            order.status = "CONFIRMED";
            order.subtotal = cart.getSubtotal();
            order.tax = cart.getTax();
            order.discount = cart.getDiscount();
            order.shippingFee = cart.getShippingFee();
            order.total = cart.getTotal();
            order.orderDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

            // Save order (in real app, send to server)
            saveOrder(order);

            // Show success message
            Toast.makeText(getContext(), "Order confirmed! Order ID: #" + System.currentTimeMillis(), Toast.LENGTH_LONG).show();

            // Clear cart
            cart.clear();

            // Navigate back to home
            Navigation.findNavController(view).navigate(R.id.homeFragment);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error confirming order", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveOrder(Order order) {
        try {
            // Save order details to SharedPreferences (for demo)
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("last_order_date", order.orderDate);
            editor.putString("last_order_total", String.valueOf(order.total));
            editor.putString("last_order_status", order.status);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

