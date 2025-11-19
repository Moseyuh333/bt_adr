package com.example.bookstore.models;

import java.io.Serializable;

public class User implements Serializable {
    public int id;
    public String name, email, phone, address;
    public String role; // "admin" or "customer"
    public boolean isBanned;

    public User() {}

    public User(String name, String email, String phone, String address) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.role = "customer";
        this.isBanned = false;
    }

    public User(int id, String name, String email, String role, boolean isBanned) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.isBanned = isBanned;
    }
}

