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
import com.example.bookstore.database.AppDatabase;
import com.example.bookstore.models.Book;
import com.example.bookstore.utils.FavoritesManager;
import com.example.bookstore.utils.RecentlyViewedManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppDatabase database = AppDatabase.getInstance(requireContext());
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            try {
                List<com.example.bookstore.database.entities.Book> dbBooks = database.bookDao().getLatestBooks(20);
                List<Book> allBooks;
                if (dbBooks == null || dbBooks.isEmpty()) {
                    // Fallback: vẫn dùng nguồn demo cũ để không trắng màn
                    allBooks = com.example.bookstore.utils.BookDataLoader.getAllBooks();
                } else {
                    allBooks = convertToOldBookList(dbBooks);
                }

                requireActivity().runOnUiThread(() -> setupUI(view, allBooks));
            } catch (Exception e) {
                e.printStackTrace();
                // Nếu lỗi DB, dùng luôn demo loader
                List<Book> allBooks = com.example.bookstore.utils.BookDataLoader.getAllBooks();
                requireActivity().runOnUiThread(() -> setupUI(view, allBooks));
            }
        });
    }

    private void setupUI(View view, List<Book> allBooks) {
        try {
            FavoritesManager favoritesManager = FavoritesManager.getInstance(requireContext());
            RecentlyViewedManager recentlyViewedManager = RecentlyViewedManager.getInstance(requireContext());

            // Favorites Section
            View favoritesSection = view.findViewById(R.id.favorites_section);
            RecyclerView favoritesRecycler = view.findViewById(R.id.favorites_recycler);
            View seeAllFavoritesBtn = view.findViewById(R.id.see_all_favorites_btn);
            List<Book> favorites = favoritesManager.getFavorites();
            if (!favorites.isEmpty()) {
                favoritesSection.setVisibility(View.VISIBLE);
                favoritesRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                favoritesRecycler.setAdapter(new BookAdapter(favorites));
                if (seeAllFavoritesBtn != null) {
                    seeAllFavoritesBtn.setOnClickListener(v -> {
                        try {
                            Navigation.findNavController(v).navigate(R.id.favoritesFragment);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }

            // Recently Viewed Section
            View recentlyViewedSection = view.findViewById(R.id.recently_viewed_section);
            RecyclerView recentlyViewedRecycler = view.findViewById(R.id.recently_viewed_recycler);
            View seeAllRecentlyViewedBtn = view.findViewById(R.id.see_all_recently_viewed_btn);
            List<Book> recentlyViewed = recentlyViewedManager.getRecentlyViewed();
            if (!recentlyViewed.isEmpty()) {
                recentlyViewedSection.setVisibility(View.VISIBLE);
                recentlyViewedRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                recentlyViewedRecycler.setAdapter(new BookAdapter(recentlyViewed));
                if (seeAllRecentlyViewedBtn != null) {
                    seeAllRecentlyViewedBtn.setOnClickListener(v -> {
                        try {
                            Navigation.findNavController(v).navigate(R.id.recentlyViewedFragment);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }

            // Explore button
            Button exploreCatalogBtn = view.findViewById(R.id.explore_catalog_btn);
            if (exploreCatalogBtn != null) {
                exploreCatalogBtn.setOnClickListener(v -> {
                    try {
                        Navigation.findNavController(v).navigate(R.id.categoryFragment);
                    } catch (Exception e) {
                        try {
                            Navigation.findNavController(v).navigate(R.id.catalogFragment);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }

            // Categories RecyclerView
            RecyclerView categoriesRecycler = view.findViewById(R.id.categories_recycler);
            if (categoriesRecycler != null) {
                // Load categories from database
                AppDatabase database = AppDatabase.getInstance(requireContext());
                ExecutorService categoryExecutor = Executors.newSingleThreadExecutor();
                categoryExecutor.execute(() -> {
                    List<String> dbCategories = database.bookDao().getAllCategories();
                    List<String> categories = new ArrayList<>();
                    if (dbCategories != null && !dbCategories.isEmpty()) {
                        // Filter and clean categories - only add valid ones
                        for (String cat : dbCategories) {
                            if (cat != null && !cat.isEmpty() && cat.length() <= 50 && !cat.contains("<") && !cat.contains(">")) {
                                String cleanCat = cat.replaceAll("<[^>]*>", "").trim();
                                if (!cleanCat.isEmpty() && !categories.contains(cleanCat)) {
                                    categories.add(cleanCat);
                                    if (categories.size() >= 6) break; // Limit to 6 categories
                                }
                            }
                        }
                    }

                    // Fallback if no valid categories found
                    if (categories.isEmpty()) {
                        categories = Arrays.asList("Fiction", "Fantasy", "Science Fiction", "Romance", "Mystery", "Thriller");
                    }
                    
                    final List<String> finalCategories = categories;
                    requireActivity().runOnUiThread(() -> {
                        categoriesRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.HORIZONTAL, false));
                        categoriesRecycler.setAdapter(new CategoryAdapter(finalCategories, category -> {
                            try {
                                Bundle args = new Bundle();
                                args.putString("category", category);
                                Navigation.findNavController(view).navigate(R.id.categoryFragment, args);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }));
                    });
                });
            }

            // Featured Books
            RecyclerView featuredRecycler = view.findViewById(R.id.featured_books_recycler);
            if (featuredRecycler != null) {
                featuredRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                List<Book> featuredBooks = new ArrayList<>();
                for (Book book : allBooks) {
                    if (book.rating >= 4.0 && featuredBooks.size() < 10) {
                        featuredBooks.add(book);
                    }
                }
                featuredRecycler.setAdapter(new BookAdapter(featuredBooks));
            }

            // New Books
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

    private List<Book> convertToOldBookList(List<com.example.bookstore.database.entities.Book> dbBooks) {
        return com.example.bookstore.utils.BookConverter.convertToDisplayBooks(dbBooks);
    }
}
