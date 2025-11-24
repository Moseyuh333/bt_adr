package com.example.bookstore.ui.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bookstore.R;
import com.example.bookstore.adapters.AdminProductAdapter;
import com.example.bookstore.models.Book;
import com.example.bookstore.utils.DataManager;
import java.util.ArrayList;
import java.util.List;

public class AdminProductsFragment extends Fragment {

    private DataManager dataManager;
    private AdminProductAdapter adapter;
    private List<Book> allBooks;
    private EditText searchInput;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_products, container, false);

        dataManager = DataManager.getInstance(requireContext());
        allBooks = dataManager.getAllBooks();

        // Setup RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.admin_products_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new AdminProductAdapter(new AdminProductAdapter.OnProductActionListener() {
            @Override
            public void onEdit(Book book) {
                showEditProductDialog(book);
            }

            @Override
            public void onDelete(Book book) {
                showDeleteConfirmDialog(book);
            }
        });

        recyclerView.setAdapter(adapter);
        adapter.setBooks(allBooks);

        // Search functionality
        searchInput = view.findViewById(R.id.search_product_input);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // FAB để thêm sản phẩm
        View addProductFab = view.findViewById(R.id.add_product_fab);
        if (addProductFab != null) {
            addProductFab.setOnClickListener(v -> showAddProductDialog());
        }

        return view;
    }

    private void filterProducts(String query) {
        if (query.isEmpty()) {
            adapter.setBooks(allBooks);
        } else {
            List<Book> filtered = new ArrayList<>();
            for (Book book : allBooks) {
                if (book.title.toLowerCase().contains(query.toLowerCase()) ||
                    book.author.toLowerCase().contains(query.toLowerCase()) ||
                    book.category.toLowerCase().contains(query.toLowerCase())) {
                    filtered.add(book);
                }
            }
            adapter.setBooks(filtered);
        }
    }

    private void showAddProductDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_product, null);
        EditText titleInput = dialogView.findViewById(R.id.input_product_title);
        EditText authorInput = dialogView.findViewById(R.id.input_product_author);
        EditText priceInput = dialogView.findViewById(R.id.input_product_price);
        EditText categoryInput = dialogView.findViewById(R.id.input_product_category);
        EditText stockInput = dialogView.findViewById(R.id.input_product_stock);
        EditText descInput = dialogView.findViewById(R.id.input_product_description);

        new AlertDialog.Builder(requireContext())
            .setTitle("➕ Thêm Sản Phẩm Mới")
            .setView(dialogView)
            .setPositiveButton("Thêm", (dialog, which) -> {
                String title = titleInput.getText().toString().trim();
                String author = authorInput.getText().toString().trim();
                String priceStr = priceInput.getText().toString().trim();
                String category = categoryInput.getText().toString().trim();
                String stockStr = stockInput.getText().toString().trim();
                String desc = descInput.getText().toString().trim();

                if (title.isEmpty() || author.isEmpty() || priceStr.isEmpty()) {
                    Toast.makeText(getContext(), "⚠️ Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    double price = Double.parseDouble(priceStr);
                    int stock = stockStr.isEmpty() ? 100 : Integer.parseInt(stockStr);

                    Book newBook = new Book("0", title, author, price,
                        "https://picsum.photos/seed/" + title.hashCode() + "/200/300",
                        desc.isEmpty() ? "Sách mới" : desc,
                        category.isEmpty() ? "General" : category,
                        4.5, stock);

                    dataManager.addBook(newBook);
                    allBooks = dataManager.getAllBooks();
                    adapter.setBooks(allBooks);

                    Toast.makeText(getContext(), "✅ Đã thêm sản phẩm: " + title, Toast.LENGTH_SHORT).show();
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "⚠️ Giá hoặc số lượng không hợp lệ!", Toast.LENGTH_SHORT).show();
                }
            })
            .setNegativeButton("Hủy", null)
            .show();
    }

    private void showEditProductDialog(Book book) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_product, null);
        EditText titleInput = dialogView.findViewById(R.id.input_product_title);
        EditText authorInput = dialogView.findViewById(R.id.input_product_author);
        EditText priceInput = dialogView.findViewById(R.id.input_product_price);
        EditText categoryInput = dialogView.findViewById(R.id.input_product_category);
        EditText stockInput = dialogView.findViewById(R.id.input_product_stock);
        EditText descInput = dialogView.findViewById(R.id.input_product_description);

        // Pre-fill current values
        titleInput.setText(book.title);
        authorInput.setText(book.author);
        priceInput.setText(String.valueOf((int)book.price));
        categoryInput.setText(book.category);
        stockInput.setText(String.valueOf(book.quantity));
        descInput.setText(book.description);

        new AlertDialog.Builder(requireContext())
            .setTitle("✏️ Chỉnh Sửa Sản Phẩm")
            .setView(dialogView)
            .setPositiveButton("Lưu", (dialog, which) -> {
                try {
                    book.title = titleInput.getText().toString().trim();
                    book.author = authorInput.getText().toString().trim();
                    book.price = Double.parseDouble(priceInput.getText().toString().trim());
                    book.category = categoryInput.getText().toString().trim();
                    book.quantity = Integer.parseInt(stockInput.getText().toString().trim());
                    book.description = descInput.getText().toString().trim();
                    book.inStock = book.quantity > 0;

                    dataManager.updateBook(book);
                    allBooks = dataManager.getAllBooks();
                    adapter.setBooks(allBooks);

                    Toast.makeText(getContext(), "✅ Đã cập nhật: " + book.title, Toast.LENGTH_SHORT).show();
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "⚠️ Dữ liệu không hợp lệ!", Toast.LENGTH_SHORT).show();
                }
            })
            .setNegativeButton("Hủy", null)
            .show();
    }

    private void showDeleteConfirmDialog(Book book) {
        new AlertDialog.Builder(requireContext())
            .setTitle("🗑️ Xóa Sản Phẩm")
            .setMessage("Bạn có chắc muốn xóa \"" + book.title + "\"?")
            .setPositiveButton("Xóa", (dialog, which) -> {
                dataManager.deleteBook(book.id);
                allBooks = dataManager.getAllBooks();
                adapter.setBooks(allBooks);
                Toast.makeText(getContext(), "✅ Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
            })
            .setNegativeButton("Hủy", null)
            .show();
    }
}
