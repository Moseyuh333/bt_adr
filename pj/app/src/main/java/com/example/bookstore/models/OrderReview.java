package com.example.bookstore.models;

import java.util.ArrayList;
import java.util.List;

public class OrderReview {
    public int rating; // 1-5 stars
    public String comment;
    public List<String> mediaUrls; // URLs for images/videos
    public String reviewDate;
    public String userName;

    public OrderReview() {
        this.mediaUrls = new ArrayList<>();
        this.rating = 5;
    }

    public OrderReview(int rating, String comment, String userName) {
        this();
        this.rating = rating;
        this.comment = comment;
        this.userName = userName;
    }
}

