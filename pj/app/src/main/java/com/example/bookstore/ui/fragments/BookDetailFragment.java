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

import com.bumptech.glide.Glide;
import com.example.bookstore.R;
import com.example.bookstore.models.Book;
import com.example.bookstore.models.Cart;

public class BookDetailFragment extends Fragment {

    private Book book;
    private TextView quantityText;
    private TextView priceText, ratingText, reviewsText, descriptionText, authorText, categoryText;
    private ImageView bookImage;
    private RatingBar ratingBar;
    private Button addToCartBtn, decreaseBtn, increaseBtn;
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
            quantityText = view.findViewById(R.id.quantity_text);
            decreaseBtn = view.findViewById(R.id.decrease_quantity_btn);
            increaseBtn = view.findViewById(R.id.increase_quantity_btn);
            addToCartBtn = view.findViewById(R.id.add_to_cart_btn);

            // Set book details
            titleText.setText(book.title);
            authorText.setText("Tác giả: " + book.author);
            priceText.setText(String.format("%,.0f₫", book.price));
            ratingBar.setRating((float) book.rating);
            ratingText.setText(String.format("%.1f", book.rating));
            reviewsText.setText(book.reviews + " đánh giá");
            descriptionText.setText(book.description);
            categoryText.setText("Thể loại: " + book.category);

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
                    currentQuantity++;
                    updateQuantityDisplay();
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
                Toast.makeText(getContext(), "Quantity must be greater than 0", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!book.inStock) {
                Toast.makeText(getContext(), "Book is out of stock", Toast.LENGTH_SHORT).show();
                return;
            }

            cart.addItem(book, currentQuantity);
            Toast.makeText(getContext(), currentQuantity + " copy(ies) added to cart", Toast.LENGTH_SHORT).show();

            // Go back to previous screen
            Navigation.findNavController(view).popBackStack();

        } catch (Exception e) {
            Toast.makeText(getContext(), "Error adding to cart", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateQuantityDisplay() {
        if (quantityText != null) {
            quantityText.setText(String.valueOf(currentQuantity));
        }
    }
}

