package com.example.bookstore.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "addresses",
        foreignKeys = @ForeignKey(entity = User.class,
                                 parentColumns = "id",
                                 childColumns = "userId",
                                 onDelete = ForeignKey.CASCADE),
        indices = {@Index("userId")})
public class Address {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int userId;
    private String recipientName;
    private String phone;
    private String address;
    private String city;
    private String district;
    private String ward;
    private boolean isDefault;

    public Address() {
        this.isDefault = false;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getRecipientName() { return recipientName; }
    public void setRecipientName(String recipientName) { this.recipientName = recipientName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public String getWard() { return ward; }
    public void setWard(String ward) { this.ward = ward; }

    public boolean isDefault() { return isDefault; }
    public void setDefault(boolean aDefault) { isDefault = aDefault; }
}

