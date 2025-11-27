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

        // Tạo mô tả dài chi tiết hơn
        String longDesc = generateLongDescription(title, author, description, category);
        book.setLongDescription(longDesc);

        // Ensure category is never null or empty
        book.setCategory(category != null && !category.trim().isEmpty() ? category.trim() : "Khác");
        book.setLanguage("Tiếng Việt");
        book.setStock((int)(Math.random() * 100) + 10);
        book.setPages(200 + (int)(Math.random() * 400));

        // Tạo nhiều ảnh cho mỗi cuốn sách (2-4 ảnh)
        String baseUrl = "https://picsum.photos/seed/" + title.replace(" ", "").replace("–", "").replace("á", "a") + id;
        book.setImageUrl(baseUrl + "/300/450");

        // Tạo danh sách 2-4 URLs ảnh
        int numImages = 2 + (int)(Math.random() * 3); // 2-4 ảnh
        StringBuilder imageUrls = new StringBuilder();
        for (int i = 1; i <= numImages; i++) {
            if (i > 1) imageUrls.append(",");
            imageUrls.append(baseUrl).append("v").append(i).append("/300/450");
        }
        book.setImageUrls(imageUrls.toString());

        book.setPublisher("NXB Trẻ");
        book.setPublishYear("2024");
        book.setActive(true);
        return book;
    }

    // Tạo mô tả dài chi tiết hơn
    private static String generateLongDescription(String title, String author, String shortDesc, String category) {
        StringBuilder longDesc = new StringBuilder();

        // Phần giới thiệu
        longDesc.append("📖 GIỚI THIỆU SÁCH\n\n");
        longDesc.append(shortDesc).append("\n\n");

        // Nội dung chi tiết theo từng thể loại
        longDesc.append("📚 NỘI DUNG CHI TIẾT\n\n");

        switch (category) {
            case "Văn học":
                longDesc.append("Tác phẩm \"").append(title).append("\" của tác giả ").append(author)
                    .append(" là một trong những kiệt tác văn học Việt Nam, đã để lại dấu ấn sâu đậm trong lòng độc giả qua nhiều thế hệ. ")
                    .append("Với ngòi bút tinh tế và sắc sảo, tác giả đã khắc họa sinh động những con người, những số phận, ")
                    .append("phản ánh chân thực những mâu thuẫn xã hội và tâm tư tình cảm con người. ")
                    .append("Tác phẩm không chỉ có giá trị nghệ thuật cao mà còn mang ý nghĩa nhân văn sâu sắc, ")
                    .append("giúp người đọc suy ngẫm về cuộc sống, về con người và xã hội.\n\n");
                break;
            case "Kỹ năng":
                longDesc.append("\"").append(title).append("\" của ").append(author)
                    .append(" là cuốn sách self-help được đánh giá cao, giúp người đọc phát triển kỹ năng sống và tư duy tích cực. ")
                    .append("Cuốn sách cung cấp những phương pháp thực tiễn, dễ áp dụng vào cuộc sống hàng ngày. ")
                    .append("Qua từng trang sách, bạn sẽ học được cách quản lý thời gian hiệu quả, ")
                    .append("xây dựng mối quan hệ tốt đẹp, vượt qua khó khăn và đạt được thành công trong sự nghiệp. ")
                    .append("Đây là cuốn sách không thể thiếu cho những ai muốn hoàn thiện bản thân và đạt được mục tiêu cuộc sống.\n\n");
                break;
            case "Thiếu nhi":
                longDesc.append("Cuốn sách \"").append(title).append("\" là một tác phẩm thiếu nhi tuyệt vời, ")
                    .append("mang đến cho các em nhỏ những giây phút giải trí bổ ích và ý nghĩa. ")
                    .append("Với ngôn ngữ giản dị, gần gũi, câu chuyện dễ hiểu và hấp dẫn, ")
                    .append("cuốn sách không chỉ giúp trẻ em phát triển trí tưởng tượng, óc sáng tạo ")
                    .append("mà còn rèn luyện kỹ năng đọc hiểu, tư duy logic. ")
                    .append("Các bài học đạo đức, tình bạn, lòng dũng cảm được khéo léo lồng ghép vào câu chuyện, ")
                    .append("giúp trẻ em học hỏi và trưởng thành qua mỗi trang sách.\n\n");
                break;
            case "Kinh tế":
                longDesc.append("\"").append(title).append("\" là tác phẩm kinh tế - kinh doanh nổi tiếng của ").append(author).append(", ")
                    .append("cung cấp kiến thức chuyên sâu về quản trị, tài chính, marketing và khởi nghiệp. ")
                    .append("Cuốn sách phân tích chi tiết các chiến lược kinh doanh thành công, ")
                    .append("các mô hình kinh doanh hiện đại và xu hướng thị trường toàn cầu. ")
                    .append("Với nhiều case study thực tế từ các doanh nghiệp lớn, ")
                    .append("cuốn sách giúp người đọc hiểu rõ bản chất kinh doanh, ")
                    .append("đưa ra những quyết định đúng đắn và đạt được thành công bền vững.\n\n");
                break;
            case "Tâm lý":
                longDesc.append("Cuốn sách \"").append(title).append("\" của ").append(author)
                    .append(" là một tác phẩm tâm lý học sâu sắc, giúp người đọc hiểu rõ hơn về bản thân và người khác. ")
                    .append("Tác giả phân tích tâm lý con người một cách khoa học nhưng dễ hiểu, ")
                    .append("giúp bạn nhận ra những rào cản tâm lý, vượt qua stress, lo âu ")
                    .append("và xây dựng lối sống tích cực, hạnh phúc. ")
                    .append("Cuốn sách cung cấp nhiều bài tập thực hành, kỹ thuật tự điều chỉnh tâm lý, ")
                    .append("giúp bạn kiểm soát cảm xúc và sống một cuộc đời ý nghĩa hơn.\n\n");
                break;
            case "Lịch sử":
                longDesc.append("\"").append(title).append("\" là một công trình nghiên cứu lịch sử công phu, ")
                    .append("tái hiện sinh động các sự kiện lịch sử quan trọng của dân tộc. ")
                    .append("Với phong cách viết hấp dẫn, dựa trên tư liệu lịch sử chính xác, ")
                    .append("cuốn sách giúp người đọc hiểu sâu sắc về quá khứ, ")
                    .append("về những anh hùng dân tộc, những cuộc chiến tranh bảo vệ Tổ quốc. ")
                    .append("Đây là cuốn sách quý giá cho những ai yêu thích lịch sử, ")
                    .append("muốn tìm hiểu về nguồn gốc, truyền thống và văn hóa Việt Nam.\n\n");
                break;
            case "Khoa học":
                longDesc.append("Tác phẩm \"").append(title).append("\" của ").append(author)
                    .append(" là một cuốn sách khoa học phổ thông xuất sắc, ")
                    .append("giúp người đọc khám phá những bí ẩn của vũ trụ, tự nhiên và con người. ")
                    .append("Với cách trình bày dễ hiểu, sinh động, đi kèm nhiều hình ảnh minh họa, ")
                    .append("cuốn sách làm sáng tỏ những hiện tượng khoa học phức tạp, ")
                    .append("kích thích trí tò mò và niềm đam mê khám phá. ")
                    .append("Đây là tài liệu tham khảo tuyệt vời cho học sinh, sinh viên ")
                    .append("cũng như những người yêu thích khoa học.\n\n");
                break;
            case "Công nghệ":
                longDesc.append("\"").append(title).append("\" là cuốn sách công nghệ thiết thực, ")
                    .append("hướng dẫn chi tiết các kiến thức lập trình, công nghệ thông tin hiện đại. ")
                    .append("Với lối viết rõ ràng, các ví dụ code cụ thể, bài tập thực hành phong phú, ")
                    .append("cuốn sách giúp người đọc nắm vững kiến thức từ cơ bản đến nâng cao. ")
                    .append("Đây là tài liệu học tập không thể thiếu cho lập trình viên, ")
                    .append("sinh viên công nghệ thông tin và những ai đam mê công nghệ.\n\n");
                break;
            default:
                longDesc.append("Cuốn sách \"").append(title).append("\" của ").append(author)
                    .append(" là một tác phẩm đáng đọc, mang lại nhiều kiến thức bổ ích và giá trị cho người đọc. ")
                    .append("Với nội dung phong phú, cách trình bày hấp dẫn, cuốn sách sẽ là người bạn đồng hành tuyệt vời, ")
                    .append("giúp bạn mở rộng tầm hiểu biết và trải nghiệm những điều thú vị mới mẻ.\n\n");
        }

        // Thêm phần đặc điểm nổi bật
        longDesc.append("✨ ĐẶC ĐIỂM NỔI BẬT\n\n");
        longDesc.append("• Nội dung chất lượng, được biên soạn công phu\n");
        longDesc.append("• Ngôn ngữ dễ hiểu, phù hợp với nhiều độc giả\n");
        longDesc.append("• Bìa đẹp, in ấn chất lượng cao\n");
        longDesc.append("• Giấy in đạt chuẩn, bảo vệ thị lực\n");
        longDesc.append("• Giá cả hợp lý, đáng đồng tiền bát gạo\n\n");

        // Thêm thông tin khuyến mại
        longDesc.append("🎁 ƯU ĐÃI ĐẶC BIỆT\n\n");
        longDesc.append("• Giao hàng toàn quốc - Thanh toán khi nhận hàng\n");
        longDesc.append("• Kiểm tra hàng trước khi thanh toán\n");
        longDesc.append("• Đổi trả trong 7 ngày nếu có lỗi từ nhà xuất bản\n");
        longDesc.append("• Tặng kèm bookmark độc quyền (nếu còn)\n");
        longDesc.append("• Giảm giá cho đơn hàng từ 2 cuốn trở lên\n\n");

        longDesc.append("Hãy đặt mua ngay hôm nay để không bỏ lỡ cơ hội sở hữu cuốn sách tuyệt vời này!");

        return longDesc.toString();
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
