package com.example.bookstore.models;

public class Customer {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private boolean isActive;
    private String registrationDate;
    private int totalOrders;

    public Customer(String id, String name, String email, String phone, String address, boolean isActive, String registrationDate, int totalOrders) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.isActive = isActive;
        this.registrationDate = registrationDate;
        this.totalOrders = totalOrders;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public boolean isActive() { return isActive; }
    public String getRegistrationDate() { return registrationDate; }
    public int getTotalOrders() { return totalOrders; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAddress(String address) { this.address = address; }
    public void setActive(boolean active) { isActive = active; }
    public void setRegistrationDate(String registrationDate) { this.registrationDate = registrationDate; }
    public void setTotalOrders(int totalOrders) { this.totalOrders = totalOrders; }
}
