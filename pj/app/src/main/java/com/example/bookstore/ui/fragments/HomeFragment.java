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
                List<Book> books = new ArrayList<>();
                // Using picsum.photos for reliable placeholder images with book-like aspect ratio
                books.add(new Book(1, "The Great Gatsby", "F. Scott Fitzgerald", 12.99, "A classic novel", "https://picsum.photos/seed/book1/200/300", 4.5, 1250, "Fiction", true));
                books.add(new Book(2, "1984", "George Orwell", 14.99, "Dystopian novel", "https://picsum.photos/seed/book2/200/300", 4.8, 2150, "Fiction", true));
                books.add(new Book(3, "To Kill a Mockingbird", "Harper Lee", 13.99, "Classic literature", "https://picsum.photos/seed/book3/200/300", 4.7, 1890, "Fiction", true));
                books.add(new Book(4, "Pride and Prejudice", "Jane Austen", 11.99, "Romance classic", "https://picsum.photos/seed/book4/200/300", 4.6, 1670, "Romance", true));
                books.add(new Book(5, "The Catcher in the Rye", "J.D. Salinger", 13.99, "Coming of age", "https://picsum.photos/seed/book5/200/300", 4.3, 980, "Fiction", true));
                books.add(new Book(6, "Animal Farm", "George Orwell", 10.99, "Political satire", "https://picsum.photos/seed/book6/200/300", 4.5, 1340, "Fiction", true));
                books.add(new Book(7, "The Hobbit", "J.R.R. Tolkien", 15.99, "Fantasy adventure", "https://picsum.photos/seed/book7/200/300", 4.9, 3200, "Fantasy", true));
                books.add(new Book(8, "Harry Potter", "J.K. Rowling", 16.99, "Magical adventure", "https://picsum.photos/seed/book8/200/300", 4.9, 4500, "Fantasy", true));
                books.add(new Book(9, "The Alchemist", "Paulo Coelho", 12.99, "Philosophical novel", "https://picsum.photos/seed/book9/200/300", 4.4, 1560, "Fiction", true));
                books.add(new Book(10, "Brave New World", "Aldous Huxley", 14.99, "Dystopian future", "https://picsum.photos/seed/book10/200/300", 4.5, 1420, "Fiction", true));
                recycler.setAdapter(new BookAdapter(books));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

