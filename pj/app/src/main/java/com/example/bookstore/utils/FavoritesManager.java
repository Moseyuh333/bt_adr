package com.example.bookstore.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.bookstore.models.Book;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class FavoritesManager {
    private static FavoritesManager instance;
    private SharedPreferences sharedPreferences;
    private Gson gson;
    private static final String PREFS_NAME = "BookstorePrefs";
    private static final String KEY_FAVORITES = "favorite_books";

    private FavoritesManager(Context context) {
        sharedPreferences = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public static synchronized FavoritesManager getInstance(Context context) {
        if (instance == null) {
            instance = new FavoritesManager(context);
        }
        return instance;
    }

    public void addFavorite(Book book) {
        List<Book> favorites = getFavorites();

        // Check if already exists
        for (Book fav : favorites) {
            if (fav.id == book.id) {
                return; // Already in favorites
            }
        }

        favorites.add(book);
        saveFavorites(favorites);
    }

    public void removeFavorite(int bookId) {
        List<Book> favorites = getFavorites();
        favorites.removeIf(book -> book.id == bookId);
        saveFavorites(favorites);
    }

    public boolean isFavorite(int bookId) {
        List<Book> favorites = getFavorites();
        for (Book book : favorites) {
            if (book.id == bookId) {
                return true;
            }
        }
        return false;
    }

    public List<Book> getFavorites() {
        String json = sharedPreferences.getString(KEY_FAVORITES, "");
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

    private void saveFavorites(List<Book> favorites) {
        String json = gson.toJson(favorites);
        sharedPreferences.edit().putString(KEY_FAVORITES, json).apply();
    }

    public void toggleFavorite(Book book) {
        if (isFavorite(book.id)) {
            removeFavorite(book.id);
        } else {
            addFavorite(book);
        }
    }
}

