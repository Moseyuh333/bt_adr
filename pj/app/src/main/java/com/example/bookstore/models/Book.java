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
    public double originalPrice; // Giá gốc
    public int discount; // Phần trăm giảm giá
    public double rating;
    public int reviews;
    public boolean inStock;
    public int quantity; // Số lượng tồn kho
    public int soldCount; // Số lượng đã bán
    public String shopName; // Tên shop
    public String publisher; // Nhà xuất bản
    public String url; // URL sản phẩm
    public String highlights; // Điểm nổi bật
    public String specifications; // Thông số kỹ thuật

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
        this.originalPrice = price * 1.2; // Default: giá gốc cao hơn 20%
        this.discount = 15; // Default: 15% discount
        this.soldCount = (int)(Math.random() * 500) + 50; // Random 50-550
        this.shopName = "BookStore Official";
        this.publisher = "NXB Trẻ";
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
        this.originalPrice = price * 1.25; // Default: giá gốc cao hơn 25%
        this.discount = 20; // Default: 20% discount
        this.soldCount = (int)(Math.random() * 1000) + 100; // Random 100-1100
        this.shopName = "BookStore Official";
        this.publisher = "NXB Kim Đồng";
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
        this.originalPrice = price * 1.3; // Default: giá gốc cao hơn 30%
        this.discount = 23; // Default: 23% discount
        this.soldCount = (int)(Math.random() * 800) + 150; // Random 150-950
        this.shopName = "BookStore Official";
        this.publisher = "NXB Văn Học";
    }

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public double getPrice() { return price; }
    public double getOriginalPrice() { return originalPrice; }
    public int getDiscount() { return discount; }
    public String getDescription() { return description; }
    public String getCoverImage() { return coverImage; }
    public double getRating() { return rating; }
    public int getReviews() { return reviews; }
    public String getCategory() { return category; }
    public boolean isInStock() { return inStock; }
    public int getStock() { return quantity; }
    public int getQuantity() { return quantity; }
    public int getSoldCount() { return soldCount; }
    public String getShopName() { return shopName; }
    public String getPublisher() { return publisher; }
    public String getUrl() { return url; }
    public String getHighlights() { return highlights; }
    public String getSpecifications() { return specifications; }
}
