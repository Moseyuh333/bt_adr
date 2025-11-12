package com.example.bookstore.models;

import java.util.ArrayList;
import java.util.List;

public class Order {
    public int id;
    public List<CartItem> items;
    public String orderDate;
    public String deliveryDate;
    public String status; // PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED
    public double subtotal;
    public double tax;
    public double discount;
    public double shippingFee;
    public double total;
    public String voucherCode;
    public String customerName;
    public String customerEmail;
    public String customerPhone;
    public String deliveryAddress;
    public String paymentMethod; // CASH, CARD, ONLINE

    public Order() {
        this.items = new ArrayList<>();
        this.status = "PENDING";
        this.tax = 0;
        this.discount = 0;
        this.shippingFee = 0;
    }

    public double calculateTotal() {
        this.subtotal = 0;
        for (CartItem item : items) {
            this.subtotal += item.getTotalPrice();
        }
        this.tax = 0; // No tax for VND
        this.total = this.subtotal + this.tax + this.shippingFee - this.discount;
        return this.total;
    }

    public void applyDiscount(double discountAmount) {
        this.discount = discountAmount;
        calculateTotal();
    }

    public void setShippingFee(double fee) {
        this.shippingFee = fee;
        calculateTotal();
    }
}

