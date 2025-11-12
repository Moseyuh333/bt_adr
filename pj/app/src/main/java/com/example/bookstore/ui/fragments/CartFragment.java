package com.example.bookstore.ui.fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bookstore.R;
import com.example.bookstore.adapters.CartAdapter;
import com.example.bookstore.models.Cart;

public class CartFragment extends Fragment {
    private Cart cart;
    private TextView subtotalText, taxText, shippingText, totalText, emptyCartText;
    private LinearLayout cartSummary;
    private Button checkoutBtn, continueShopping;
    private RecyclerView cartRecycler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            cart = Cart.getInstance();

            // Initialize views
            cartRecycler = view.findViewById(R.id.cart_recycler);
            emptyCartText = view.findViewById(R.id.empty_cart_text);
            cartSummary = view.findViewById(R.id.cart_summary);
            subtotalText = view.findViewById(R.id.cart_subtotal);
            taxText = view.findViewById(R.id.cart_tax);
            shippingText = view.findViewById(R.id.cart_shipping);
            totalText = view.findViewById(R.id.cart_total);
            checkoutBtn = view.findViewById(R.id.checkout_btn);
            continueShopping = view.findViewById(R.id.continue_shopping_btn);

            if (cartRecycler != null) {
                cartRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
                cartRecycler.setAdapter(new CartAdapter(cart.getItems(), this::updateCartSummary));
            }

            // Update summary
            updateCartSummary();

            // Checkout button
            if (checkoutBtn != null) {
                checkoutBtn.setOnClickListener(v -> {
                    if (cart.isEmpty()) {
                        Toast.makeText(getContext(), "Cart is empty", Toast.LENGTH_SHORT).show();
                    } else {
                        Navigation.findNavController(v).navigate(R.id.checkoutFragment);
                    }
                });
            }

            // Continue shopping button
            if (continueShopping != null) {
                continueShopping.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.catalogFragment));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateCartSummary() {
        try {
            if (cart.isEmpty()) {
                emptyCartText.setVisibility(View.VISIBLE);
                cartSummary.setVisibility(View.GONE);
                cartRecycler.setVisibility(View.GONE);
            } else {
                emptyCartText.setVisibility(View.GONE);
                cartSummary.setVisibility(View.VISIBLE);
                cartRecycler.setVisibility(View.VISIBLE);

                subtotalText.setText(String.format("%,.0f₫", cart.getSubtotal()));
                taxText.setText(String.format("%,.0f₫", cart.getTax()));
                shippingText.setText(String.format("%,.0f₫", cart.getShippingFee()));
                totalText.setText(String.format("%,.0f₫", cart.getTotal()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh cart when coming back to this screen
        if (cartRecycler != null && cartRecycler.getAdapter() != null) {
            cartRecycler.getAdapter().notifyDataSetChanged();
        }
        updateCartSummary();
    }
}

