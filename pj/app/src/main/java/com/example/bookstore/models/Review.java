package com.example.bookstore.models;

import java.io.Serializable;

public class Review implements Serializable {
    public String reviewerName;
    public float rating;
    public String date;
    public String content;

    public Review(String reviewerName, float rating, String date, String content) {
        this.reviewerName = reviewerName;
        this.rating = rating;
        this.date = date;
        this.content = content;
    }
}

