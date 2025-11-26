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

    // Demo books data - BRAND NEW with Vietnamese titles and clear categories
    private static List<Book> getDemoBooks() {
        List<Book> books = new ArrayList<>();

        // === DANH MỤC: Văn học (10 sách) ===
        books.add(createBook(1, "Số Đỏ", "Vũ Trọng Phụng", 85000,
            "Tác phẩm nổi tiếng về cuộc sống Hà Nội xưa", "Văn học"));
        books.add(createBook(2, "Truyện Kiều", "Nguyễn Du", 120000,
            "Kiệt tác văn học cổ điển Việt Nam", "Văn học"));
        books.add(createBook(3, "Chí Phèo", "Nam Cao", 65000,
            "Chân dung người nông dân nghèo khổ", "Văn học"));
        books.add(createBook(4, "Tắt Đèn", "Ngô Tất Tố", 75000,
            "Bi kịch xã hội nông thôn Việt Nam", "Văn học"));
        books.add(createBook(5, "Vợ Nhặt", "Kim Lân", 55000,
            "Chuyện tình yêu thương giữa hoàn cảnh khó khăn", "Văn học"));
        books.add(createBook(6, "Lão Hạc", "Nam Cao", 60000,
            "Số phận người nghèo trong xã hội cũ", "Văn học"));
        books.add(createBook(7, "Chiếc Lược Ngà", "Nguyễn Quang Sáng", 70000,
            "Chuyện tình cảm động về tình cha con", "Văn học"));
        books.add(createBook(8, "Rừng Xà Nu", "Nguyễn Trung Thành", 95000,
            "Cuộc sống vùng rừng núi phía Bắc", "Văn học"));
        books.add(createBook(9, "Những Ngôi Sao Xa Xôi", "Lê Minh Khuê", 88000,
            "Chuyện chiến tranh và hòa bình", "Văn học"));
        books.add(createBook(10, "Vang Bóng Một Thời", "Nguyễn Tuân", 78000,
            "Hồi ức về Hà Nội xưa", "Văn học"));

        // === DANH MỤC: Kỹ năng (8 sách) ===
        books.add(createBook(11, "Đắc Nhân Tâm", "Dale Carnegie", 89000,
            "Nghệ thuật giao tiếp và ứng xử", "Kỹ năng"));
        books.add(createBook(12, "Quẳng Gánh Lo Đi Và Vui Sống", "Dale Carnegie", 95000,
            "Cách vượt qua lo lắng trong cuộc sống", "Kỹ năng"));
        books.add(createBook(13, "Nghĩ Giàu Và Làm Giàu", "Napoleon Hill", 120000,
            "Bí quyết thành công từ suy nghĩ tích cực", "Kỹ năng"));
        books.add(createBook(14, "Không Diệt Không Sinh Đừng Sợ Hãi", "Thích Nhất Hạnh", 105000,
            "Sống an lạc trong hiện tại", "Kỹ năng"));
        books.add(createBook(15, "Đời Ngắn Đừng Ngủ Dài", "Robin Sharma", 98000,
            "Tối ưu hóa thời gian và năng suất", "Kỹ năng"));
        books.add(createBook(16, "Tuổi Trẻ Đáng Giá Bao Nhiêu", "Rosie Nguyễn", 85000,
            "Truyền cảm hứng cho giới trẻ", "Kỹ năng"));
        books.add(createBook(17, "Bạn Đắt Giá Bao Nhiêu", "Vãn Tình", 92000,
            "Nâng cao giá trị bản thân", "Kỹ năng"));
        books.add(createBook(18, "Cà Phê Cùng Tony", "Tony Buổi Sáng", 88000,
            "Bài học kinh doanh và cuộc sống", "Kỹ năng"));

        // === DANH MỤC: Thiếu nhi (8 sách) ===
        books.add(createBook(19, "Dế Mèn Phiêu Lưu Ký", "Tô Hoài", 45000,
            "Cuộc phiêu lưu của chú dế mèn", "Thiếu nhi"));
        books.add(createBook(20, "Tôi Thấy Hoa Vàng Trên Cỏ Xanh", "Nguyễn Nhật Ánh", 79000,
            "Tuổi thơ dữ dội ở nông thôn", "Thiếu nhi"));
        books.add(createBook(21, "Mắt Biếc", "Nguyễn Nhật Ánh", 85000,
            "Chuyện tình đầu ngọt ngào", "Thiếu nhi"));
        books.add(createBook(22, "Cho Tôi Xin Một Vé Đi Tuổi Thơ", "Nguyễn Nhật Ánh", 82000,
            "Hồi ức tuổi thơ đẹp đẽ", "Thiếu nhi"));
        books.add(createBook(23, "Cô Bé Quàng Khăn Đỏ", "Grimm", 35000,
            "Truyện cổ tích kinh điển", "Thiếu nhi"));
        books.add(createBook(24, "Nàng Bạch Tuyết", "Grimm", 35000,
            "Câu chuyện về công chúa và bảy chú lùn", "Thiếu nhi"));
        books.add(createBook(25, "Cậu Bé Rừng Xanh", "Nhiều Tác Giả", 42000,
            "Truyện phiêu lưu cho trẻ em", "Thiếu nhi"));
        books.add(createBook(26, "Những Cuộc Phiêu Lưu Của Tom Sawyer", "Mark Twain", 68000,
            "Hành trình mạo hiểm của cậu bé Tom", "Thiếu nhi"));

        // === DANH MỤC: Kinh tế (7 sách) ===
        books.add(createBook(27, "Dạy Con Làm Giàu", "Robert Kiyosaki", 125000,
            "Tư duy tài chính cho người Việt", "Kinh tế"));
        books.add(createBook(28, "Nhà Đầu Tư Thông Minh", "Benjamin Graham", 150000,
            "Bí quyết đầu tư chứng khoán", "Kinh tế"));
        books.add(createBook(29, "Tư Duy Nhanh Và Chậm", "Daniel Kahneman", 135000,
            "Tâm lý học trong quyết định kinh tế", "Kinh tế"));
        books.add(createBook(30, "Nghệ Thuật Bán Hàng", "Brian Tracy", 98000,
            "Kỹ năng bán hàng chuyên nghiệp", "Kinh tế"));
        books.add(createBook(31, "Khởi Nghiệp Bán Lẻ", "Trần Thanh Phong", 110000,
            "Hướng dẫn mở cửa hàng kinh doanh", "Kinh tế"));
        books.add(createBook(32, "Marketing 4.0", "Philip Kotler", 145000,
            "Chiến lược marketing thời đại số", "Kinh tế"));
        books.add(createBook(33, "Chiến Lược Đại Dương Xanh", "W. Chan Kim", 155000,
            "Tạo không gian thị trường mới", "Kinh tế"));

        // === DANH MỤC: Tâm lý (6 sách) ===
        books.add(createBook(34, "Hiểu Về Trái Tim", "Minh Niệm", 92000,
            "Hành trình tìm hiểu bản thân", "Tâm lý"));
        books.add(createBook(35, "Đời Ngắn Đừng Ngủ Dài", "Robin Sharma", 98000,
            "Sống trọn vẹn từng ngày", "Tâm lý"));
        books.add(createBook(36, "Nghĩ Đơn Giản Sống Đơn Giản", "Tolly Burkan", 85000,
            "Hạnh phúc từ sự giản đơn", "Tâm lý"));
        books.add(createBook(37, "Tâm Lý Học Tội Phạm", "Diệp Hồng Vũ", 115000,
            "Phân tích hành vi tội phạm", "Tâm lý"));
        books.add(createBook(38, "Người Khôn Ngoan Có Trái Tim", "Erich Fromm", 105000,
            "Nghệ thuật yêu thương", "Tâm lý"));
        books.add(createBook(39, "Tuổi 20 Đừng Mơ Mộng", "Nhiều Tác Giả", 88000,
            "Định hướng cho giới trẻ", "Tâm lý"));

        // === DANH MỤC: Lịch sử (5 sách) ===
        books.add(createBook(40, "Lịch Sử Việt Nam", "Nhiều Tác Giả", 180000,
            "Bộ sử Việt Nam đầy đủ", "Lịch sử"));
        books.add(createBook(41, "Việt Nam Sử Lược", "Trần Trọng Kim", 120000,
            "Tóm tắt lịch sử dân tộc", "Lịch sử"));
        books.add(createBook(42, "Đại Việt Sử Ký Toàn Thư", "Ngô Sĩ Liên", 250000,
            "Biên niên sử triều đại", "Lịch sử"));
        books.add(createBook(43, "Bác Hồ Với Thanh Niên", "Nhiều Tác Giả", 75000,
            "Lời dạy của Bác về thế hệ trẻ", "Lịch sử"));
        books.add(createBook(44, "Chiến Thắng Điện Biên Phủ", "Võ Nguyên Giáp", 135000,
            "Hồi ức về chiến dịch lịch sử", "Lịch sử"));

        // === DANH MỤC: Khoa học (5 sách) ===
        books.add(createBook(45, "Vũ Trụ Trong Vỏ Hạt Dẻ", "Stephen Hawking", 145000,
            "Khám phá vũ trụ và thời gian", "Khoa học"));
        books.add(createBook(46, "Lược Sử Thời Gian", "Stephen Hawking", 155000,
            "Từ Big Bang đến hố đen", "Khoa học"));
        books.add(createBook(47, "Sapiens", "Yuval Noah Harari", 168000,
            "Lịch sử loài người", "Khoa học"));
        books.add(createBook(48, "Trí Tuệ Nhân Tạo", "Kai-Fu Lee", 135000,
            "Tương lai của AI", "Khoa học"));
        books.add(createBook(49, "Sinh Vật Kỳ Diệu", "David Attenborough", 125000,
            "Thế giới động vật hoang dã", "Khoa học"));

        // === DANH MỤC: Công nghệ (4 sách) ===
        books.add(createBook(50, "Lập Trình Java Cơ Bản", "Phạm Hữu Khang", 98000,
            "Học Java từ con số 0", "Công nghệ"));
        books.add(createBook(51, "Python Cho Người Mới", "Đỗ Minh Tuấn", 89000,
            "Học Python dễ dàng", "Công nghệ"));
        books.add(createBook(52, "Blockchain Và Tiền Điện Tử", "Andreas M.", 145000,
            "Công nghệ blockchain explained", "Công nghệ"));
        books.add(createBook(53, "Machine Learning", "Andrew Ng", 185000,
            "Học máy và ứng dụng", "Công nghệ"));

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
