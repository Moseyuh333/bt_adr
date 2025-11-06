package com.example.bookstore.models;

public class Book {
    public int id;
    public String title, author, description, coverImage, category;
    public double price, rating;
    public int reviews;
    public boolean inStock;

    public Book(int id, String title, String author, double price, String description, String coverImage, double rating, int reviews, String category, boolean inStock) {
        this.id = id; this.title = title; this.author = author; this.price = price; this.description = description;
        this.coverImage = coverImage; this.rating = rating; this.reviews = reviews; this.category = category; this.inStock = inStock;
    }
}

