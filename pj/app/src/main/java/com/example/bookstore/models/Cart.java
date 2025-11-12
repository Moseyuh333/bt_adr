package com.example.bookstore.models;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private static Cart instance;
    private List<CartItem> items;
    private double subtotal;
    private double tax;
    private double discount;
    private double shippingFee;
    private Voucher appliedVoucher;

    private Cart() {
        this.items = new ArrayList<>();
        this.subtotal = 0;
        this.tax = 0;
        this.discount = 0;
        this.shippingFee = 0; // Free shipping
        this.appliedVoucher = null;
    }

    public static Cart getInstance() {
        if (instance == null) {
            instance = new Cart();
        }
        return instance;
    }

    public void addItem(Book book, int quantity) {
        for (CartItem item : items) {
            if (item.book.id == book.id) {
                item.quantity += quantity;
                calculateTotal();
                return;
            }
        }
        items.add(new CartItem(book, quantity));
        calculateTotal();
    }

    public void removeItem(int bookId) {
        items.removeIf(item -> item.book.id == bookId);
        calculateTotal();
    }

    public void updateQuantity(int bookId, int quantity) {
        for (CartItem item : items) {
            if (item.book.id == bookId) {
                if (quantity <= 0) {
                    removeItem(bookId);
                } else {
                    item.quantity = quantity;
                }
                calculateTotal();
                return;
            }
        }
    }

    public void calculateTotal() {
        this.subtotal = 0;
        for (CartItem item : items) {
            this.subtotal += item.getTotalPrice();
        }
        this.tax = 0; // No tax for VND

        // Apply voucher discount if exists
        if (appliedVoucher != null && appliedVoucher.isValid()) {
            this.discount = appliedVoucher.calculateDiscount(this.subtotal);
        } else {
            this.discount = 0;
        }
    }

    public void applyVoucher(Voucher voucher) {
        if (voucher.isValid()) {
            this.appliedVoucher = voucher;
            calculateTotal();
        }
    }

    public void removeVoucher() {
        this.appliedVoucher = null;
        this.discount = 0;
        calculateTotal();
    }

    public List<CartItem> getItems() {
        return items;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getTax() {
        return tax;
    }

    public double getDiscount() {
        return discount;
    }

    public double getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(double fee) {
        this.shippingFee = fee;
        calculateTotal();
    }

    public double getTotal() {
        return subtotal + tax + shippingFee - discount;
    }

    public Voucher getAppliedVoucher() {
        return appliedVoucher;
    }

    public int getItemCount() {
        return items.size();
    }

    public void clear() {
        items.clear();
        appliedVoucher = null;
        calculateTotal();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}

