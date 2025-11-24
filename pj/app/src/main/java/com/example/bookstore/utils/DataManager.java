package com.example.bookstore.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.bookstore.models.Book;
import com.example.bookstore.models.Order;
import com.example.bookstore.models.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Singleton class để quản lý dữ liệu app (books, orders, users)
 * Sử dụng SharedPreferences để lưu trữ dữ liệu
 */
public class DataManager {
    private static DataManager instance;
    private SharedPreferences prefs;
    private Gson gson;

    private List<Book> books;
    private List<Order> orders;
    private List<User> users;

    private static final String PREFS_NAME = "BookstoreData";
    private static final String KEY_BOOKS = "books";
    private static final String KEY_ORDERS = "orders";
    private static final String KEY_USERS = "users";

    private DataManager(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
        loadData();
    }

    public static synchronized DataManager getInstance(Context context) {
        if (instance == null) {
            instance = new DataManager(context);
        }
        return instance;
    }

    private void loadData() {
        // Load books
        String booksJson = prefs.getString(KEY_BOOKS, null);
        if (booksJson != null) {
            Type type = new TypeToken<List<Book>>(){}.getType();
            books = gson.fromJson(booksJson, type);
        } else {
            books = BookDataLoader.getAllBooks();
            saveBooks();
        }

        // Load orders
        String ordersJson = prefs.getString(KEY_ORDERS, null);
        if (ordersJson != null) {
            Type type = new TypeToken<List<Order>>(){}.getType();
            orders = gson.fromJson(ordersJson, type);
        } else {
            orders = generateMockOrders();
            saveOrders();
        }

        // Load users
        String usersJson = prefs.getString(KEY_USERS, null);
        if (usersJson != null) {
            Type type = new TypeToken<List<User>>(){}.getType();
            users = gson.fromJson(usersJson, type);
        } else {
            users = generateMockUsers();
            saveUsers();
        }
    }

    // Books CRUD
    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }

    public Book getBookById(int id) {
        for (Book book : books) {
            if (book.id == id) return book;
        }
        return null;
    }

    public void addBook(Book book) {
        book.id = getNextBookId();
        books.add(book);
        saveBooks();
    }

    public void updateBook(Book book) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).id == book.id) {
                books.set(i, book);
                saveBooks();
                return;
            }
        }
    }

    public void deleteBook(int id) {
        books.removeIf(book -> book.id == id);
        saveBooks();
    }

    private int getNextBookId() {
        int maxId = 0;
        for (Book book : books) {
            if (book.id > maxId) maxId = book.id;
        }
        return maxId + 1;
    }

    private void saveBooks() {
        String json = gson.toJson(books);
        prefs.edit().putString(KEY_BOOKS, json).apply();
    }

    // Orders CRUD
    public List<Order> getAllOrders() {
        return new ArrayList<>(orders);
    }

    public List<Order> getOrdersByStatus(String status) {
        List<Order> filtered = new ArrayList<>();
        for (Order order : orders) {
            if (order.getStatus().equals(status)) {
                filtered.add(order);
            }
        }
        return filtered;
    }

    public void updateOrderStatus(String orderId, String newStatus) {
        for (Order order : orders) {
            if (order.getId().equals(orderId)) {
                order.setStatus(newStatus);
                saveOrders();
                return;
            }
        }
    }

    public void addOrder(Order order) {
        orders.add(order);
        saveOrders();
    }

    private void saveOrders() {
        String json = gson.toJson(orders);
        prefs.edit().putString(KEY_ORDERS, json).apply();
    }

    // Users CRUD
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    public User getUserById(int id) {
        for (User user : users) {
            if (user.id == id) return user;
        }
        return null;
    }

    public void updateUser(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).id == user.id) {
                users.set(i, user);
                saveUsers();
                return;
            }
        }
    }

    public void toggleUserBan(int userId) {
        for (User user : users) {
            if (user.id == userId) {
                user.isBanned = !user.isBanned;
                saveUsers();
                return;
            }
        }
    }

    private void saveUsers() {
        String json = gson.toJson(users);
        prefs.edit().putString(KEY_USERS, json).apply();
    }

    // Mock data generators
    private List<Order> generateMockOrders() {
        List<Order> mockOrders = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String today = sdf.format(new Date());

        mockOrders.add(new Order("ORD001", "1", "Nguyễn Văn A", "123 Đường ABC, Q1, HCM", "Pending", 450000, today));
        mockOrders.add(new Order("ORD002", "2", "Trần Thị B", "456 Đường XYZ, Q3, HCM", "Processing", 320000, today));
        mockOrders.add(new Order("ORD003", "3", "Lê Văn C", "789 Đường DEF, Q5, HCM", "Shipped", 680000, today));
        mockOrders.add(new Order("ORD004", "4", "Phạm Thị D", "321 Đường GHI, Q7, HCM", "Delivered", 550000, "23/11/2025"));
        mockOrders.add(new Order("ORD005", "5", "Hoàng Văn E", "654 Đường JKL, Q10, HCM", "Cancelled", 220000, "22/11/2025"));

        return mockOrders;
    }

    private List<User> generateMockUsers() {
        List<User> mockUsers = new ArrayList<>();

        mockUsers.add(new User(1, "Nguyễn Văn A", "nguyenvana@email.com", "customer", false));
        mockUsers.add(new User(2, "Trần Thị B", "tranthib@email.com", "customer", false));
        mockUsers.add(new User(3, "Lê Văn C", "levanc@email.com", "customer", false));
        mockUsers.add(new User(4, "Phạm Thị D", "phamthid@email.com", "customer", true));
        mockUsers.add(new User(5, "Hoàng Văn E", "hoangvane@email.com", "customer", false));

        return mockUsers;
    }
}

