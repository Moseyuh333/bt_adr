package com.example.bookstore.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "order_items",
        foreignKeys = {
            @ForeignKey(entity = Order.class,
                       parentColumns = "id",
                       childColumns = "orderId",
                       onDelete = ForeignKey.CASCADE),
            @ForeignKey(entity = Book.class,
                       parentColumns = "id",
                       childColumns = "bookId",
                       onDelete = ForeignKey.CASCADE)
        },
        indices = {@Index("orderId"), @Index("bookId")})
public class OrderItem {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int orderId;
    private int bookId;
    private String bookTitle;
    private String bookAuthor;
    private String bookImageUrl;
    private double price;
    private int quantity;
    private double subtotal;

    public OrderItem() {}

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }

    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }

    public String getBookAuthor() { return bookAuthor; }
    public void setBookAuthor(String bookAuthor) { this.bookAuthor = bookAuthor; }

    public String getBookImageUrl() { return bookImageUrl; }
    public void setBookImageUrl(String bookImageUrl) { this.bookImageUrl = bookImageUrl; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
}

