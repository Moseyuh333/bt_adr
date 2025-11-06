package com.example.bookstore;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
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

        try {
            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
            if (navHostFragment != null) {
                navController = navHostFragment.getNavController();
                bottomNav = findViewById(R.id.bottom_navigation);

                if (bottomNav != null) {
                    NavigationUI.setupWithNavController(bottomNav, navController);

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
}

