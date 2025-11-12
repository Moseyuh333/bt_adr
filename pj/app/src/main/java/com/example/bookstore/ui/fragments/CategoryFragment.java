package com.example.bookstore.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.bookstore.models.Book;
import com.example.bookstore.utils.BookDataLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoryFragment extends Fragment {

    private RecyclerView categoryRecycler, booksRecycler;
    private TextView categoryTitle;
    private BookAdapter bookAdapter;
    private CategoryAdapter categoryAdapter;
    private List<Book> currentBooks;
    private String selectedCategory = "All";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            categoryRecycler = view.findViewById(R.id.category_recycler);
            booksRecycler = view.findViewById(R.id.category_books_recycler);
            categoryTitle = view.findViewById(R.id.category_title_text);

            currentBooks = new ArrayList<>();

            // Setup categories
            List<String> categories = Arrays.asList(
                "All", "Fiction", "Romance", "Fantasy", "Science Fiction",
                "Mystery", "Thriller", "History", "Biography", "Self-Help",
                "Business", "Psychology", "Philosophy", "Gothic", "Dystopian",
                "Historical", "Finance"
            );

            categoryRecycler.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
            categoryAdapter = new CategoryAdapter(categories, this::onCategorySelected);
            categoryRecycler.setAdapter(categoryAdapter);

            // Setup books
            booksRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
            bookAdapter = new BookAdapter(currentBooks);
            booksRecycler.setAdapter(bookAdapter);

            // Load all books initially
            loadBooks("All");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onCategorySelected(String category) {
        selectedCategory = category;
        loadBooks(category);
    }

    private void loadBooks(String category) {
        try {
            currentBooks.clear();

            if (category.equals("All")) {
                currentBooks.addAll(BookDataLoader.getAllBooks());
                categoryTitle.setText("Tất Cả Sách (" + currentBooks.size() + ")");
            } else {
                currentBooks.addAll(BookDataLoader.getBooksByCategory(category));
                categoryTitle.setText(category + " (" + currentBooks.size() + " cuốn)");
            }

            bookAdapter.notifyDataSetChanged();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

