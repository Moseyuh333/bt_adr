package com.example.bookstore.models;

public class CartItem {
    public Book book;
    public int quantity;
    public CartItem(Book book, int quantity) { this.book = book; this.quantity = quantity; }
    public double getTotalPrice() { return book.price * quantity; }
}

