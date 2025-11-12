package com.example.bookstore.ui.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore.R;
import com.example.bookstore.adapters.BookAdapter;
import com.example.bookstore.models.Book;
import com.example.bookstore.utils.BookDataLoader;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private EditText searchInput;
    private RecyclerView searchRecycler;
    private TextView emptyText;
    private BookAdapter adapter;
    private List<Book> allBooks;
    private List<Book> searchResults;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            searchInput = view.findViewById(R.id.search_input);
            searchRecycler = view.findViewById(R.id.search_results_recycler);
            emptyText = view.findViewById(R.id.empty_search_text);

            allBooks = BookDataLoader.getAllBooks();
            searchResults = new ArrayList<>();

            searchRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
            adapter = new BookAdapter(searchResults);
            searchRecycler.setAdapter(adapter);

            // Search listener
            searchInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    performSearch(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });

            showEmptyState();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void performSearch(String query) {
        try {
            searchResults.clear();

            if (query.trim().isEmpty()) {
                showEmptyState();
                adapter.notifyDataSetChanged();
                return;
            }

            String lowerQuery = query.toLowerCase();
            for (Book book : allBooks) {
                if (book.title.toLowerCase().contains(lowerQuery) ||
                    book.author.toLowerCase().contains(lowerQuery) ||
                    book.category.toLowerCase().contains(lowerQuery)) {
                    searchResults.add(book);
                }
            }

            if (searchResults.isEmpty()) {
                emptyText.setText("Không tìm thấy sách nào");
                emptyText.setVisibility(View.VISIBLE);
                searchRecycler.setVisibility(View.GONE);
            } else {
                emptyText.setVisibility(View.GONE);
                searchRecycler.setVisibility(View.VISIBLE);
            }

            adapter.notifyDataSetChanged();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showEmptyState() {
        emptyText.setText("Nhập tên sách, tác giả hoặc thể loại để tìm kiếm");
        emptyText.setVisibility(View.VISIBLE);
        searchRecycler.setVisibility(View.GONE);
    }
}

