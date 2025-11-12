package com.example.bookstore.ui.fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bookstore.R;
import com.example.bookstore.adapters.BookAdapter;
import com.example.bookstore.models.Book;
import com.example.bookstore.utils.BookDataLoader;
import java.util.List;

public class CatalogFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_catalog, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            RecyclerView recycler = view.findViewById(R.id.catalog_grid_recycler);
            if (recycler != null) {
                recycler.setLayoutManager(new GridLayoutManager(getContext(), 2));

                // Lấy tất cả sách từ BookDataLoader
                List<Book> books = BookDataLoader.getAllBooks();

                recycler.setAdapter(new BookAdapter(books));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

