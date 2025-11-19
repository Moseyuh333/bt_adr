package com.example.bookstore.ui.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.bumptech.glide.Glide;
import com.example.bookstore.R;
import com.example.bookstore.models.Book;

public class AdminEditProductFragment extends Fragment {
    private boolean isEdit;
    private Book book;
    private EditText titleInput, authorInput, categoryInput, priceInput, stockInput, descriptionInput, imageUrlInput;
    private ImageView imagePreview;
    private Button saveBtn, backBtn;
    private TextView headerTitle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_edit_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            isEdit = getArguments().getBoolean("isEdit", false);
            if (isEdit) book = (Book) getArguments().getSerializable("book");
        }
        headerTitle = view.findViewById(R.id.header_title);
        titleInput = view.findViewById(R.id.title_input);
        authorInput = view.findViewById(R.id.author_input);
        categoryInput = view.findViewById(R.id.category_input);
        priceInput = view.findViewById(R.id.price_input);
        stockInput = view.findViewById(R.id.stock_input);
        descriptionInput = view.findViewById(R.id.description_input);
        imageUrlInput = view.findViewById(R.id.image_url_input);
        imagePreview = view.findViewById(R.id.product_image_preview);
        saveBtn = view.findViewById(R.id.save_product_btn);
        backBtn = view.findViewById(R.id.back_btn);
        headerTitle.setText(isEdit ? "Sửa Sản Phẩm" : "Thêm Sản Phẩm");
        if (isEdit && book != null) {
            titleInput.setText(book.title);
            authorInput.setText(book.author);
            categoryInput.setText(book.category);
            priceInput.setText(String.valueOf((int)book.price));
            stockInput.setText(String.valueOf(book.quantity));
            descriptionInput.setText(book.description);
            imageUrlInput.setText(book.coverImage);
            Glide.with(this).load(book.coverImage).placeholder(R.drawable.book_placeholder).into(imagePreview);
        }
        imageUrlInput.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) Glide.with(AdminEditProductFragment.this).load(s.toString()).placeholder(R.drawable.book_placeholder).error(R.drawable.book_placeholder).into(imagePreview);
            }
            public void afterTextChanged(Editable s) {}
        });
        backBtn.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());
        saveBtn.setOnClickListener(v -> handleSave());
    }

    private void handleSave() {
        String title = titleInput.getText().toString().trim();
        String author = authorInput.getText().toString().trim();
        String category = categoryInput.getText().toString().trim();
        String priceStr = priceInput.getText().toString().trim();
        String stockStr = stockInput.getText().toString().trim();
        String description = descriptionInput.getText().toString().trim();
        String imageUrl = imageUrlInput.getText().toString().trim();
        if (title.isEmpty() || author.isEmpty() || category.isEmpty() || priceStr.isEmpty() || stockStr.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng điền đầy đủ thông tin bắt buộc", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            double price = Double.parseDouble(priceStr);
            int stock = Integer.parseInt(stockStr);
            if (isEdit && book != null) {
                book.title = title;
                book.author = author;
                book.category = category;
                book.price = price;
                book.quantity = stock;
                book.description = description;
                book.coverImage = imageUrl;
                Toast.makeText(getContext(), "Đã cập nhật sản phẩm", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Đã thêm sản phẩm mới", Toast.LENGTH_SHORT).show();
            }
            Navigation.findNavController(requireView()).popBackStack();
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Giá và số lượng phải là số hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }
}
