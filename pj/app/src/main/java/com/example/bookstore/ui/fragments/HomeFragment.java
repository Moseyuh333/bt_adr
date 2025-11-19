package com.example.bookstore.ui.fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bookstore.R;
import com.example.bookstore.adapters.BookAdapter;
import com.example.bookstore.adapters.CategoryAdapter;
import com.example.bookstore.models.Book;
import com.example.bookstore.utils.BookDataLoader;
import com.example.bookstore.utils.FavoritesManager;
import com.example.bookstore.utils.RecentlyViewedManager;
import java.util.ArrayList;
import java.util.Arrays;
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
            List<Book> allBooks = BookDataLoader.getAllBooks();
            FavoritesManager favoritesManager = FavoritesManager.getInstance(requireContext());
            RecentlyViewedManager recentlyViewedManager = RecentlyViewedManager.getInstance(requireContext());

            // Favorites Section
            View favoritesSection = view.findViewById(R.id.favorites_section);
            RecyclerView favoritesRecycler = view.findViewById(R.id.favorites_recycler);
            List<Book> favorites = favoritesManager.getFavorites();
            if (!favorites.isEmpty()) {
                favoritesSection.setVisibility(View.VISIBLE);
                favoritesRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                favoritesRecycler.setAdapter(new BookAdapter(favorites));
            }

            // Recently Viewed Section
            View recentlyViewedSection = view.findViewById(R.id.recently_viewed_section);
            RecyclerView recentlyViewedRecycler = view.findViewById(R.id.recently_viewed_recycler);
            List<Book> recentlyViewed = recentlyViewedManager.getRecentlyViewed();
            if (!recentlyViewed.isEmpty()) {
                recentlyViewedSection.setVisibility(View.VISIBLE);
                recentlyViewedRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                recentlyViewedRecycler.setAdapter(new BookAdapter(recentlyViewed));
            }

            // Explore button - Navigate to Categories
            Button exploreCatalogBtn = view.findViewById(R.id.explore_catalog_btn);
            if (exploreCatalogBtn != null) {
                exploreCatalogBtn.setOnClickListener(v -> {
                    try {
                        Navigation.findNavController(v).navigate(R.id.categoryFragment);
                    } catch (Exception e) {
                        // Fallback to catalog if category not available
                        try {
                            Navigation.findNavController(v).navigate(R.id.catalogFragment);
                        } catch (Exception ex) {
                            // Handle navigation error
                        }
                    }
                });
            }

            // Categories RecyclerView
            RecyclerView categoriesRecycler = view.findViewById(R.id.categories_recycler);
            if (categoriesRecycler != null) {
                categoriesRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.HORIZONTAL, false));
                List<String> categories = Arrays.asList("Fiction", "Fantasy", "Science Fiction", "Romance", "Mystery", "Thriller");
                categoriesRecycler.setAdapter(new CategoryAdapter(categories, category -> {
                    // Navigate to category page
                    try {
                        Bundle args = new Bundle();
                        args.putString("category", category);
                        Navigation.findNavController(view).navigate(R.id.categoryFragment, args);
                    } catch (Exception e) {
                        // Handle navigation error
                    }
                }));
            }

            // Featured Books (Top rated)
            RecyclerView featuredRecycler = view.findViewById(R.id.featured_books_recycler);
            if (featuredRecycler != null) {
                featuredRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

                List<Book> featuredBooks = new ArrayList<>();
                // Get books with highest ratings
                for (Book book : allBooks) {
                    if (book.rating >= 4.7 && featuredBooks.size() < 10) {
                        featuredBooks.add(book);
                    }
                }
                featuredRecycler.setAdapter(new BookAdapter(featuredBooks));
            }

            // New Books (Last 10)
            RecyclerView newBooksRecycler = view.findViewById(R.id.new_books_recycler);
            if (newBooksRecycler != null) {
                newBooksRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

                List<Book> newBooks = new ArrayList<>();
                int start = Math.max(0, allBooks.size() - 10);
                for (int i = start; i < allBooks.size(); i++) {
                    newBooks.add(allBooks.get(i));
                }
                newBooksRecycler.setAdapter(new BookAdapter(newBooks));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

