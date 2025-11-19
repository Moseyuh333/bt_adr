package com.example.bookstore.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.bookstore.models.Book;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class RecentlyViewedManager {
    private static RecentlyViewedManager instance;
    private SharedPreferences sharedPreferences;
    private Gson gson;
    private static final String PREFS_NAME = "BookstorePrefs";
    private static final String KEY_RECENTLY_VIEWED = "recently_viewed_books";
    private static final int MAX_ITEMS = 20;

    private RecentlyViewedManager(Context context) {
        sharedPreferences = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public static synchronized RecentlyViewedManager getInstance(Context context) {
        if (instance == null) {
            instance = new RecentlyViewedManager(context);
        }
        return instance;
    }

    public void addRecentlyViewed(Book book) {
        List<Book> recentlyViewed = getRecentlyViewed();

        // Remove if already exists (to move to front)
        recentlyViewed.removeIf(b -> b.id == book.id);

        // Add to front
        recentlyViewed.add(0, book);

        // Keep only MAX_ITEMS
        if (recentlyViewed.size() > MAX_ITEMS) {
            recentlyViewed = recentlyViewed.subList(0, MAX_ITEMS);
        }

        saveRecentlyViewed(recentlyViewed);
    }

    public List<Book> getRecentlyViewed() {
        String json = sharedPreferences.getString(KEY_RECENTLY_VIEWED, "");
        if (json.isEmpty()) {
            return new ArrayList<>();
        }

        try {
            return gson.fromJson(json, new TypeToken<List<Book>>() {}.getType());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void saveRecentlyViewed(List<Book> books) {
        String json = gson.toJson(books);
        sharedPreferences.edit().putString(KEY_RECENTLY_VIEWED, json).apply();
    }

    public void clear() {
        sharedPreferences.edit().remove(KEY_RECENTLY_VIEWED).apply();
    }
}

