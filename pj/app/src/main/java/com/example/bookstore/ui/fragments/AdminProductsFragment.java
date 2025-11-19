package com.example.bookstore.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.bookstore.R;

public class AdminProductsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_products, container, false);

        // FAB để thêm sản phẩm
        View addProductFab = view.findViewById(R.id.add_product_fab);
        if (addProductFab != null) {
            addProductFab.setOnClickListener(v ->
                Toast.makeText(getContext(), "🆕 Thêm sản phẩm mới - Chức năng đang phát triển", Toast.LENGTH_LONG).show()
            );
        }

        return view;
    }
}
