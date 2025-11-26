package com.example.bookstore.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookstore.R;
import com.example.bookstore.adapters.BookAdapter;
import com.example.bookstore.adapters.ReviewAdapter;
import com.example.bookstore.models.Book;
import com.example.bookstore.models.Cart;
import com.example.bookstore.models.Review;
import com.example.bookstore.utils.BookDataLoader;
import com.example.bookstore.utils.FavoritesManager;
import com.example.bookstore.utils.RecentlyViewedManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookDetailFragment extends Fragment {

    private Book book;
    private TextView quantityText, stockStatusText;
    private TextView priceText, ratingText, reviewsText, descriptionText, authorText, categoryText;
    private TextView shopNameText, soldCountText, originalPriceText, discountBadgeText;
    private ImageView bookImage;
    private RatingBar ratingBar;
    private Button addToCartBtn, decreaseBtn, increaseBtn, buyNowBtn, favoriteBtn;
    private RecyclerView reviewsRecycler, relatedBooksRecycler;
    private Cart cart;
    private FavoritesManager favoritesManager;
    private RecentlyViewedManager recentlyViewedManager;
    private int currentQuantity = 1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_book_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            cart = Cart.getInstance();
            favoritesManager = FavoritesManager.getInstance(requireContext());
            recentlyViewedManager = RecentlyViewedManager.getInstance(requireContext());

            // Get book from arguments
            if (getArguments() != null) {
                book = (Book) getArguments().getSerializable("book");
            }

            if (book == null) {
                Toast.makeText(getContext(), "Book not found", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(view).popBackStack();
                return;
            }

            // Add to recently viewed
            recentlyViewedManager.addRecentlyViewed(book);

            // Initialize views
            bookImage = view.findViewById(R.id.detail_book_image);
            TextView titleText = view.findViewById(R.id.detail_book_title);
            authorText = view.findViewById(R.id.detail_book_author);
            priceText = view.findViewById(R.id.detail_book_price);
            originalPriceText = view.findViewById(R.id.detail_original_price);
            discountBadgeText = view.findViewById(R.id.detail_discount_badge);
            ratingBar = view.findViewById(R.id.detail_rating_bar);
            ratingText = view.findViewById(R.id.detail_rating_text);
            reviewsText = view.findViewById(R.id.detail_reviews_count);
            soldCountText = view.findViewById(R.id.detail_sold_count);
            shopNameText = view.findViewById(R.id.detail_shop_name);
            descriptionText = view.findViewById(R.id.detail_book_description);
            categoryText = view.findViewById(R.id.detail_book_category);
            stockStatusText = view.findViewById(R.id.stock_status_text);
            quantityText = view.findViewById(R.id.quantity_text);
            decreaseBtn = view.findViewById(R.id.decrease_quantity_btn);
            increaseBtn = view.findViewById(R.id.increase_quantity_btn);
            addToCartBtn = view.findViewById(R.id.add_to_cart_btn);
            buyNowBtn = view.findViewById(R.id.buy_now_btn);
            favoriteBtn = view.findViewById(R.id.favorite_btn);
            reviewsRecycler = view.findViewById(R.id.reviews_recycler);
            relatedBooksRecycler = view.findViewById(R.id.related_books_recycler);

            // Set book details with null checks
            if (titleText != null) {
                titleText.setText(book.title != null ? book.title : "Sách");
            }
            if (authorText != null) {
                authorText.setText("Tác giả: " + (book.author != null ? book.author : "Chưa rõ"));
            }
            if (priceText != null) {
                priceText.setText(String.format("%,.0f₫", book.price));
            }

            // Show original price and discount if applicable
            if (originalPriceText != null) {
                if (book.originalPrice > book.price) {
                    originalPriceText.setVisibility(View.VISIBLE);
                    originalPriceText.setText(String.format("%,.0f₫", book.originalPrice));
                    originalPriceText.setPaintFlags(originalPriceText.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    originalPriceText.setVisibility(View.GONE);
                }
            }

            // Show discount badge
            if (discountBadgeText != null) {
                if (book.discount > 0) {
                    discountBadgeText.setVisibility(View.VISIBLE);
                    discountBadgeText.setText("-" + book.discount + "%");
                } else {
                    discountBadgeText.setVisibility(View.GONE);
                }
            }

            if (ratingBar != null) {
                ratingBar.setRating((float) book.rating);
            }
            if (ratingText != null) {
                ratingText.setText(String.format("%.1f", book.rating));
            }
            if (reviewsText != null) {
                reviewsText.setText("(" + book.reviews + " đánh giá)");
            }

            // Show sold count
            if (soldCountText != null) {
                if (book.soldCount > 0) {
                    if (book.soldCount >= 1000) {
                        soldCountText.setText("Đã bán " + String.format("%.1fk", book.soldCount / 1000.0));
                    } else {
                        soldCountText.setText("Đã bán " + book.soldCount);
                    }
                } else {
                    soldCountText.setText("");
                }
            }

            // Show shop name
            if (shopNameText != null) {
                if (book.shopName != null && !book.shopName.isEmpty()) {
                    shopNameText.setText("📚 " + book.shopName);
                } else {
                    shopNameText.setText("📚 BookStore Official");
                }
            }

            if (descriptionText != null) {
                descriptionText.setText(book.description != null ? book.description : "Mô tả sách");
            }
            if (categoryText != null) {
                categoryText.setText("Thể loại: " + (book.category != null ? book.category : "Khác"));
            }

            // Update stock status
            updateStockStatus();

            // Load image with null check
            if (bookImage != null) {
                if (book.coverImage != null && !book.coverImage.isEmpty()) {
                    try {
                        Glide.with(this)
                            .load(book.coverImage)
                            .placeholder(R.drawable.book_placeholder)
                            .error(R.drawable.book_placeholder)
                            .centerCrop()
                            .into(bookImage);
                    } catch (Exception e) {
                        bookImage.setImageResource(R.drawable.book_placeholder);
                    }
                } else {
                    bookImage.setImageResource(R.drawable.book_placeholder);
                }
            }

            // Set default quantity
            currentQuantity = 1;
            updateQuantityDisplay();

            // Decrease quantity button
            if (decreaseBtn != null) {
                decreaseBtn.setOnClickListener(v -> {
                    if (currentQuantity > 1) {
                        currentQuantity--;
                        updateQuantityDisplay();
                    }
                });
            }

            // Increase quantity button
            if (increaseBtn != null) {
                increaseBtn.setOnClickListener(v -> {
                    if (currentQuantity < book.quantity) {
                        currentQuantity++;
                        updateQuantityDisplay();
                    } else {
                        Toast.makeText(getContext(),
                            String.format("Chỉ còn %d cuốn trong kho", book.quantity),
                            Toast.LENGTH_SHORT).show();
                    }
                });
            }

            // Add to cart button
            if (addToCartBtn != null) {
                addToCartBtn.setOnClickListener(v -> handleAddToCart(view));
            }

            // Buy now button
            if (buyNowBtn != null) {
                buyNowBtn.setOnClickListener(v -> handleBuyNow(view));
            }

            // Favorite button
            if (favoriteBtn != null) {
                updateFavoriteButton();
                favoriteBtn.setOnClickListener(v -> handleToggleFavorite());
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error loading book details", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleAddToCart(View view) {
        try {
            if (currentQuantity <= 0) {
                Toast.makeText(getContext(), "Số lượng phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!book.inStock || book.quantity <= 0) {
                Toast.makeText(getContext(), "Sản phẩm hết hàng", Toast.LENGTH_SHORT).show();
                return;
            }

            if (currentQuantity > book.quantity) {
                Toast.makeText(getContext(),
                    String.format("Chỉ còn %d cuốn trong kho", book.quantity),
                    Toast.LENGTH_SHORT).show();
                return;
            }

            cart.addItem(book, currentQuantity);
            Toast.makeText(getContext(), currentQuantity + " cuốn đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();

            // Reset quantity to 1 after adding
            currentQuantity = 1;
            updateQuantityDisplay();

        } catch (Exception e) {
            Toast.makeText(getContext(), "Lỗi khi thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateQuantityDisplay() {
        if (quantityText != null) {
            quantityText.setText(String.valueOf(currentQuantity));
        }
    }

    private void updateStockStatus() {
        if (stockStatusText != null) {
            if (book.quantity > 50) {
                stockStatusText.setText("Còn " + book.quantity + " cuốn");
                stockStatusText.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            } else if (book.quantity > 10) {
                stockStatusText.setText("Còn " + book.quantity + " cuốn");
                stockStatusText.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
            } else if (book.quantity > 0) {
                stockStatusText.setText("Chỉ còn " + book.quantity + " cuốn");
                stockStatusText.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            } else {
                stockStatusText.setText("Hết hàng");
                stockStatusText.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            }
        }

        // Load reviews - with null check
        if (reviewsRecycler != null) {
            loadReviews();
        }

        // Load related books - with null check
        if (relatedBooksRecycler != null) {
            loadRelatedBooks();
        }
    }

    private void loadReviews() {
        try {
            if (reviewsRecycler == null || getContext() == null) return;

            List<Review> reviews = generateSampleReviews();
            reviewsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
            reviewsRecycler.setAdapter(new ReviewAdapter(reviews));

            // Update rating statistics
            updateRatingStatistics();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateRatingStatistics() {
        try {
            View view = getView();
            if (view == null) return;

            // Calculate rating distribution based on book's average rating
            double rating = book.rating;

            // Generate realistic distribution
            int percent5Star, percent4Star, percent3Star, percent2Star, percent1Star;

            if (rating >= 4.7) {
                percent5Star = 75 + (int)(Math.random() * 10);
                percent4Star = 20 - (int)(Math.random() * 5);
                percent3Star = 5 - (int)(Math.random() * 3);
                percent2Star = (int)(Math.random() * 2);
                percent1Star = 0;
            } else if (rating >= 4.3) {
                percent5Star = 60 + (int)(Math.random() * 10);
                percent4Star = 25 + (int)(Math.random() * 5);
                percent3Star = 8 + (int)(Math.random() * 3);
                percent2Star = (int)(Math.random() * 3);
                percent1Star = (int)(Math.random() * 2);
            } else if (rating >= 4.0) {
                percent5Star = 50 + (int)(Math.random() * 10);
                percent4Star = 30 + (int)(Math.random() * 5);
                percent3Star = 12 + (int)(Math.random() * 3);
                percent2Star = 5 + (int)(Math.random() * 2);
                percent1Star = (int)(Math.random() * 3);
            } else {
                percent5Star = 40 + (int)(Math.random() * 10);
                percent4Star = 30 + (int)(Math.random() * 5);
                percent3Star = 15 + (int)(Math.random() * 5);
                percent2Star = 10 + (int)(Math.random() * 3);
                percent1Star = 5;
            }

            // Normalize to 100%
            int total = percent5Star + percent4Star + percent3Star + percent2Star + percent1Star;
            if (total != 100) {
                int diff = 100 - total;
                percent5Star += diff;
            }

            // Update progress bars and percentages
            android.widget.ProgressBar progress5 = view.findViewById(R.id.progress_5_star);
            android.widget.ProgressBar progress4 = view.findViewById(R.id.progress_4_star);
            android.widget.ProgressBar progress3 = view.findViewById(R.id.progress_3_star);
            android.widget.ProgressBar progress2 = view.findViewById(R.id.progress_2_star);
            android.widget.ProgressBar progress1 = view.findViewById(R.id.progress_1_star);

            TextView count5 = view.findViewById(R.id.count_5_star);
            TextView count4 = view.findViewById(R.id.count_4_star);
            TextView count3 = view.findViewById(R.id.count_3_star);
            TextView count2 = view.findViewById(R.id.count_2_star);
            TextView count1 = view.findViewById(R.id.count_1_star);

            if (progress5 != null) progress5.setProgress(percent5Star);
            if (progress4 != null) progress4.setProgress(percent4Star);
            if (progress3 != null) progress3.setProgress(percent3Star);
            if (progress2 != null) progress2.setProgress(percent2Star);
            if (progress1 != null) progress1.setProgress(percent1Star);

            if (count5 != null) count5.setText(percent5Star + "%");
            if (count4 != null) count4.setText(percent4Star + "%");
            if (count3 != null) count3.setText(percent3Star + "%");
            if (count2 != null) count2.setText(percent2Star + "%");
            if (count1 != null) count1.setText(percent1Star + "%");

            // Update overall rating display
            TextView overallRating = view.findViewById(R.id.overall_rating);
            RatingBar overallRatingBar = view.findViewById(R.id.overall_rating_bar);
            TextView totalReviewsText = view.findViewById(R.id.total_reviews_text);

            if (overallRating != null) overallRating.setText(String.format("%.1f", rating));
            if (overallRatingBar != null) overallRatingBar.setRating((float)rating);
            if (totalReviewsText != null) {
                String reviewText = book.reviews >= 1000
                    ? String.format("%.1fk đánh giá", book.reviews / 1000.0)
                    : book.reviews + " đánh giá";
                totalReviewsText.setText(reviewText);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadRelatedBooks() {
        try {
            if (getContext() == null || book == null) return;

            // Load related books from database in background
            new Thread(() -> {
                try {
                    com.example.bookstore.database.AppDatabase db =
                        com.example.bookstore.database.AppDatabase.getInstance(getContext());

                    List<Book> relatedBooks = new ArrayList<>();

                    // Get books from same category (with null check)
                    if (book.category != null && !book.category.isEmpty()) {
                        List<com.example.bookstore.database.entities.Book> dbBooks =
                            db.bookDao().getBooksByCategory(book.category);

                        // Convert to old Book model and filter out current book
                        if (dbBooks != null) {
                            for (com.example.bookstore.database.entities.Book dbBook : dbBooks) {
                                if (dbBook != null && dbBook.getTitle() != null &&
                                    book.title != null && !dbBook.getTitle().equals(book.title)) {
                                    Book relBook = com.example.bookstore.utils.BookConverter.convertToDisplayBook(dbBook);
                                    if (relBook != null) {
                                        relatedBooks.add(relBook);
                                        if (relatedBooks.size() >= 8) break; // Limit to 8 books
                                    }
                                }
                            }
                        }
                    }

                    // If not enough books in same category, add from other categories
                    if (relatedBooks.size() < 6) {
                        List<com.example.bookstore.database.entities.Book> allBooks =
                            db.bookDao().getAllActiveBooks();
                        if (allBooks != null) {
                            for (com.example.bookstore.database.entities.Book dbBook : allBooks) {
                                if (dbBook != null && dbBook.getTitle() != null &&
                                    book.title != null && !dbBook.getTitle().equals(book.title)) {
                                    // Check category is different
                                    String dbCategory = dbBook.getCategory();
                                    if (dbCategory == null || book.category == null ||
                                        !dbCategory.equals(book.category)) {
                                        Book relBook = com.example.bookstore.utils.BookConverter.convertToDisplayBook(dbBook);
                                        if (relBook != null) {
                                            relatedBooks.add(relBook);
                                            if (relatedBooks.size() >= 8) break;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    final List<Book> finalRelatedBooks = relatedBooks;

                    // Update UI on main thread
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            if (!finalRelatedBooks.isEmpty() && relatedBooksRecycler != null) {
                                relatedBooksRecycler.setLayoutManager(
                                    new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                                relatedBooksRecycler.setAdapter(new BookAdapter(finalRelatedBooks));
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Review> generateSampleReviews() {
        List<Review> reviews = new ArrayList<>();

        // Generate sample reviews based on book rating
        String[] names = {
            "Nguyễn Minh Anh", "Trần Văn Bình", "Lê Thị Hương",
            "Phạm Quốc Đạt", "Hoàng Thu Hà", "Vũ Đức Mạnh",
            "Đặng Thị Lan", "Bùi Văn Nam", "Dương Thị Phương"
        };

        String[][] commentsByRating = {
            // 5 stars
            {
                "Cuốn sách tuyệt vời! Nội dung rất hay và ý nghĩa. Tôi đã học được nhiều điều bổ ích từ cuốn sách này. Đóng gói cẩn thận, giao hàng nhanh. Rất đáng đọc!",
                "Một trong những cuốn sách hay nhất tôi từng đọc. Cách viết dễ hiểu, nội dung sâu sắc. Rất phù hợp với những ai muốn tìm hiểu thêm về chủ đề này.",
                "Sách chất lượng tốt, bìa đẹp, in ấn rõ nét. Nội dung hấp dẫn từ đầu đến cuối. Shop giao hàng rất nhanh và đóng gói cẩn thận. 5 sao không có gì để chê!",
                "Đọc xong cuốn này mà cảm thấy mở mang đầu óc thật sự. Tác giả viết rất hay và dễ hiểu. Giá cả hợp lý, đáng đồng tiền. Sẽ giới thiệu cho bạn bè!"
            },
            // 4 stars
            {
                "Sách hay, nội dung bổ ích. Có một vài chương hơi dài dòng nhưng nhìn chung vẫn rất đáng đọc. Giao hàng nhanh, sách mới nguyên seal.",
                "Cuốn sách khá tốt, phù hợp với mọi lứa tuổi. Một số phần có thể ngắn gọn hơn nhưng vẫn rất giá trị. Đóng gói cẩn thận, ship nhanh.",
                "Nội dung hay và thiết thực. Giấy in đẹp, chữ rõ. Có một vài lỗi chính tả nhỏ nhưng không ảnh hưởng nhiều. Nhìn chung là rất hài lòng!",
                "Đáng đọc! Một số ý tưởng rất mới mẻ và hữu ích. Có thể áp dụng vào thực tế. Nếu giá rẻ hơn một chút thì tuyệt vời."
            },
            // 3-4 stars
            {
                "Sách cũng ổn, có nhiều thông tin hay. Tuy nhiên có phần nào đó hơi khó hiểu với người mới. Cần đọc kỹ mới nắm được hết ý.",
                "Nội dung tạm được, có phần hay nhưng cũng có phần hơi nhạt. Phù hợp để đọc giải trí hoặc tham khảo thêm kiến thức.",
                "Chất lượng sách tốt, nội dung khá ổn. Không có gì nổi bật lắm nhưng cũng không tệ. Đọc để biết thêm thông tin cũng được."
            }
        };

        String[] dates = {
            "25/11/2025", "24/11/2025", "23/11/2025", "22/11/2025",
            "21/11/2025", "20/11/2025", "19/11/2025", "18/11/2025"
        };

        int numReviews = Math.min(8, book.reviews > 0 ? (int)(3 + Math.random() * 6) : 0);
        for (int i = 0; i < numReviews; i++) {
            // Generate rating based on book's average rating
            float rating;
            String comment;

            if (book.rating >= 4.5) {
                // Mostly 5 stars with some 4 stars
                rating = Math.random() < 0.8 ? 5.0f : 4.0f;
            } else if (book.rating >= 4.0) {
                // Mix of 4 and 5 stars
                rating = Math.random() < 0.6 ? 4.0f : 5.0f;
            } else {
                // Mix of 3, 4, and 5 stars
                double rand = Math.random();
                if (rand < 0.4) rating = 4.0f;
                else if (rand < 0.7) rating = 3.0f;
                else rating = 5.0f;
            }

            // Select appropriate comment based on rating
            if (rating == 5.0f) {
                comment = commentsByRating[0][(int)(Math.random() * commentsByRating[0].length)];
            } else if (rating == 4.0f) {
                comment = commentsByRating[1][(int)(Math.random() * commentsByRating[1].length)];
            } else {
                comment = commentsByRating[2][(int)(Math.random() * commentsByRating[2].length)];
            }

            String name = names[i % names.length];
            String date = dates[i % dates.length];

            reviews.add(new Review(name, rating, date, comment));
        }

        return reviews;
    }

    private void handleBuyNow(View view) {
        try {
            if (currentQuantity <= 0) {
                Toast.makeText(getContext(), "Số lượng phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!book.inStock || book.quantity <= 0) {
                Toast.makeText(getContext(), "Sản phẩm hết hàng", Toast.LENGTH_SHORT).show();
                return;
            }

            if (currentQuantity > book.quantity) {
                Toast.makeText(getContext(),
                    String.format("Chỉ còn %d cuốn trong kho", book.quantity),
                    Toast.LENGTH_SHORT).show();
                return;
            }

            // Clear cart and add only this book
            cart.clear();
            cart.addItem(book, currentQuantity);

            // Navigate to checkout
            Navigation.findNavController(view).navigate(R.id.checkoutFragment);

            Toast.makeText(getContext(), "Chuyển đến thanh toán...", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(getContext(), "Lỗi khi mua hàng", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void handleToggleFavorite() {
        try {
            favoritesManager.toggleFavorite(book);
            updateFavoriteButton();

            if (favoritesManager.isFavorite(book.id)) {
                Toast.makeText(getContext(), "❤️ Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "💔 Đã xóa khỏi yêu thích", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Lỗi", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void updateFavoriteButton() {
        try {
            if (favoritesManager.isFavorite(book.id)) {
                favoriteBtn.setText("❤️ Đã yêu thích");
                favoriteBtn.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
            } else {
                favoriteBtn.setText("🤍 Thêm vào yêu thích");
                favoriteBtn.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

