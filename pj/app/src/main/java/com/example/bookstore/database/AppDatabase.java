package com.example.bookstore.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.bookstore.database.dao.AddressDao;
import com.example.bookstore.database.dao.BookDao;
import com.example.bookstore.database.dao.CartDao;
import com.example.bookstore.database.dao.OrderDao;
import com.example.bookstore.database.dao.OrderItemDao;
import com.example.bookstore.database.dao.UserDao;
import com.example.bookstore.database.entities.Address;
import com.example.bookstore.database.entities.Book;
import com.example.bookstore.database.entities.Cart;
import com.example.bookstore.database.entities.Order;
import com.example.bookstore.database.entities.OrderItem;
import com.example.bookstore.database.entities.User;

@Database(entities = {User.class, Book.class, Address.class, Order.class, OrderItem.class, Cart.class},
          version = 1,
          exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract UserDao userDao();
    public abstract BookDao bookDao();
    public abstract AddressDao addressDao();
    public abstract OrderDao orderDao();
    public abstract OrderItemDao orderItemDao();
    public abstract CartDao cartDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                context.getApplicationContext(),
                AppDatabase.class,
                "bookstore_database"
            )
            .fallbackToDestructiveMigration()
            .build();
        }
        return instance;
    }
}

