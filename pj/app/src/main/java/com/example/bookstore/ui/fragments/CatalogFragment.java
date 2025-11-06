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
import java.util.ArrayList;
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
                List<Book> books = new ArrayList<>();

                // Fiction books
                books.add(new Book(1, "The Great Gatsby", "F. Scott Fitzgerald", 12.99, "A classic American novel", "https://picsum.photos/seed/cat1/200/300", 4.5, 1250, "Fiction", true));
                books.add(new Book(2, "1984", "George Orwell", 14.99, "Dystopian masterpiece", "https://picsum.photos/seed/cat2/200/300", 4.8, 2150, "Fiction", true));
                books.add(new Book(3, "To Kill a Mockingbird", "Harper Lee", 13.99, "American classic", "https://picsum.photos/seed/cat3/200/300", 4.7, 1890, "Fiction", true));
                books.add(new Book(4, "The Catcher in the Rye", "J.D. Salinger", 13.99, "Coming of age story", "https://picsum.photos/seed/cat4/200/300", 4.3, 980, "Fiction", true));

                // Romance
                books.add(new Book(5, "Pride and Prejudice", "Jane Austen", 11.99, "Timeless romance", "https://picsum.photos/seed/cat5/200/300", 4.6, 1670, "Romance", true));
                books.add(new Book(6, "Jane Eyre", "Charlotte Brontë", 12.99, "Gothic romance", "https://picsum.photos/seed/cat6/200/300", 4.5, 1340, "Romance", true));

                // Fantasy
                books.add(new Book(7, "The Hobbit", "J.R.R. Tolkien", 15.99, "Epic fantasy adventure", "https://picsum.photos/seed/cat7/200/300", 4.9, 3200, "Fantasy", true));
                books.add(new Book(8, "Harry Potter", "J.K. Rowling", 16.99, "Magical journey", "https://picsum.photos/seed/cat8/200/300", 4.9, 4500, "Fantasy", true));
                books.add(new Book(9, "The Name of the Wind", "Patrick Rothfuss", 17.99, "Fantasy masterpiece", "https://picsum.photos/seed/cat9/200/300", 4.7, 2800, "Fantasy", true));

                // Science Fiction
                books.add(new Book(10, "Dune", "Frank Herbert", 18.99, "Sci-fi epic", "https://picsum.photos/seed/cat10/200/300", 4.8, 3100, "Sci-Fi", true));
                books.add(new Book(11, "Foundation", "Isaac Asimov", 14.99, "Classic sci-fi", "https://picsum.photos/seed/cat11/200/300", 4.6, 1900, "Sci-Fi", true));
                books.add(new Book(12, "Neuromancer", "William Gibson", 15.99, "Cyberpunk classic", "https://picsum.photos/seed/cat12/200/300", 4.5, 1600, "Sci-Fi", true));

                // Mystery & Thriller
                books.add(new Book(13, "The Girl with the Dragon Tattoo", "Stieg Larsson", 14.99, "Gripping mystery", "https://picsum.photos/seed/cat13/200/300", 4.4, 2200, "Mystery", true));
                books.add(new Book(14, "Gone Girl", "Gillian Flynn", 13.99, "Psychological thriller", "https://picsum.photos/seed/cat14/200/300", 4.3, 1850, "Thriller", true));
                books.add(new Book(15, "The Da Vinci Code", "Dan Brown", 15.99, "Mystery thriller", "https://picsum.photos/seed/cat15/200/300", 4.2, 2500, "Mystery", true));

                // Non-Fiction
                books.add(new Book(16, "Sapiens", "Yuval Noah Harari", 19.99, "Human history", "https://picsum.photos/seed/cat16/200/300", 4.7, 3400, "Non-Fiction", true));
                books.add(new Book(17, "Educated", "Tara Westover", 16.99, "Memoir", "https://picsum.photos/seed/cat17/200/300", 4.8, 2900, "Biography", true));
                books.add(new Book(18, "Atomic Habits", "James Clear", 17.99, "Self-improvement", "https://picsum.photos/seed/cat18/200/300", 4.9, 4200, "Self-Help", true));

                // More Fiction
                books.add(new Book(19, "The Alchemist", "Paulo Coelho", 12.99, "Philosophical journey", "https://picsum.photos/seed/cat19/200/300", 4.4, 1560, "Fiction", true));
                books.add(new Book(20, "Brave New World", "Aldous Huxley", 14.99, "Dystopian vision", "https://picsum.photos/seed/cat20/200/300", 4.5, 1420, "Fiction", true));

                // Add more variety
                for (int i = 21; i <= 40; i++) {
                    String category = i % 5 == 0 ? "Fantasy" : i % 4 == 0 ? "Mystery" : i % 3 == 0 ? "Romance" : "Fiction";
                    books.add(new Book(i, "Book Title " + i, "Author " + (i % 15 + 1),
                        9.99 + (i % 10), "Description for book " + i,
                        "https://picsum.photos/seed/cat" + i + "/200/300",
                        3.5 + (i % 3) * 0.5, 100 + i * 25, category, true));
                }

                recycler.setAdapter(new BookAdapter(books));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

