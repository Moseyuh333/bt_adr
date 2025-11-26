package com.example.bookstore.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart",
        foreignKeys = {
            @ForeignKey(entity = User.class,
                       parentColumns = "id",
                       childColumns = "userId",
                       onDelete = ForeignKey.CASCADE),
            @ForeignKey(entity = Book.class,
                       parentColumns = "id",
                       childColumns = "bookId",
                       onDelete = ForeignKey.CASCADE)
        },
        indices = {@Index("userId"), @Index("bookId"), @Index(value = {"userId", "bookId"}, unique = true)})
public class Cart {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int userId;
    private int bookId;
    private int quantity;
    private long addedAt;

    public Cart() {
        this.quantity = 1;
        this.addedAt = System.currentTimeMillis();
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public long getAddedAt() { return addedAt; }
    public void setAddedAt(long addedAt) { this.addedAt = addedAt; }
}

