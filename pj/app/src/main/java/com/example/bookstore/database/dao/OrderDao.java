package com.example.bookstore.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bookstore.database.entities.Order;

import java.util.List;

@Dao
public interface OrderDao {
    @Insert
    long insert(Order order);

    @Update
    void update(Order order);

    @Delete
    void delete(Order order);

    @Query("SELECT * FROM orders WHERE id = :orderId")
    Order getOrderById(int orderId);

    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY createdAt DESC")
    List<Order> getOrdersByUserId(int userId);

    @Query("SELECT * FROM orders WHERE userId = :userId AND status = :status ORDER BY createdAt DESC")
    List<Order> getOrdersByUserIdAndStatus(int userId, String status);

    @Query("SELECT * FROM orders ORDER BY createdAt DESC")
    List<Order> getAllOrders();

    @Query("SELECT * FROM orders WHERE status = :status ORDER BY createdAt DESC")
    List<Order> getOrdersByStatus(String status);

    @Query("UPDATE orders SET status = :status, updatedAt = :updatedAt WHERE id = :orderId")
    void updateOrderStatus(int orderId, String status, long updatedAt);

    @Query("UPDATE orders SET status = :status, cancelReason = :reason, updatedAt = :updatedAt WHERE id = :orderId")
    void cancelOrder(int orderId, String status, String reason, long updatedAt);

    @Query("SELECT COUNT(*) FROM orders WHERE userId = :userId")
    int getOrderCountByUser(int userId);

    @Query("SELECT COUNT(*) FROM orders")
    int getTotalOrderCount();

    @Query("SELECT SUM(totalAmount) FROM orders WHERE status = 'DELIVERED'")
    Double getTotalRevenue();
}

