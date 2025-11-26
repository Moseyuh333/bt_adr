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
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore.R;
import com.example.bookstore.adapters.BookAdapter;
import com.example.bookstore.models.Book;
import com.example.bookstore.utils.FavoritesManager;

import java.util.List;

public class FavoritesFragment extends Fragment {

    private RecyclerView favoritesRecycler;
    private View emptyFavoritesLayout;
    private FavoritesManager favoritesManager;
    private BookAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            favoritesManager = FavoritesManager.getInstance(requireContext());

            favoritesRecycler = view.findViewById(R.id.favorites_recycler);
            emptyFavoritesLayout = view.findViewById(R.id.empty_favorites_layout);

            // Set up grid layout with 2 columns
            favoritesRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));

            loadFavorites();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadFavorites() {
        try {
            List<Book> favorites = favoritesManager.getFavorites();

            if (favorites.isEmpty()) {
                if (emptyFavoritesLayout != null) emptyFavoritesLayout.setVisibility(View.VISIBLE);
                favoritesRecycler.setVisibility(View.GONE);
            } else {
                if (emptyFavoritesLayout != null) emptyFavoritesLayout.setVisibility(View.GONE);
                favoritesRecycler.setVisibility(View.VISIBLE);

                adapter = new BookAdapter(favorites);
                favoritesRecycler.setAdapter(adapter);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFavorites(); // Refresh when coming back
    }
}

