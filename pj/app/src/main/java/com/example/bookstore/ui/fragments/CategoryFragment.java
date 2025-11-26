package com.example.bookstore.ui.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bookstore.R;
import com.example.bookstore.adapters.BookAdapter;
import com.example.bookstore.adapters.CategoryAdapter;
import com.example.bookstore.database.AppDatabase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CategoryFragment extends Fragment {

    private RecyclerView categoryRecycler, booksRecycler;
    private TextView categoryTitle;
    private Button sortButton;
    private BookAdapter bookAdapter;
    private CategoryAdapter categoryAdapter;
    private List<com.example.bookstore.database.entities.Book> currentBooks;
    private String selectedCategory = "All";
    private String currentSortMethod = "default";
    private AppDatabase database;
    private ExecutorService executorService;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        database = AppDatabase.getInstance(requireContext());
        executorService = Executors.newSingleThreadExecutor();

        categoryRecycler = view.findViewById(R.id.category_recycler);
        booksRecycler = view.findViewById(R.id.category_books_recycler);
        categoryTitle = view.findViewById(R.id.category_title_text);
        sortButton = view.findViewById(R.id.sort_button);

        currentBooks = new ArrayList<>();

        if (sortButton != null) {
            sortButton.setOnClickListener(v -> showSortDialog());
        }

        executorService.execute(() -> {
            List<String> dbCategories = database.bookDao().getAllCategories();
            List<String> categories = new ArrayList<>();
            categories.add("All");
            categories.addAll(dbCategories);

            requireActivity().runOnUiThread(() -> {
                categoryRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                categoryAdapter = new CategoryAdapter(categories, this::onCategorySelected);
                categoryRecycler.setAdapter(categoryAdapter);
            });
        });

        booksRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        bookAdapter = new BookAdapter(new ArrayList<>());
        booksRecycler.setAdapter(bookAdapter);

        String initialCategory = "All";
        if (getArguments() != null && getArguments().containsKey("category")) {
            initialCategory = getArguments().getString("category", "All");
            selectedCategory = initialCategory;
        }

        loadBooks(initialCategory);
    }

    private void onCategorySelected(String category) {
        selectedCategory = category;
        loadBooks(category);
    }

    private void loadBooks(String category) {
        executorService.execute(() -> {
            List<com.example.bookstore.database.entities.Book> books;

            if (category.equals("All")) {
                books = database.bookDao().getAllActiveBooks();
            } else {
                books = database.bookDao().getBooksByCategory(category);
                // Nếu category không trả ra sách, fallback sang tất cả sách
                if (books == null || books.isEmpty()) {
                    books = database.bookDao().getAllActiveBooks();
                }
            }

            final List<com.example.bookstore.database.entities.Book> finalBooks = books;
            requireActivity().runOnUiThread(() -> {
                currentBooks.clear();
                if (finalBooks != null) currentBooks.addAll(finalBooks);
                applySorting();

                if (category.equals("All") || finalBooks == null) {
                    categoryTitle.setText("Tất Cả Sách (" + currentBooks.size() + ")");
                } else {
                    categoryTitle.setText(category + " (" + currentBooks.size() + " cuốn)");
                }

                List<com.example.bookstore.models.Book> oldBooks = convertToOldBookList(currentBooks);
                bookAdapter = new BookAdapter(oldBooks);
                booksRecycler.setAdapter(bookAdapter);
            });
        });
    }

    private void showSortDialog() {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_sort, null);
        AlertDialog dialog = new AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create();

        android.widget.RadioGroup radioGroup = dialogView.findViewById(R.id.sort_radio_group);
        android.widget.Button cancelButton = dialogView.findViewById(R.id.cancel_button);
        android.widget.Button applyButton = dialogView.findViewById(R.id.apply_button);

        // Set current selection
        switch (currentSortMethod) {
            case "default": radioGroup.check(R.id.sort_default); break;
            case "price_asc": radioGroup.check(R.id.sort_price_asc); break;
            case "price_desc": radioGroup.check(R.id.sort_price_desc); break;
            case "name_asc": radioGroup.check(R.id.sort_name_asc); break;
            case "name_desc": radioGroup.check(R.id.sort_name_desc); break;
            case "newest": radioGroup.check(R.id.sort_newest); break;
        }

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        applyButton.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            if (selectedId == R.id.sort_default) currentSortMethod = "default";
            else if (selectedId == R.id.sort_price_asc) currentSortMethod = "price_asc";
            else if (selectedId == R.id.sort_price_desc) currentSortMethod = "price_desc";
            else if (selectedId == R.id.sort_name_asc) currentSortMethod = "name_asc";
            else if (selectedId == R.id.sort_name_desc) currentSortMethod = "name_desc";
            else if (selectedId == R.id.sort_newest) currentSortMethod = "newest";

            applySorting();
            List<com.example.bookstore.models.Book> oldBooks = convertToOldBookList(currentBooks);
            bookAdapter = new BookAdapter(oldBooks);
            booksRecycler.setAdapter(bookAdapter);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void applySorting() {
        switch (currentSortMethod) {
            case "price_asc":
                Collections.sort(currentBooks, Comparator.comparingDouble(com.example.bookstore.database.entities.Book::getPrice));
                break;
            case "price_desc":
                Collections.sort(currentBooks, (a, b) -> Double.compare(b.getPrice(), a.getPrice()));
                break;
            case "name_asc":
                Collections.sort(currentBooks, Comparator.comparing(com.example.bookstore.database.entities.Book::getTitle));
                break;
            case "name_desc":
                Collections.sort(currentBooks, (a, b) -> b.getTitle().compareTo(a.getTitle()));
                break;
            case "newest":
                Collections.sort(currentBooks, (a, b) -> Integer.compare(b.getId(), a.getId()));
                break;
        }
    }

    private List<com.example.bookstore.models.Book> convertToOldBookList(List<com.example.bookstore.database.entities.Book> dbBooks) {
        List<com.example.bookstore.models.Book> oldBooks = new ArrayList<>();
        for (com.example.bookstore.database.entities.Book db : dbBooks) {
            com.example.bookstore.models.Book book = new com.example.bookstore.models.Book(
                String.valueOf(db.getId()),
                db.getTitle(),
                db.getAuthor(),
                db.getPrice(),
                db.getImageUrl(),
                db.getDescription(),
                db.getCategory(),
                0.0,
                db.getStock()
            );
            oldBooks.add(book);
        }
        return oldBooks;
    }
}
