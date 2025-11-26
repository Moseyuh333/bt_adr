package com.example.bookstore.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore.R;
import com.example.bookstore.adapters.BookAdapter;
import com.example.bookstore.models.Book;
import com.example.bookstore.utils.RecentlyViewedManager;

import java.util.List;

public class RecentlyViewedFragment extends Fragment {

    private RecyclerView recentlyViewedRecycler;
    private View emptyRecentlyViewedLayout;
    private RecentlyViewedManager recentlyViewedManager;
    private BookAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recently_viewed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            recentlyViewedManager = RecentlyViewedManager.getInstance(requireContext());

            recentlyViewedRecycler = view.findViewById(R.id.recently_viewed_recycler);
            emptyRecentlyViewedLayout = view.findViewById(R.id.empty_recently_viewed_layout);

            // Set up grid layout with 2 columns
            recentlyViewedRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));

            loadRecentlyViewed();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadRecentlyViewed() {
        try {
            List<Book> recentlyViewed = recentlyViewedManager.getRecentlyViewed();

            if (recentlyViewed.isEmpty()) {
                if (emptyRecentlyViewedLayout != null) emptyRecentlyViewedLayout.setVisibility(View.VISIBLE);
                recentlyViewedRecycler.setVisibility(View.GONE);
            } else {
                if (emptyRecentlyViewedLayout != null) emptyRecentlyViewedLayout.setVisibility(View.GONE);
                recentlyViewedRecycler.setVisibility(View.VISIBLE);

                adapter = new BookAdapter(recentlyViewed);
                recentlyViewedRecycler.setAdapter(adapter);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadRecentlyViewed(); // Refresh when coming back
    }
}

