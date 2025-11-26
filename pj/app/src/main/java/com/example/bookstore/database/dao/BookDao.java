package com.example.bookstore.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bookstore.database.entities.Book;

import java.util.List;

@Dao
public interface BookDao {
    @Insert
    long insert(Book book);

    @Update
    void update(Book book);

    @Delete
    void delete(Book book);

    @Query("SELECT * FROM books WHERE id = :bookId")
    Book getBookById(int bookId);

    @Query("SELECT * FROM books WHERE isActive = 1 ORDER BY id DESC")
    List<Book> getAllActiveBooks();

    @Query("SELECT * FROM books ORDER BY id DESC")
    List<Book> getAllBooks();

    @Query("SELECT * FROM books WHERE (isActive = 1 AND title LIKE '%' || :query || '%') OR (isActive = 1 AND author LIKE '%' || :query || '%')")
    List<Book> searchBooks(String query);

    @Query("SELECT * FROM books WHERE isActive = 1 AND category = :category ORDER BY id DESC")
    List<Book> getBooksByCategory(String category);

    @Query("SELECT DISTINCT category FROM books WHERE isActive = 1 AND category IS NOT NULL ORDER BY category")
    List<String> getAllCategories();

    @Query("UPDATE books SET stock = stock - :quantity WHERE id = :bookId AND stock >= :quantity")
    int decreaseStock(int bookId, int quantity);

    @Query("UPDATE books SET stock = stock + :quantity WHERE id = :bookId")
    void increaseStock(int bookId, int quantity);

    @Query("UPDATE books SET isActive = :isActive WHERE id = :bookId")
    void updateBookStatus(int bookId, boolean isActive);

    @Query("SELECT * FROM books WHERE isActive = 1 ORDER BY id DESC LIMIT :limit")
    List<Book> getLatestBooks(int limit);
}

