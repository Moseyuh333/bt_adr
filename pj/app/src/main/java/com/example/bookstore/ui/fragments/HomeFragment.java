package com.example.bookstore.ui.fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bookstore.R;
import com.example.bookstore.adapters.BookAdapter;
import com.example.bookstore.models.Book;
import com.example.bookstore.utils.BookDataLoader;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            RecyclerView recycler = view.findViewById(R.id.featured_books_recycler);
            if (recycler != null) {
                recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

                // Lấy 10 sách đầu tiên làm sách nổi bật
                List<Book> books = new ArrayList<>();
                List<Book> allBooks = BookDataLoader.getAllBooks();
                for (int i = 0; i < Math.min(10, allBooks.size()); i++) {
                    books.add(allBooks.get(i));
                }

                recycler.setAdapter(new BookAdapter(books));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

