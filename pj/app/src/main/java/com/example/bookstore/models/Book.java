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
}

