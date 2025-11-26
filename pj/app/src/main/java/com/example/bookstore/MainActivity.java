package com.example.bookstore;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.example.bookstore.database.DatabaseHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNav;
    private NavController navController;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("BookstorePrefs", MODE_PRIVATE);

        // FORCE CLEAR old data to load new books with proper titles and categories
        // This ensures database v2 loads with brand new Vietnamese book data
        clearOldDataIfNeeded();

        // Initialize database with NEW data
        DatabaseHelper.initializeDatabase(this, success -> {
            runOnUiThread(() -> {
                if (success) {
                    Toast.makeText(this, "✅ Đã tải 53 sách mới!", Toast.LENGTH_SHORT).show();
                    // Create sample orders with new books
                    com.example.bookstore.utils.OrderManager.getInstance(MainActivity.this)
                        .createSampleOrdersIfNeeded(MainActivity.this);
                }
            });
        });

        try {
            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
            if (navHostFragment != null) {
                navController = navHostFragment.getNavController();
                bottomNav = findViewById(R.id.bottom_navigation);

                if (bottomNav != null) {
                    // Custom listener để xử lý navigation đúng cách
                    bottomNav.setOnItemSelectedListener(item -> {
                        int itemId = item.getItemId();
                        NavDestination currentDestination = navController.getCurrentDestination();

                        // Nếu đã ở destination đó rồi, không làm gì
                        if (currentDestination != null && currentDestination.getId() == itemId) {
                            return true;
                        }

                        if (itemId == R.id.homeFragment) {
                            // Click Home: clear back stack và về home
                            navController.popBackStack(R.id.homeFragment, true);
                            navController.navigate(R.id.homeFragment);
                            return true;
                        } else if (itemId == R.id.searchFragment) {
                            // Click Search
                            navController.popBackStack(R.id.homeFragment, false);
                            navController.navigate(R.id.searchFragment);
                            return true;
                        } else if (itemId == R.id.categoryFragment) {
                            // Click Categories: clear back stack trước khi navigate
                            navController.popBackStack(R.id.homeFragment, false);
                            navController.navigate(R.id.categoryFragment);
                            return true;
                        } else if (itemId == R.id.catalogFragment) {
                            // Click Catalog
                            navController.popBackStack(R.id.homeFragment, false);
                            navController.navigate(R.id.catalogFragment);
                            return true;
                        } else if (itemId == R.id.cartFragment) {
                            // Click Cart
                            navController.navigate(R.id.cartFragment);
                            return true;
                        } else if (itemId == R.id.profileFragment) {
                            // Click Profile
                            navController.navigate(R.id.profileFragment);
                            return true;
                        }

                        return false;
                    });

                    // Ẩn/hiện bottom navigation dựa trên destination
                    navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                        int destinationId = destination.getId();

                        // Ẩn bottom nav ở các màn hình auth
                        if (destinationId == R.id.loginFragment ||
                            destinationId == R.id.registerFragment ||
                            destinationId == R.id.forgotPasswordFragment) {
                            bottomNav.setVisibility(View.GONE);
                        } else {
                            bottomNav.setVisibility(View.VISIBLE);
                        }
                    });
                }

                // Kiểm tra trạng thái đăng nhập
                boolean isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false);
                if (isLoggedIn) {
                    // Nếu đã đăng nhập, chuyển đến home
                    navController.navigate(R.id.homeFragment);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Clear old data one time to ensure new books load properly
     * This fixes the issue where old data prevents new titles/categories from showing
     */
    private void clearOldDataIfNeeded() {
        SharedPreferences prefs = getSharedPreferences("AppState", MODE_PRIVATE);
        boolean hasCleared = prefs.getBoolean("cleared_for_v2", false);

        if (!hasCleared) {
            // Clear OrderManager data (stored in SharedPreferences)
            getSharedPreferences("OrderPrefs", MODE_PRIVATE).edit().clear().apply();

            // Clear app state
            getSharedPreferences("BookstorePrefs", MODE_PRIVATE).edit().clear().apply();

            // Mark as cleared
            prefs.edit().putBoolean("cleared_for_v2", true).apply();

            Toast.makeText(this, "🔄 Đang cập nhật dữ liệu mới...", Toast.LENGTH_SHORT).show();
        }
    }
}

