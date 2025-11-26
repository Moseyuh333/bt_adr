package com.example.bookstore.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bookstore.database.entities.Cart;

import java.util.List;

@Dao
public interface CartDao {
    @Insert
    long insert(Cart cart);

    @Update
    void update(Cart cart);

    @Delete
    void delete(Cart cart);

    @Query("SELECT * FROM cart WHERE id = :cartId")
    Cart getCartItemById(int cartId);

    @Query("SELECT * FROM cart WHERE userId = :userId ORDER BY addedAt DESC")
    List<Cart> getCartItemsByUserId(int userId);

    @Query("SELECT * FROM cart WHERE userId = :userId AND bookId = :bookId LIMIT 1")
    Cart getCartItem(int userId, int bookId);

    @Query("UPDATE cart SET quantity = :quantity WHERE id = :cartId")
    void updateQuantity(int cartId, int quantity);

    @Query("DELETE FROM cart WHERE userId = :userId")
    void clearCart(int userId);

    @Query("SELECT COUNT(*) FROM cart WHERE userId = :userId")
    int getCartItemCount(int userId);

    @Query("DELETE FROM cart WHERE userId = :userId AND bookId IN (:bookIds)")
    void deleteCartItems(int userId, List<Integer> bookIds);
}
