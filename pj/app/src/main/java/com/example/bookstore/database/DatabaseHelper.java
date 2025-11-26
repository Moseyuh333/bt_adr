package com.example.bookstore.database;

import android.content.Context;
import android.util.Log;

import com.example.bookstore.database.entities.Book;
import com.example.bookstore.database.entities.User;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseHelper {
    private static final String TAG = "DatabaseHelper";
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static void initializeDatabase(Context context, OnInitializedListener listener) {
        executorService.execute(() -> {
            try {
                AppDatabase db = AppDatabase.getInstance(context);

                int userCount = db.userDao().getAllUsers().size();
                // Nếu đã init user trước đó, vẫn đảm bảo có sách
                int bookCount = db.bookDao().getAllBooks().size();
                if (bookCount == 0) {
                    importBooksFromCSV(context, db);
                }
                if (userCount > 0) {
                    if (listener != null) listener.onInitialized(true);
                    return;
                }

                // Create admin user
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(hashPassword("admin"));
                admin.setFullName("Administrator");
                admin.setEmail("admin@bookstore.com");
                admin.setPhone("0123456789");
                admin.setAdmin(true);
                db.userDao().insert(admin);

                // Create 100 demo users
                List<Long> userIds = new ArrayList<>();
                for (int i = 1; i <= 100; i++) {
                    User demoUser = new User();
                    demoUser.setUsername("demo" + i);
                    demoUser.setPassword(hashPassword("demo" + i));
                    demoUser.setFullName("Khách hàng " + i);
                    demoUser.setEmail("demo" + i + "@bookstore.com");
                    demoUser.setPhone(String.format("09%08d", 10000000 + i));
                    demoUser.setAdmin(false);
                    long userId = db.userDao().insert(demoUser);
                    userIds.add(userId);
                }

                // Import books from CSV
                importBooksFromCSV(context, db);

                // Create sample orders
                createSampleOrders(context, db, userIds);

                Log.d(TAG, "Database initialized successfully");
                if (listener != null) {
                    listener.onInitialized(true);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error initializing database", e);
                if (listener != null) {
                    listener.onInitialized(false);
                }
            }
        });
    }

    private static void importBooksFromCSV(Context context, AppDatabase db) {
        try {
            InputStream is = context.getAssets().open("books_full_9xx.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            boolean isFirstLine = true;
            int count = 0;

            while ((line = reader.readLine()) != null && count < 100) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                Book book = parseCSVLine(line);
                if (book != null) {
                    db.bookDao().insert(book);
                    count++;
                }
            }

            reader.close();
            Log.d(TAG, "Imported " + count + " books");
        } catch (Exception e) {
            Log.w(TAG, "Error importing books", e);
        }
    }

    private static Book parseCSVLine(String line) {
        try {
            String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
            if (parts.length < 5) return null;

            Book book = new Book();
            book.setTitle(cleanValue(parts[0]));
            book.setAuthor(parts.length > 1 ? cleanValue(parts[1]) : "Unknown");
            book.setPublisher(parts.length > 2 ? cleanValue(parts[2]) : "");
            book.setPublishYear(parts.length > 3 ? cleanValue(parts[3]) : "");
            book.setCategory(parts.length > 4 ? cleanValue(parts[4]) : "General");
            book.setLanguage(parts.length > 5 ? cleanValue(parts[5]) : "Vietnamese");
            book.setDescription(parts.length > 6 ? cleanValue(parts[6]) : "");
            book.setPrice(50000 + (Math.random() * 450000));
            book.setStock((int)(Math.random() * 100) + 10);
            book.setImageUrl("https://via.placeholder.com/150x200?text=Book");
            book.setPages(parts.length > 7 ? parseIntSafe(cleanValue(parts[7])) : 200);
            book.setActive(true);

            return book;
        } catch (Exception e) {
            return null;
        }
    }

    private static void createSampleOrders(Context context, AppDatabase db, List<Long> userIds) {
        try {
            List<com.example.bookstore.database.entities.Book> books = db.bookDao().getAllActiveBooks();
            if (books.isEmpty()) return;

            String[] statuses = {"PENDING", "CONFIRMED", "SHIPPING", "DELIVERED", "CANCELLED"};
            String[] addresses = {
                "123 Nguyễn Huệ, Q1, TP.HCM",
                "456 Lê Lợi, Q1, TP.HCM",
                "789 Trần Hưng Đạo, Q5, TP.HCM",
                "321 Võ Văn Tần, Q3, TP.HCM",
                "654 Pasteur, Q1, TP.HCM"
            };

            for (int i = 0; i < 150; i++) {
                Long userId = userIds.get((int)(Math.random() * userIds.size()));

                com.example.bookstore.database.entities.Order order = new com.example.bookstore.database.entities.Order();
                order.setUserId(userId.intValue());
                order.setStatus(statuses[(int)(Math.random() * statuses.length)]);
                order.setShippingAddress(addresses[(int)(Math.random() * addresses.length)]);
                order.setRecipientName("Khách hàng " + userId);
                order.setRecipientPhone(String.format("09%08d", 10000000 + userId));
                order.setPaymentMethod(Math.random() > 0.5 ? "COD" : "Banking");

                long daysAgo = (long)(Math.random() * 60);
                order.setCreatedAt(System.currentTimeMillis() - (daysAgo * 24 * 60 * 60 * 1000));
                order.setUpdatedAt(order.getCreatedAt());

                int numItems = 1 + (int)(Math.random() * 5);
                double totalAmount = 0;

                long orderId = db.orderDao().insert(order);

                for (int j = 0; j < numItems; j++) {
                    com.example.bookstore.database.entities.Book book = books.get((int)(Math.random() * books.size()));
                    int quantity = 1 + (int)(Math.random() * 3);

                    com.example.bookstore.database.entities.OrderItem item = new com.example.bookstore.database.entities.OrderItem();
                    item.setOrderId((int)orderId);
                    item.setBookId(book.getId());
                    item.setBookTitle(book.getTitle());
                    item.setBookAuthor(book.getAuthor());
                    item.setBookImageUrl(book.getImageUrl());
                    item.setPrice(book.getPrice());
                    item.setQuantity(quantity);
                    item.setSubtotal(book.getPrice() * quantity);

                    totalAmount += item.getSubtotal();
                    db.orderItemDao().insert(item);
                }

                order.setId((int)orderId);
                order.setTotalAmount(totalAmount);
                db.orderDao().update(order);
            }

            Log.d(TAG, "Created 150 sample orders");
        } catch (Exception e) {
            Log.e(TAG, "Error creating orders", e);
        }
    }

    private static String cleanValue(String value) {
        if (value == null) return "";
        value = value.trim();
        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1);
        }
        return value;
    }

    private static int parseIntSafe(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }

    private static String hashPassword(String password) {
        return String.valueOf(password.hashCode());
    }

    public interface OnInitializedListener {
        void onInitialized(boolean success);
    }
}
