package com.example.bookstore.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bookstore.database.entities.Address;

import java.util.List;

@Dao
public interface AddressDao {
    @Insert
    long insert(Address address);

    @Update
    void update(Address address);

    @Delete
    void delete(Address address);

    @Query("SELECT * FROM addresses WHERE id = :addressId")
    Address getAddressById(int addressId);

    @Query("SELECT * FROM addresses WHERE userId = :userId ORDER BY isDefault DESC, id DESC")
    List<Address> getAddressesByUserId(int userId);

    @Query("SELECT * FROM addresses WHERE userId = :userId AND isDefault = 1 LIMIT 1")
    Address getDefaultAddress(int userId);

    @Query("UPDATE addresses SET isDefault = 0 WHERE userId = :userId")
    void clearDefaultAddresses(int userId);

    @Query("UPDATE addresses SET isDefault = 1 WHERE id = :addressId")
    void setDefaultAddress(int addressId);
}

