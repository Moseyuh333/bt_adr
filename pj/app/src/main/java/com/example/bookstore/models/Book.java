package com.example.bookstore.models;

import java.io.Serializable;

public class Book implements Serializable {
    private static final long serialVersionUID = 1L;

    public int id;
    public String title;
    public String author;
    public String description;
    public String coverImage;
    public String category;
    public double price;
    public double rating;
    public int reviews;
    public boolean inStock;
    public int quantity; // Số lượng tồn kho

    // Constructor cho admin (với String id)
    public Book(String id, String title, String author, double price, String coverImage, String description, String category, double rating, int stock) {
        this.id = Integer.parseInt(id);
        this.title = title;
        this.author = author;
        this.price = price;
        this.description = description;
        this.coverImage = coverImage;
        this.rating = rating;
        this.reviews = 0;
        this.category = category;
        this.quantity = stock;
        this.inStock = stock > 0;
    }

    public Book(int id, String title, String author, double price, String description,
                String coverImage, double rating, int reviews, String category, boolean inStock) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.description = description;
        this.coverImage = coverImage;
        this.rating = rating;
        this.reviews = reviews;
        this.category = category;
        this.inStock = inStock;
        this.quantity = inStock ? 100 : 0; // Mặc định 100 cái nếu có sẵn, 0 nếu hết
    }

    public Book(int id, String title, String author, double price, String description,
                String coverImage, double rating, int reviews, String category, boolean inStock, int quantity) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.description = description;
        this.coverImage = coverImage;
        this.rating = rating;
        this.reviews = reviews;
        this.category = category;
        this.inStock = inStock;
        this.quantity = quantity;
    }

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }
    public String getCoverImage() { return coverImage; }
    public double getRating() { return rating; }
    public int getReviews() { return reviews; }
    public String getCategory() { return category; }
    public boolean isInStock() { return inStock; }
    public int getStock() { return quantity; }
    public int getQuantity() { return quantity; }
}
