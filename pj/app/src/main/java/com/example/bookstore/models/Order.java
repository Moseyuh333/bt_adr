package com.example.bookstore.models;

public class Order {
    private String id;
    private String customerId;
    private String customerName;
    private String shippingAddress;
    private String status;
    private double totalAmount;
    private String orderDate;

    public Order(String id, String customerId, String customerName, String shippingAddress, String status, double totalAmount, String orderDate) {
        this.id = id;
        this.customerId = customerId;
        this.customerName = customerName;
        this.shippingAddress = shippingAddress;
        this.status = status;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
    }

    // Getters
    public String getId() { return id; }
    public String getCustomerId() { return customerId; }
    public String getCustomerName() { return customerName; }
    public String getShippingAddress() { return shippingAddress; }
    public String getStatus() { return status; }
    public double getTotalAmount() { return totalAmount; }
    public String getOrderDate() { return orderDate; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
    public void setStatus(String status) { this.status = status; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public void setOrderDate(String orderDate) { this.orderDate = orderDate; }
}
