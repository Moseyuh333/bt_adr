package com.example.bookstore.utils;

import com.example.bookstore.models.Book;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to convert database Book entities to display Book models
 * with all required fields initialized
 */
public class BookConverter {

    /**
     * Convert a single database Book entity to display Book model
     * Ensures all fields are properly initialized and never null
     */
    public static Book convertToDisplayBook(com.example.bookstore.database.entities.Book dbBook) {
        if (dbBook == null) return null;

        // Generate realistic rating and stats
        double rating = 4.0 + (Math.random() * 1.0); // 4.0 - 5.0
        int reviews = (int)(100 + Math.random() * 900); // 100 - 1000

        // Ensure we have valid data before creating Book object - STRICT NULL CHECKS
        String title = (dbBook.getTitle() != null && !dbBook.getTitle().trim().isEmpty())
            ? dbBook.getTitle().trim() : "Sách " + dbBook.getId();
        String author = (dbBook.getAuthor() != null && !dbBook.getAuthor().trim().isEmpty())
            ? dbBook.getAuthor().trim() : "Tác giả";
        String description = (dbBook.getDescription() != null && !dbBook.getDescription().trim().isEmpty())
            ? dbBook.getDescription().trim() : "Mô tả sách";
        String category = (dbBook.getCategory() != null && !dbBook.getCategory().trim().isEmpty())
            ? dbBook.getCategory().trim() : "Sách";
        String imageUrl = (dbBook.getImageUrl() != null && !dbBook.getImageUrl().trim().isEmpty())
            ? dbBook.getImageUrl().trim() : "https://picsum.photos/seed/book" + dbBook.getId() + "/300/450";

        Book book = new Book(
            String.valueOf(dbBook.getId()),
            title,
            author,
            dbBook.getPrice() > 0 ? dbBook.getPrice() : 50000,
            imageUrl,
            description,
            category,
            rating,
            dbBook.getStock()
        );

        // Initialize all display fields - ensure nothing is null
        book.id = dbBook.getId();
        book.title = title; // EXPLICIT: title = tên sách
        book.author = author;
        book.category = category; // EXPLICIT: category = danh mục
        book.description = description;
        book.coverImage = imageUrl;
        book.rating = rating;
        book.reviews = reviews;
        book.soldCount = (int)(50 + Math.random() * 950); // 50 - 1000
        book.discount = (int)(10 + Math.random() * 20); // 10-30%
        book.originalPrice = book.price * (1 + book.discount / 100.0);
        book.shopName = "BookStore Official";
        book.publisher = (dbBook.getPublisher() != null && !dbBook.getPublisher().trim().isEmpty())
            ? dbBook.getPublisher().trim() : getPublisherByCategory(category);
        book.inStock = dbBook.getStock() > 0;
        book.quantity = dbBook.getStock();

        return book;
    }

    /**
     * Convert a list of database Book entities to display Book models
     */
    public static List<Book> convertToDisplayBooks(List<com.example.bookstore.database.entities.Book> dbBooks) {
        List<Book> books = new ArrayList<>();
        if (dbBooks != null) {
            for (com.example.bookstore.database.entities.Book dbBook : dbBooks) {
                Book book = convertToDisplayBook(dbBook);
                if (book != null) {
                    books.add(book);
                }
            }
        }
        return books;
    }

    /**
     * Get publisher name based on category
     */
    private static String getPublisherByCategory(String category) {
        if (category == null) return "NXB Trẻ";

        switch (category.toLowerCase()) {
            case "fiction":
            case "tiểu thuyết":
            case "văn học":
                return "NXB Văn Học";
            case "children":
            case "thiếu nhi":
                return "NXB Kim Đồng";
            case "science":
            case "khoa học":
            case "technology":
            case "công nghệ":
                return "NXB Khoa Học & Kỹ Thuật";
            case "business":
            case "kinh doanh":
            case "finance":
            case "tài chính":
                return "NXB Lao Động";
            case "education":
            case "giáo dục":
                return "NXB Giáo Dục";
            case "art":
            case "nghệ thuật":
                return "NXB Mỹ Thuật";
            default:
                return "NXB Trẻ";
        }
    }

    /**
     * Ensure a Book object has all required display fields initialized
     * Use this for existing Book objects that may be missing fields
     */
    public static void ensureDisplayFields(Book book) {
        if (book == null) return;

        // Check and initialize missing fields
        if (book.soldCount == 0) {
            book.soldCount = (int)(50 + Math.random() * 950);
        }

        if (book.discount == 0) {
            book.discount = (int)(10 + Math.random() * 20);
        }

        if (book.originalPrice == 0 || book.originalPrice <= book.price) {
            book.originalPrice = book.price * (1 + book.discount / 100.0);
        }

        if (book.shopName == null || book.shopName.isEmpty()) {
            book.shopName = "BookStore Official";
        }

        if (book.publisher == null || book.publisher.isEmpty()) {
            book.publisher = getPublisherByCategory(book.category);
        }

        if (book.reviews == 0) {
            book.reviews = (int)(100 + Math.random() * 900);
        }

        if (book.rating == 0) {
            book.rating = 4.0 + (Math.random() * 1.0);
        }
    }

    /**
     * Ensure all Book objects in a list have required display fields
     */
    public static void ensureDisplayFields(List<Book> books) {
        if (books != null) {
            for (Book book : books) {
                ensureDisplayFields(book);
            }
        }
    }
}

