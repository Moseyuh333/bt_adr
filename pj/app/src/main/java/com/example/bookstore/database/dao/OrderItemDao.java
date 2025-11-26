package com.example.bookstore.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bookstore.database.entities.OrderItem;

import java.util.List;

@Dao
public interface OrderItemDao {
    @Insert
    long insert(OrderItem orderItem);

    @Insert
    void insertAll(List<OrderItem> orderItems);

    @Update
    void update(OrderItem orderItem);

    @Delete
    void delete(OrderItem orderItem);

    @Query("SELECT * FROM order_items WHERE id = :itemId")
    OrderItem getOrderItemById(int itemId);

    @Query("SELECT * FROM order_items WHERE orderId = :orderId")
    List<OrderItem> getOrderItemsByOrderId(int orderId);

    @Query("DELETE FROM order_items WHERE orderId = :orderId")
    void deleteOrderItemsByOrderId(int orderId);
}

