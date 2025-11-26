package com.example.bookstore.models;

import java.util.List;

public class Order {
    public String id;
    public String customerId;
    public String customerName;
    public String customerEmail;
    public String customerPhone;
    public String deliveryAddress;
    public String shippingAddress;
    public String status;
    public String paymentMethod;
    public String voucherCode;
    public double subtotal;
    public double tax;
    public double discount;
    public double shippingFee;
    public double total;
    public double totalAmount;
    public String orderDate;
    public List<CartItem> items;
    public String cancelReason;
    public String returnReason;
    public OrderReview review;
    public boolean isConfirmedReceived;
    public String confirmedReceivedDate;

    // No-arg constructor
    public Order() {
        this.id = "ORD" + System.currentTimeMillis();
    }

    // Constructor for backward compatibility
    public Order(String id, String customerId, String customerName, String shippingAddress, String status, double totalAmount, String orderDate) {
        this.id = id;
        this.customerId = customerId;
        this.customerName = customerName;
        this.shippingAddress = shippingAddress;
        this.deliveryAddress = shippingAddress;
        this.status = status;
        this.totalAmount = totalAmount;
        this.total = totalAmount;
        this.orderDate = orderDate;
    }

    // Getters
    public String getId() { return id; }
    public String getCustomerId() { return customerId; }
    public String getCustomerName() { return customerName; }
    public String getCustomerEmail() { return customerEmail; }
    public String getCustomerPhone() { return customerPhone; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public String getShippingAddress() { return shippingAddress != null ? shippingAddress : deliveryAddress; }
    public String getStatus() { return status; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getVoucherCode() { return voucherCode; }
    public double getSubtotal() { return subtotal; }
    public double getTax() { return tax; }
    public double getDiscount() { return discount; }
    public double getShippingFee() { return shippingFee; }
    public double getTotal() { return total; }
    public double getTotalAmount() { return totalAmount > 0 ? totalAmount : total; }
    public String getOrderDate() { return orderDate; }
    public List<CartItem> getItems() { return items; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
    public void setStatus(String status) { this.status = status; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public void setVoucherCode(String voucherCode) { this.voucherCode = voucherCode; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
    public void setTax(double tax) { this.tax = tax; }
    public void setDiscount(double discount) { this.discount = discount; }
    public void setShippingFee(double shippingFee) { this.shippingFee = shippingFee; }
    public void setTotal(double total) { this.total = total; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public void setOrderDate(String orderDate) { this.orderDate = orderDate; }
    public void setItems(List<CartItem> items) { this.items = items; }

    public String getCancelReason() { return cancelReason; }
    public void setCancelReason(String cancelReason) { this.cancelReason = cancelReason; }

    public String getReturnReason() { return returnReason; }
    public void setReturnReason(String returnReason) { this.returnReason = returnReason; }

    public OrderReview getReview() { return review; }
    public void setReview(OrderReview review) { this.review = review; }

    public boolean isConfirmedReceived() { return isConfirmedReceived; }
    public void setConfirmedReceived(boolean confirmedReceived) { isConfirmedReceived = confirmedReceived; }

    public String getConfirmedReceivedDate() { return confirmedReceivedDate; }
    public void setConfirmedReceivedDate(String confirmedReceivedDate) { this.confirmedReceivedDate = confirmedReceivedDate; }
}
