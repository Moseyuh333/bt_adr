package com.example.bookstore.models;

public class Voucher {
    public String code;
    public String name;
    public double discountPercent;
    public double discountAmount;
    public double minOrderAmount;
    public int maxUsage;
    public int usageCount;
    public boolean isActive;
    public String expiryDate;

    // Discount by percentage
    public Voucher(String code, String name, double discountPercent, double minOrderAmount, int maxUsage, String expiryDate) {
        this.code = code;
        this.name = name;
        this.discountPercent = discountPercent;
        this.discountAmount = 0;
        this.minOrderAmount = minOrderAmount;
        this.maxUsage = maxUsage;
        this.usageCount = 0;
        this.isActive = true;
        this.expiryDate = expiryDate;
    }

    // Discount by fixed amount
    public Voucher(String code, String name, double minOrderAmount, double discountAmount, int maxUsage) {
        this.code = code;
        this.name = name;
        this.discountPercent = 0;
        this.discountAmount = discountAmount;
        this.minOrderAmount = minOrderAmount;
        this.maxUsage = maxUsage;
        this.usageCount = 0;
        this.isActive = true;
        this.expiryDate = "2025-12-31";
    }

    public boolean isValid() {
        return isActive && usageCount < maxUsage;
    }

    public double calculateDiscount(double orderTotal) {
        if (orderTotal < minOrderAmount || !isValid()) {
            return 0;
        }

        if (discountPercent > 0) {
            return orderTotal * (discountPercent / 100.0);
        } else {
            return discountAmount;
        }
    }

    public void incrementUsage() {
        if (usageCount < maxUsage) {
            usageCount++;
        }
    }
}

