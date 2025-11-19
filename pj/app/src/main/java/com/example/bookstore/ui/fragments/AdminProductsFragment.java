package com.example.bookstore.ui.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore.R;
import com.example.bookstore.adapters.AdminProductAdapter;
import com.example.bookstore.models.Book;
import com.example.bookstore.utils.BookDataLoader;

import java.util.ArrayList;
import java.util.List;

public class AdminProductsFragment extends Fragment {
    private RecyclerView recyclerView;
    private AdminProductAdapter adapter;
    private EditText searchInput;
    private Button addProductBtn, backBtn;
    private List<Book> allProducts, filteredProducts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_products, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.admin_products_recycler);
        searchInput = view.findViewById(R.id.search_product_input);
        addProductBtn = view.findViewById(R.id.add_product_btn);
        backBtn = view.findViewById(R.id.back_btn);

        allProducts = BookDataLoader.getAllBooks();
        filteredProducts = new ArrayList<>(allProducts);

        adapter = new AdminProductAdapter(filteredProducts, new AdminProductAdapter.OnProductActionListener() {
            @Override
            public void onEdit(Book book) {
                onEditProduct(book);
            }

            @Override
            public void onDelete(Book book) {
                onDeleteProduct(book);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        backBtn.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());

        addProductBtn.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean("isEdit", false);
            Navigation.findNavController(v).navigate(R.id.adminEditProductFragment, bundle);
        });

        searchInput.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts(s.toString());
            }

            public void afterTextChanged(Editable s) {}
        });
    }

    private void filterProducts(String query) {
        filteredProducts.clear();
        if (query.isEmpty()) {
            filteredProducts.addAll(allProducts);
        } else {
            String lowerQuery = query.toLowerCase();
            for (Book book : allProducts) {
                if (book.title.toLowerCase().contains(lowerQuery)
                        || book.author.toLowerCase().contains(lowerQuery)
                        || book.category.toLowerCase().contains(lowerQuery)) {
                    filteredProducts.add(book);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void onEditProduct(Book book) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("isEdit", true);
        bundle.putSerializable("book", book);
        Navigation.findNavController(requireView()).navigate(R.id.adminEditProductFragment, bundle);
    }

    private void onDeleteProduct(Book book) {
        allProducts.remove(book);
        filteredProducts.remove(book);
        adapter.notifyDataSetChanged();
    }
}
