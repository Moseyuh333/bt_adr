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
import com.example.bookstore.adapters.ReviewAdapter;
import com.example.bookstore.models.Book;
import com.example.bookstore.models.Cart;
import com.example.bookstore.models.Review;

import java.util.ArrayList;
import java.util.List;

public class BookDetailFragment extends Fragment {

    private Book book;
    private TextView quantityText, stockStatusText;
    private TextView priceText, ratingText, reviewsText, descriptionText, authorText, categoryText;
    private ImageView bookImage;
    private RatingBar ratingBar;
    private Button addToCartBtn, decreaseBtn, increaseBtn;
    private RecyclerView reviewsRecycler;
    private Cart cart;
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

            // Get book from arguments
            if (getArguments() != null) {
                book = (Book) getArguments().getSerializable("book");
            }

            if (book == null) {
                Toast.makeText(getContext(), "Book not found", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(view).popBackStack();
                return;
            }

            // Initialize views
            bookImage = view.findViewById(R.id.detail_book_image);
            TextView titleText = view.findViewById(R.id.detail_book_title);
            authorText = view.findViewById(R.id.detail_book_author);
            priceText = view.findViewById(R.id.detail_book_price);
            ratingBar = view.findViewById(R.id.detail_rating_bar);
            ratingText = view.findViewById(R.id.detail_rating_text);
            reviewsText = view.findViewById(R.id.detail_reviews_count);
            descriptionText = view.findViewById(R.id.detail_book_description);
            categoryText = view.findViewById(R.id.detail_book_category);
            stockStatusText = view.findViewById(R.id.stock_status_text);
            quantityText = view.findViewById(R.id.quantity_text);
            decreaseBtn = view.findViewById(R.id.decrease_quantity_btn);
            increaseBtn = view.findViewById(R.id.increase_quantity_btn);
            addToCartBtn = view.findViewById(R.id.add_to_cart_btn);
            reviewsRecycler = view.findViewById(R.id.reviews_recycler);

            // Set book details
            titleText.setText(book.title);
            authorText.setText("Tác giả: " + book.author);
            priceText.setText(String.format("%,.0f₫", book.price));
            ratingBar.setRating((float) book.rating);
            ratingText.setText(String.format("%.1f", book.rating));
            reviewsText.setText(book.reviews + " đánh giá");
            descriptionText.setText(book.description);
            categoryText.setText("Thể loại: " + book.category);

            // Update stock status
            updateStockStatus();

            // Load image
            if (book.coverImage != null && !book.coverImage.isEmpty()) {
                Glide.with(this)
                    .load(book.coverImage)
                    .placeholder(R.drawable.book_placeholder)
                    .error(R.drawable.book_placeholder)
                    .centerCrop()
                    .into(bookImage);
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
            addToCartBtn.setOnClickListener(v -> handleAddToCart(view));

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

        // Load reviews
        loadReviews();
    }

    private void loadReviews() {
        try {
            List<Review> reviews = generateSampleReviews();
            reviewsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
            reviewsRecycler.setAdapter(new ReviewAdapter(reviews));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Review> generateSampleReviews() {
        List<Review> reviews = new ArrayList<>();

        // Generate sample reviews based on book rating
        String[] names = {"Nguyễn Văn A", "Trần Thị B", "Lê Văn C", "Phạm Thị D", "Hoàng Văn E"};
        String[] comments = {
            "Sách rất hay, nội dung bổ ích và dễ hiểu!",
            "Đọc xong cảm thấy học được nhiều điều mới.",
            "Chất lượng tốt, giao hàng nhanh.",
            "Nội dung sâu sắc, đáng đọc!",
            "Một trong những cuốn sách hay nhất tôi từng đọc."
        };
        String[] dates = {"18/11/2025", "17/11/2025", "16/11/2025", "15/11/2025", "14/11/2025"};

        int numReviews = Math.min(5, book.reviews > 0 ? 5 : 0);
        for (int i = 0; i < numReviews; i++) {
            float rating = (float) (book.rating - 0.5 + Math.random());
            rating = Math.max(3.0f, Math.min(5.0f, rating));
            reviews.add(new Review(names[i], rating, dates[i], comments[i]));
        }

        return reviews;
    }
}

