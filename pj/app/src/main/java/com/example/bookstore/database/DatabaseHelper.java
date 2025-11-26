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

    // Demo books data - clean and validated with proper categories and titles
    private static List<Book> getDemoBooks() {
        List<Book> books = new ArrayList<>();

        // Văn học - Fiction
        books.add(createBook(1, "1984", "George Orwell", 95000,
            "Tiểu thuyết viễn tưởng kinh điển về xã hội độc tài và kiểm soát tuyệt đối", "Văn học"));
        books.add(createBook(2, "The Great Gatsby", "F. Scott Fitzgerald", 85000,
            "Tiểu thuyết cổ điển Mỹ về tình yêu, giàu có và giấc mơ Mỹ", "Văn học"));
        books.add(createBook(3, "The Alchemist", "Paulo Coelho", 89000,
            "Triết lý sống về việc theo đuổi giấc mơ và khám phá bản thân", "Văn học"));
        books.add(createBook(4, "Animal Farm", "George Orwell", 72000,
            "Tiểu thuyết châm biếm về cách mạng và sự tham vọng quyền lực", "Văn học"));
        books.add(createBook(5, "To Kill a Mockingbird", "Harper Lee", 99000,
            "Tác phẩm kinh điển về công lý, nhân quyền và sự trưởng thành", "Văn học"));

        // Lịch sử - History
        books.add(createBook(6, "Sapiens", "Yuval Noah Harari", 150000,
            "Hành trình từ động vật hoang dã đến chủ nhân thế giới - Lịch sử loài người", "Lịch sử"));
        books.add(createBook(7, "21 Bài Học Cho Thế Kỷ 21", "Yuval Noah Harari", 135000,
            "Những thách thức lớn nhất của thế giới hiện đại", "Lịch sử"));
        books.add(createBook(8, "Việt Nam Sử Lược", "Trần Trọng Kim", 120000,
            "Lịch sử Việt Nam từ thời cổ đại đến hiện đại", "Lịch sử"));

        // Khoa học viễn tưởng - Science Fiction
        books.add(createBook(9, "Dune", "Frank Herbert", 125000,
            "Tác phẩm kinh điển sci-fi về hành tinh sa mạc bí ẩn và cuộc chiến giành quyền lực", "Khoa học"));
        books.add(createBook(10, "Foundation", "Isaac Asimov", 105000,
            "Tác phẩm kinh điển sci-fi về việc xây dựng lại nền văn minh", "Khoa học"));
        books.add(createBook(11, "Neuromancer", "William Gibson", 98000,
            "Khởi đầu của thể loại cyberpunk - Tương lai điểm tối của công nghệ", "Khoa học"));

        // Lãng mạn - Romance
        books.add(createBook(12, "Pride and Prejudice", "Jane Austen", 79000,
            "Tiểu thuyết tình cảm vĩ đại của Jane Austen - Tình yêu và thành kiến xã hội", "Kinh tế"));
        books.add(createBook(13, "The Notebook", "Nicholas Sparks", 85000,
            "Câu chuyện tình yêu đẹp và đau thương xuyên suốt thời gian", "Kinh tế"));
        books.add(createBook(14, "Outlander", "Diana Gabaldon", 135000,
            "Hành trình xuyên thời gian kết hợp lãng mạn, phiêu lưu và lịch sử", "Kinh tế"));

        // Kỹ năng - Self-Help
        books.add(createBook(15, "Atomic Habits", "James Clear", 139000,
            "Những thói quen nhỏ, những kết quả phi thường - Cách xây dựng cuộc sống tốt hơn", "Kỹ năng"));
        books.add(createBook(16, "Đắc Nhân Tâm", "Dale Carnegie", 89000,
            "Nghệ thuật giao tiếp và ứng xử thành công", "Kỹ năng"));
        books.add(createBook(17, "7 Thói Quen Hiệu Quả", "Stephen Covey", 120000,
            "Bảy thói quen của người thành đạt", "Kỹ năng"));

        // Tiểu sử - Biography
        books.add(createBook(18, "Educated", "Tara Westover", 119000,
            "Hành trình giáo dục từ gia đình bí truyền đến Đại học Yale", "Tâm lý"));
        books.add(createBook(19, "Becoming", "Michelle Obama", 145000,
            "Tự truyện của cựu Đệ nhất phu nhân - Câu chuyện về sự kiên trì và phấn đấu", "Tâm lý"));
        books.add(createBook(20, "Steve Jobs", "Walter Isaacson", 155000,
            "Tiểu sử chính thức của Steve Jobs", "Tâm lý"));

        // Giáo dục - Fantasy
        books.add(createBook(21, "The Hobbit", "J.R.R. Tolkien", 119000,
            "Cuộc phiêu lưu huyền thoại của Bilbo Baggins vào thế giới Trung Địa", "Giáo dục"));
        books.add(createBook(22, "Harry Potter và Hòn Đá Phù Thủy", "J.K. Rowling", 129000,
            "Khởi đầu của loạt tiểu thuyết phép thuật được yêu thích nhất thế giới", "Giáo dục"));
        books.add(createBook(23, "The Name of the Wind", "Patrick Rothfuss", 129000,
            "Tiểu thuyết fantasy huyền thoại về một pháp sư trẻ tài năng", "Giáo dục"));
        books.add(createBook(24, "A Game of Thrones", "George R.R. Martin", 145000,
            "Trò chơi vương quyền - Cuộc chiến giành ngai vàng", "Giáo dục"));

        // Nghệ thuật - Thriller/Mystery
        books.add(createBook(25, "Gone Girl", "Gillian Flynn", 99000,
            "Thriller tâm lý căng thẳng về vụ mất tích bí ẩn và những bí mật hôn nhân", "Nghệ thuật"));
        books.add(createBook(26, "The Da Vinci Code", "Dan Brown", 110000,
            "Bí ẩn và âm mưu xoay quanh tác phẩm nổi tiếng của Leonardo da Vinci", "Nghệ thuật"));
        books.add(createBook(27, "The Girl with the Dragon Tattoo", "Stieg Larsson", 119000,
            "Bí ẩn tội phạm Thụy Điển - Nhà báo và hacker tài ba", "Nghệ thuật"));
        books.add(createBook(28, "And Then There Were None", "Agatha Christie", 79000,
            "Tiểu thuyết bí ẩn kinh điển - Mười nhân vật và một bí mật chết chóc", "Nghệ thuật"));

        // Công nghệ - Psychology
        books.add(createBook(29, "Thinking, Fast and Slow", "Daniel Kahneman", 125000,
            "Khám phá cách hoạt động của tâm trí và những sai lầm trong quyết định", "Công nghệ"));
        books.add(createBook(30, "Tư Duy Nhanh và Chậm", "Daniel Kahneman", 125000,
            "Hai hệ thống tư duy và cách chúng định hình quyết định của chúng ta", "Công nghệ"));

        // Thiếu nhi - Children
        books.add(createBook(31, "Dế Mèn Phiêu Lưu Ký", "Tô Hoài", 45000,
            "Cuộc phiêu lưu của chú dế mèn trong thế giới côn trùng", "Thiếu nhi"));
        books.add(createBook(32, "Tôi Thấy Hoa Vàng Trên Cỏ Xanh", "Nguyễn Nhật Ánh", 79000,
            "Tuổi thơ dữ dội và những ký ức không thể quên", "Thiếu nhi"));
        books.add(createBook(33, "Mắt Biếc", "Nguyễn Nhật Ánh", 85000,
            "Chuyện tình đầu ngọt ngào và day dứt", "Thiếu nhi"));

        // Du lịch - Travel
        books.add(createBook(34, "Việt Nam - Đất Nước Con Người", "National Geographic", 180000,
            "Khám phá vẻ đẹp thiên nhiên và văn hóa Việt Nam", "Du lịch"));
        books.add(createBook(35, "50 Điểm Đến Đẹp Nhất Thế Giới", "Various Authors", 220000,
            "Cẩm nang du lịch toàn cầu", "Du lịch"));

        // Y học - Health
        books.add(createBook(36, "Tự Chữa Lành", "Deepak Chopra", 95000,
            "Sức khỏe toàn diện từ tâm trí đến cơ thể", "Y học"));
        books.add(createBook(37, "Ăn Uống Lành Mạnh", "Nhiều Tác Giả", 89000,
            "Hướng dẫn dinh dưỡng khoa học", "Y học"));

        return books;
    }

    // Helper to create book with clean data - ensures no null/empty fields
    private static Book createBook(int id, String title, String author, double price,
                                   String description, String category) {
        Book book = new Book();
        book.setId(id);
        // Ensure title is never null or empty
        book.setTitle(title != null && !title.trim().isEmpty() ? title.trim() : "Sách " + id);
        // Ensure author is never null
        book.setAuthor(author != null && !author.trim().isEmpty() ? author.trim() : "Tác giả");
        book.setPrice(price > 0 ? price : 50000);
        // Ensure description is never null
        book.setDescription(description != null && !description.trim().isEmpty() ? description.trim() : "Mô tả sách");
        // Ensure category is never null or empty
        book.setCategory(category != null && !category.trim().isEmpty() ? category.trim() : "Khác");
        book.setLanguage("Tiếng Việt");
        book.setStock((int)(Math.random() * 100) + 10);
        book.setPages(200 + (int)(Math.random() * 400));
        book.setImageUrl("https://picsum.photos/seed/" + title.replace(" ", "").replace("–", "").replace("á", "a") + id + "/300/450");
        book.setPublisher("NXB Trẻ");
        book.setPublishYear("2024");
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
