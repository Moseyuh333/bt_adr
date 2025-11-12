package com.example.bookstore.utils;

import com.example.bookstore.models.Book;
import java.util.ArrayList;
import java.util.List;

public class BookDataLoader {
    /**
     * Tải danh sách sách từ CSV data (hardcoded từ books_full_9xx.csv)
     * Mỗi sách có đầy đủ thông tin: ID, Tiêu đề, Tác giả, Giá, Mô tả, Hình ảnh, Rating, Reviews, Danh mục
     */
    public static List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();

        // Format: new Book(id, title, author, price, description, imageUrl, rating, reviews, category, inStock)

        // Sách Tiếng Việt & Quốc tế
        books.add(new Book(1, "Những Người Khôn Ngoan", "Nhân Phạm", 89000,
            "Cuốn sách khám phá những bí mật của những người thành công trong cuộc sống và kinh doanh",
            "https://picsum.photos/seed/book1/200/300", 4.8, 1250, "Self-Help", true));

        books.add(new Book(2, "Sapiens", "Yuval Noah Harari", 150000,
            "Hành trình từ động vật hoang dã đến chủ nhân thế giới - Lịch sử loài người từ góc nhìn mới",
            "https://picsum.photos/seed/book2/200/300", 4.9, 2150, "History", true));

        books.add(new Book(3, "1984", "George Orwell", 95000,
            "Tiểu thuyết viễn tưởng kinh điển về xã hội độc tài và kiểm soát tuyệt đối",
            "https://picsum.photos/seed/book3/200/300", 4.8, 1890, "Fiction", true));

        books.add(new Book(4, "Dune", "Frank Herbert", 125000,
            "Tác phẩm kinh điển sci-fi về hành tinh sa mạc bí ẩn và cuộc chiến giành quyền lực",
            "https://picsum.photos/seed/book4/200/300", 4.7, 1670, "Science Fiction", true));

        books.add(new Book(5, "The Great Gatsby", "F. Scott Fitzgerald", 85000,
            "Tiểu thuyết cổ điển Mỹ về tình yêu, giàu có và giấc mơ Mỹ",
            "https://picsum.photos/seed/book5/200/300", 4.6, 980, "Fiction", true));

        books.add(new Book(6, "Pride and Prejudice", "Jane Austen", 79000,
            "Tiểu thuyết tình cảm vĩ đại của Jane Austen - Tình yêu và thành kiến xã hội",
            "https://picsum.photos/seed/book6/200/300", 4.7, 1340, "Romance", true));

        books.add(new Book(7, "To Kill a Mockingbird", "Harper Lee", 99000,
            "Tác phẩm kinh điển về công lý, nhân quyền và sự trưởng thành ở miền Nam nước Mỹ",
            "https://picsum.photos/seed/book7/200/300", 4.8, 1450, "Fiction", true));

        books.add(new Book(8, "The Hobbit", "J.R.R. Tolkien", 119000,
            "Cuộc phiêu lưu huyền thoại của Bilbo Baggins vào thế giới Trung Địa",
            "https://picsum.photos/seed/book8/200/300", 4.9, 2300, "Fantasy", true));

        books.add(new Book(9, "Harry Potter and the Philosopher's Stone", "J.K. Rowling", 129000,
            "Khởi đầu của loạt tiểu thuyết phép thuật được yêu thích nhất thế giới",
            "https://picsum.photos/seed/book9/200/300", 4.9, 4500, "Fantasy", true));

        books.add(new Book(10, "Atomic Habits", "James Clear", 139000,
            "Những thói quen nhỏ, những kết quả phi thường - Cách xây dựng cuộc sống tốt hơn",
            "https://picsum.photos/seed/book10/200/300", 4.9, 3200, "Self-Help", true));

        books.add(new Book(11, "Educated", "Tara Westover", 119000,
            "Hành trình giáo dục từ gia đình bí truyền đến Đại học Yale",
            "https://picsum.photos/seed/book11/200/300", 4.8, 2100, "Biography", true));

        books.add(new Book(12, "Becoming", "Michelle Obama", 145000,
            "Tự truyện của Nữ Hoàng Mỹ tiêu biểu - Câu chuyện về sự kiên trì và phấn đấu",
            "https://picsum.photos/seed/book12/200/300", 4.8, 1900, "Biography", true));

        books.add(new Book(13, "Gone Girl", "Gillian Flynn", 99000,
            "Thriller tâm lý căng thẳng về vụ mất tích bí ẩn và những bí mật hôn nhân",
            "https://picsum.photos/seed/book13/200/300", 4.6, 2400, "Thriller", true));

        books.add(new Book(14, "The Da Vinci Code", "Dan Brown", 110000,
            "Bí ẩn và âm mưu xoay quanh tác phẩm nổi tiếng của Leonardo da Vinci",
            "https://picsum.photos/seed/book14/200/300", 4.5, 2800, "Mystery", true));

        books.add(new Book(15, "Thinking, Fast and Slow", "Daniel Kahneman", 125000,
            "Khám phá cách hoạt động của tâm trí và những sai lầm trong quyết định",
            "https://picsum.photos/seed/book15/200/300", 4.7, 1700, "Psychology", true));

        books.add(new Book(16, "The Alchemist", "Paulo Coelho", 89000,
            "Triết lý sống về việc theo đuổi giấc mơ và khám phá bản thân",
            "https://picsum.photos/seed/book16/200/300", 4.6, 2000, "Fiction", true));

        books.add(new Book(17, "Animal Farm", "George Orwell", 72000,
            "Tiểu thuyết châm biếm về cách mạng và sự tham vọng quyền lực",
            "https://picsum.photos/seed/book17/200/300", 4.7, 1200, "Fiction", true));

        books.add(new Book(18, "Brave New World", "Aldous Huxley", 95000,
            "Tiểu thuyết viễn tưởng về tương lai xã hội được kiểm soát bằng công nghệ",
            "https://picsum.photos/seed/book18/200/300", 4.6, 1450, "Science Fiction", true));

        books.add(new Book(19, "The Catcher in the Rye", "J.D. Salinger", 88000,
            "Tiểu thuyết về tuổi trẻ, sự cô đơn và con đường tìm kiếm bản sắc",
            "https://picsum.photos/seed/book19/200/300", 4.4, 980, "Fiction", true));

        books.add(new Book(20, "Outlander", "Diana Gabaldon", 135000,
            "Hành trình xuyên thời gian kết hợp lãng mạn, phiêu lưu và lịch sử",
            "https://picsum.photos/seed/book20/200/300", 4.7, 2100, "Romance", true));

        books.add(new Book(21, "The Name of the Wind", "Patrick Rothfuss", 129000,
            "Tiểu thuyết fantasy huyền thoại về một pháp sư trẻ tài năng",
            "https://picsum.photos/seed/book21/200/300", 4.8, 2300, "Fantasy", true));

        books.add(new Book(22, "Foundation", "Isaac Asimov", 105000,
            "Tác phẩm kinh điển sci-fi về việc xây dựng lại nền văn minh",
            "https://picsum.photos/seed/book22/200/300", 4.7, 1500, "Science Fiction", true));

        books.add(new Book(23, "Neuromancer", "William Gibson", 98000,
            "Khởi đầu của thể loại cyberpunk - Tương lai điểm tối của công nghệ",
            "https://picsum.photos/seed/book23/200/300", 4.5, 1100, "Science Fiction", true));

        books.add(new Book(24, "The Notebook", "Nicholas Sparks", 85000,
            "Câu chuyện tình yêu đẹp và đau thương xuyên suốt thời gian",
            "https://picsum.photos/seed/book24/200/300", 4.4, 1300, "Romance", true));

        books.add(new Book(25, "Twilight", "Stephenie Meyer", 92000,
            "Tiểu thuyết lãng mạn về tình yêu giữa con người và ma cà rồng",
            "https://picsum.photos/seed/book25/200/300", 4.2, 2500, "Romance", true));

        books.add(new Book(26, "A Game of Thrones", "George R.R. Martin", 145000,
            "Tiểu thuyết fantasy đầu tiên của loạt Bản Ghi Chép Cơn Lốc - Vương quyền và chính trị",
            "https://picsum.photos/seed/book26/200/300", 4.8, 2800, "Fantasy", true));

        books.add(new Book(27, "The Girl with the Dragon Tattoo", "Stieg Larsson", 119000,
            "Bí ẩn tội phạm Thụy Điển - Nhà báo và hacker tài ba",
            "https://picsum.photos/seed/book27/200/300", 4.6, 2000, "Mystery", true));

        books.add(new Book(28, "Ender's Game", "Orson Scott Card", 105000,
            "Tiểu thuyết sci-fi về một thiên tài quân sự trẻ tuổi",
            "https://picsum.photos/seed/book28/200/300", 4.7, 1600, "Science Fiction", true));

        books.add(new Book(29, "Jane Eyre", "Charlotte Brontë", 82000,
            "Tiểu thuyết gothic lãng mạn về tình yêu, tự do và độc lập",
            "https://picsum.photos/seed/book29/200/300", 4.6, 1100, "Romance", true));

        books.add(new Book(30, "Wuthering Heights", "Emily Brontë", 88000,
            "Tiểu thuyết gothic tối tăm về tình yêu, trả thù và khổ đau",
            "https://picsum.photos/seed/book30/200/300", 4.5, 950, "Gothic", true));

        books.add(new Book(31, "The Handmaid's Tale", "Margaret Atwood", 112000,
            "Tiểu thuyết dystopian về xã hội phụ nữ bị áp bức",
            "https://picsum.photos/seed/book31/200/300", 4.8, 1800, "Dystopian", true));

        books.add(new Book(32, "Inferno", "Dan Brown", 108000,
            "Thriller bí ẩn qua những tác phẩm của Dante",
            "https://picsum.photos/seed/book32/200/300", 4.5, 1400, "Mystery", true));

        books.add(new Book(33, "The Seven Husbands of Evelyn Hugo", "Taylor Jenkins Reid", 119000,
            "Tiểu thuyết lịch sử về một nữ diễn viên bí ẩn ở Hollywood",
            "https://picsum.photos/seed/book33/200/300", 4.7, 1600, "Historical", true));

        books.add(new Book(34, "And Then There Were None", "Agatha Christie", 79000,
            "Tiểu thuyết bí ẩn kinh điển - Mười nhân vật và một bí mật chết chóc",
            "https://picsum.photos/seed/book34/200/300", 4.8, 1400, "Mystery", true));

        books.add(new Book(35, "The Silence of the Lambs", "Thomas Harris", 105000,
            "Thriller tâm lý kinh hoàng về một serial killer thiên tài",
            "https://picsum.photos/seed/book35/200/300", 4.7, 1200, "Thriller", true));

        books.add(new Book(36, "Sapiens (Vietnamese)", "Yuval Noah Harari", 135000,
            "Lịch sử loài người - Từ thời kỳ đá cũ đến hiện đại",
            "https://picsum.photos/seed/book36/200/300", 4.9, 2500, "History", true));

        books.add(new Book(37, "Rich Dad Poor Dad", "Robert Kiyosaki", 125000,
            "Những bài học về tiền bạc và đầu tư từ hai hình ảnh cha của tác giả",
            "https://picsum.photos/seed/book37/200/300", 4.7, 2300, "Finance", true));

        books.add(new Book(38, "The Lean Startup", "Eric Ries", 115000,
            "Cách tiếp cận kinh doanh hiệu quả cho thời đại kỹ thuật số",
            "https://picsum.photos/seed/book38/200/300", 4.6, 1500, "Business", true));

        books.add(new Book(39, "Zero to One", "Peter Thiel", 128000,
            "Những bí mật xây dựng công ty thành công từ con số không",
            "https://picsum.photos/seed/book39/200/300", 4.7, 1700, "Business", true));

        books.add(new Book(40, "The Art of War", "Sun Tzu", 68000,
            "Chiến thuật quân sự cổ đại vẫn áp dụng được trong kinh doanh hiện đại",
            "https://picsum.photos/seed/book40/200/300", 4.6, 1200, "Philosophy", true));

        return books;
    }

    /**
     * Lấy sách theo danh mục
     */
    public static List<Book> getBooksByCategory(String category) {
        List<Book> books = getAllBooks();
        List<Book> filtered = new ArrayList<>();
        for (Book book : books) {
            if (book.category.equalsIgnoreCase(category)) {
                filtered.add(book);
            }
        }
        return filtered;
    }

    /**
     * Tìm kiếm sách theo tiêu đề
     */
    public static List<Book> searchBooks(String query) {
        List<Book> books = getAllBooks();
        List<Book> results = new ArrayList<>();
        query = query.toLowerCase();
        for (Book book : books) {
            if (book.title.toLowerCase().contains(query) ||
                book.author.toLowerCase().contains(query)) {
                results.add(book);
            }
        }
        return results;
    }
}

