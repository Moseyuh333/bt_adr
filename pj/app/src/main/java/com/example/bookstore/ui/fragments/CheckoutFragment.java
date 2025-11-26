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
import com.example.bookstore.ui.fragments.AddressFragment;
import androidx.navigation.Navigation;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CheckoutFragment extends Fragment {

    private Cart cart;
    private EditText voucherCodeInput, nameInput, emailInput, phoneInput, addressInput;
    private EditText cardNumberInput, cardNameInput, cardExpiryInput, cardCvvInput;
    private TextView subtotalText, taxText, discountText, shippingText, totalText, voucherAppliedText;
    private RadioGroup paymentMethodGroup;
    private RecyclerView itemsRecycler;
    private Button applyVoucherBtn, confirmOrderBtn, selectAddressBtn;
    private SharedPreferences sharedPreferences;
    private View cardInfoLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_checkout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            cart = Cart.getInstance();
            if (cart == null) {
                Toast.makeText(getContext(), "Error loading cart", Toast.LENGTH_SHORT).show();
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
                return;
            }

            if (getActivity() == null) {
                return;
            }

            sharedPreferences = requireActivity().getSharedPreferences("BookstorePrefs", Context.MODE_PRIVATE);

            if (cart.isEmpty()) {
                Toast.makeText(getContext(), "Giỏ hàng trống", Toast.LENGTH_SHORT).show();
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
            selectAddressBtn = view.findViewById(R.id.select_saved_address_btn);
            confirmOrderBtn = view.findViewById(R.id.confirm_order_btn);

            // Card info fields
            cardInfoLayout = view.findViewById(R.id.card_info_layout);
            cardNumberInput = view.findViewById(R.id.card_number_input);
            cardNameInput = view.findViewById(R.id.card_name_input);
            cardExpiryInput = view.findViewById(R.id.card_expiry_input);
            cardCvvInput = view.findViewById(R.id.card_cvv_input);

            // Set up cart items recycler
            if (itemsRecycler != null && getContext() != null) {
                itemsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
                if (cart.getItems() != null) {
                    itemsRecycler.setAdapter(new CartAdapter(cart.getItems(), this::updateCheckoutSummary));
                }
            }

            // Load user information from SharedPreferences
            nameInput.setText(sharedPreferences.getString("user_name", ""));
            emailInput.setText(sharedPreferences.getString("user_email", ""));
            phoneInput.setText(sharedPreferences.getString("user_phone", ""));

            // Load default address from saved addresses or fallback
            String defaultAddress = loadDefaultAddress();
            if (defaultAddress != null && !defaultAddress.isEmpty()) {
                addressInput.setText(defaultAddress);
            } else {
                addressInput.setText(sharedPreferences.getString("user_address", ""));
            }

            // Update summary
            updateCheckoutSummary();

            // Apply voucher button
            applyVoucherBtn.setOnClickListener(v -> applyVoucher());

            // Select saved address button
            selectAddressBtn.setOnClickListener(v -> showSavedAddresses());

            // Payment method listener to show/hide card info
            paymentMethodGroup.setOnCheckedChangeListener((group, checkedId) -> {
                if (checkedId == R.id.payment_online) {
                    cardInfoLayout.setVisibility(View.VISIBLE);
                } else {
                    cardInfoLayout.setVisibility(View.GONE);
                }
            });

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
            String name = nameInput != null ? nameInput.getText().toString().trim() : "";
            String email = emailInput != null ? emailInput.getText().toString().trim() : "";
            String phone = phoneInput != null ? phoneInput.getText().toString().trim() : "";
            String address = addressInput != null ? addressInput.getText().toString().trim() : "";

            if (name.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập tên", Toast.LENGTH_SHORT).show();
                return;
            }
            if (email.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
                return;
            }
            if (phone.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
                return;
            }
            if (address.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập địa chỉ giao hàng", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get selected payment method
            int selectedPaymentId = paymentMethodGroup.getCheckedRadioButtonId();
            if (selectedPaymentId == -1) {
                Toast.makeText(getContext(), "Vui lòng chọn phương thức thanh toán", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton selectedPayment = view.findViewById(selectedPaymentId);
            if (selectedPayment == null) {
                Toast.makeText(getContext(), "Lỗi chọn phương thức thanh toán", Toast.LENGTH_SHORT).show();
                return;
            }
            String paymentMethod = selectedPayment.getText().toString();

            // Validate card info if online payment selected
            if (selectedPaymentId == R.id.payment_online) {
                String cardNumber = cardNumberInput.getText().toString().trim();
                String cardName = cardNameInput.getText().toString().trim();
                String cardExpiry = cardExpiryInput.getText().toString().trim();
                String cardCvv = cardCvvInput.getText().toString().trim();

                if (cardNumber.isEmpty() || cardName.isEmpty() || cardExpiry.isEmpty() || cardCvv.isEmpty()) {
                    Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin thẻ", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Basic card number validation (16 digits)
                if (cardNumber.length() < 13 || cardNumber.length() > 19) {
                    Toast.makeText(getContext(), "Số thẻ không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }

                // CVV validation (3-4 digits)
                if (cardCvv.length() < 3 || cardCvv.length() > 4) {
                    Toast.makeText(getContext(), "CVV không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

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
            Toast.makeText(getContext(), "Đặt hàng thành công! Mã đơn: #" + order.id, Toast.LENGTH_LONG).show();

            // Clear cart
            cart.clear();

            // Navigate back to home
            Navigation.findNavController(view).popBackStack(R.id.homeFragment, false);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error confirming order", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveOrder(Order order) {
        try {
            // Save to database
            com.example.bookstore.database.AppDatabase database = com.example.bookstore.database.AppDatabase.getInstance(requireContext());
            java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newSingleThreadExecutor();

            executor.execute(() -> {
                try {
                    // Get current user ID from SharedPreferences
                    int userId = sharedPreferences.getInt("user_id", 1);

                    // Create database order
                    com.example.bookstore.database.entities.Order dbOrder = new com.example.bookstore.database.entities.Order();
                    dbOrder.setUserId(userId);
                    dbOrder.setStatus("PENDING");
                    dbOrder.setShippingAddress(order.deliveryAddress);
                    dbOrder.setRecipientName(order.customerName);
                    dbOrder.setRecipientPhone(order.customerPhone);
                    dbOrder.setPaymentMethod(order.paymentMethod);
                    dbOrder.setTotalAmount(order.total);
                    dbOrder.setCreatedAt(System.currentTimeMillis());
                    dbOrder.setUpdatedAt(System.currentTimeMillis());

                    // Insert order and get ID
                    long orderId = database.orderDao().insert(dbOrder);

                    // Insert order items
                    for (com.example.bookstore.models.CartItem item : order.items) {
                        com.example.bookstore.database.entities.OrderItem orderItem = new com.example.bookstore.database.entities.OrderItem();
                        orderItem.setOrderId((int)orderId);
                        orderItem.setBookId(item.book.id);
                        orderItem.setBookTitle(item.book.title);
                        orderItem.setBookAuthor(item.book.author);
                        orderItem.setBookImageUrl(item.book.coverImage);
                        orderItem.setPrice(item.book.price);
                        orderItem.setQuantity(item.quantity);
                        orderItem.setSubtotal(item.book.price * item.quantity);

                        database.orderItemDao().insert(orderItem);

                        // Update book stock
                        database.bookDao().decreaseStock(item.book.id, item.quantity);
                    }

                    // Clear cart in database
                    database.cartDao().clearCart(userId);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            // Also save using OrderManager for backward compatibility
            com.example.bookstore.utils.OrderManager.getInstance(getContext()).saveOrder(order);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String loadDefaultAddress() {
        try {
            String json = sharedPreferences.getString("user_addresses", "");
            if (json.isEmpty()) {
                return null;
            }

            Gson gson = new Gson();
            List<AddressFragment.Address> addresses = gson.fromJson(json,
                new TypeToken<List<AddressFragment.Address>>() {}.getType());

            if (addresses != null) {
                for (AddressFragment.Address addr : addresses) {
                    if (addr.isDefault) {
                        return addr.address;
                    }
                }
                // If no default found, return first address
                if (!addresses.isEmpty()) {
                    return addresses.get(0).address;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void showSavedAddresses() {
        try {
            String json = sharedPreferences.getString("user_addresses", "");
            if (json.isEmpty()) {
                Toast.makeText(getContext(), "Chưa có địa chỉ nào được lưu", Toast.LENGTH_SHORT).show();
                return;
            }

            Gson gson = new Gson();
            List<AddressFragment.Address> addresses = gson.fromJson(json,
                new TypeToken<List<AddressFragment.Address>>() {}.getType());

            if (addresses == null || addresses.isEmpty()) {
                Toast.makeText(getContext(), "Chưa có địa chỉ nào được lưu", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create array of address strings
            String[] addressArray = new String[addresses.size()];
            for (int i = 0; i < addresses.size(); i++) {
                AddressFragment.Address addr = addresses.get(i);
                addressArray[i] = addr.description + ": " + addr.address;
            }

            // Show custom address selection dialog
            View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_address_selection, null);
            androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create();

            androidx.recyclerview.widget.RecyclerView addressesRecycler = dialogView.findViewById(R.id.addresses_recycler);
            Button addNewAddressButton = dialogView.findViewById(R.id.add_new_address_button);
            Button closeButton = dialogView.findViewById(R.id.close_button);

            // Simple adapter for addresses
            addressesRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
            // For now, use simple list
            androidx.appcompat.app.AlertDialog.Builder simpleBuilder = new androidx.appcompat.app.AlertDialog.Builder(requireContext());
            simpleBuilder.setTitle("📍 Chọn Địa Chỉ Giao Hàng");
            simpleBuilder.setItems(addressArray, (d, which) -> {
                addressInput.setText(addresses.get(which).address);
                Toast.makeText(getContext(), "✅ Địa chỉ đã được chọn", Toast.LENGTH_SHORT).show();
            });
            simpleBuilder.setNegativeButton("Hủy", null);
            simpleBuilder.show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Lỗi khi tải địa chỉ", Toast.LENGTH_SHORT).show();
        }
    }
}

