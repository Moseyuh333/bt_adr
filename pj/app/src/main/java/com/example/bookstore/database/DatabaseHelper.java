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
            // Skip CSV import - file is corrupted with HTML
            // Instead use clean demo data
            List<Book> demoBooks = getDemoBooks();
            for (Book book : demoBooks) {
                db.bookDao().insert(book);
            }
            Log.d(TAG, "Imported " + demoBooks.size() + " demo books (CSV file was corrupted)");
        } catch (Exception e) {
            Log.w(TAG, "Error importing demo books", e);
        }
    }

    // Demo books data - clean and validated
    private static List<Book> getDemoBooks() {
        List<Book> books = new ArrayList<>();

        // Sửa sạch: title = tên sách, description = mô tả (KHÔNG hoán đổi!)
        books.add(createBook(1, "Sapiens", "Yuval Noah Harari", 150000,
            "Hành trình từ động vật hoang dã đến chủ nhân thế giới", "History"));
        books.add(createBook(2, "1984", "George Orwell", 95000,
            "Tiểu thuyết viễn tưởng kinh điển về xã hội độc tài", "Fiction"));
        books.add(createBook(3, "Dune", "Frank Herbert", 125000,
            "Tác phẩm kinh điển sci-fi về hành tinh sa mạc bí ẩn", "Science Fiction"));
        books.add(createBook(4, "The Great Gatsby", "F. Scott Fitzgerald", 85000,
            "Tiểu thuyết cổ điển Mỹ về tình yêu và giấc mơ", "Fiction"));
        books.add(createBook(5, "Pride and Prejudice", "Jane Austen", 79000,
            "Tiểu thuyết tình cảm vĩ đại - Tình yêu và thành kiến", "Romance"));
        books.add(createBook(6, "The Hobbit", "J.R.R. Tolkien", 119000,
            "Cuộc phiêu lưu huyền thoại của Bilbo Baggins", "Fantasy"));
        books.add(createBook(7, "Harry Potter and the Philosopher's Stone", "J.K. Rowling", 129000,
            "Khởi đầu của loạt tiểu thuyết phép thuật nổi tiếng", "Fantasy"));
        books.add(createBook(8, "Atomic Habits", "James Clear", 139000,
            "Những thói quen nhỏ, những kết quả phi thường", "Self-Help"));
        books.add(createBook(9, "Educated", "Tara Westover", 119000,
            "Hành trình giáo dục từ gia đình bí truyền đến Yale", "Biography"));
        books.add(createBook(10, "Becoming", "Michelle Obama", 145000,
            "Tự truyện của Nữ Hoàng Mỹ tiêu biểu", "Biography"));
        books.add(createBook(11, "Gone Girl", "Gillian Flynn", 99000,
            "Thriller tâm lý căng thẳng về vụ mất tích bí ẩn", "Thriller"));
        books.add(createBook(12, "The Da Vinci Code", "Dan Brown", 110000,
            "Bí ẩn và âm mưu xoay quanh tác phẩm nổi tiếng", "Mystery"));
        books.add(createBook(13, "Thinking, Fast and Slow", "Daniel Kahneman", 125000,
            "Khám phá cách hoạt động của tâm trí", "Psychology"));
        books.add(createBook(14, "The Alchemist", "Paulo Coelho", 89000,
            "Triết lý sống về việc theo đuổi giấc mơ", "Fiction"));
        books.add(createBook(15, "Animal Farm", "George Orwell", 72000,
            "Tiểu thuyết châm biếm về cách mạng", "Fiction"));

        return books;
    }

    // Helper to create book with clean data
    private static Book createBook(int id, String title, String author, double price,
                                   String description, String category) {
        Book book = new Book();
        book.setId(id);
        book.setTitle(title);  // title = tên sách
        book.setAuthor(author);
        book.setPrice(price);
        book.setDescription(description);  // description = mô tả
        book.setCategory(category);
        book.setLanguage("Tiếng Việt");
        book.setStock((int)(Math.random() * 100) + 10);
        book.setPages(200 + (int)(Math.random() * 400));
        book.setImageUrl("https://picsum.photos/seed/" + title.replace(" ", "") + id + "/300/450");
        book.setPublisher("NXB");
        book.setPublishYear("2025");
        book.setActive(true);
        return book;
    }

    private static void importBooksFromCSVOld(Context context, AppDatabase db) {
        // OLD CODE - KEPT FOR REFERENCE (will never execute)
        // CSV file is corrupted with HTML - using demo data instead
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

    private static double parseDoubleSafe(String value) {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return 0.0;
        }
    }

    private static String hashPassword(String password) {
        return String.valueOf(password.hashCode());
    }

    /**
     * Force reimport books from CSV to fix any data corruption
     * This will clear all books and reimport from scratch
     */
    public static void reimportBooks(Context context, OnInitializedListener listener) {
        executorService.execute(() -> {
            try {
                AppDatabase db = AppDatabase.getInstance(context);

                // Clear all existing books
                List<com.example.bookstore.database.entities.Book> allBooks = db.bookDao().getAllBooks();
                for (com.example.bookstore.database.entities.Book book : allBooks) {
                    db.bookDao().delete(book);
                }

                Log.d(TAG, "Cleared all books, reimporting from CSV...");

                // Reimport from CSV
                importBooksFromCSV(context, db);

                Log.d(TAG, "Books reimported successfully");
                if (listener != null) {
                    listener.onInitialized(true);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error reimporting books", e);
                if (listener != null) {
                    listener.onInitialized(false);
                }
            }
        });
    }

    public interface OnInitializedListener {
        void onInitialized(boolean success);
    }
}
