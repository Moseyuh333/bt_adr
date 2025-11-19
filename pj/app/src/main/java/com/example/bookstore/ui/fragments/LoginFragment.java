package com.example.bookstore.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.bookstore.AdminActivity;
import com.example.bookstore.R;

public class LoginFragment extends Fragment {

    private EditText emailInput, passwordInput;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        sharedPreferences = requireActivity().getSharedPreferences("BookstorePrefs", Context.MODE_PRIVATE);

        emailInput = view.findViewById(R.id.login_email);
        passwordInput = view.findViewById(R.id.login_password);
        Button loginButton = view.findViewById(R.id.login_btn);
        TextView registerLink = view.findViewById(R.id.register_link);
        TextView forgotPasswordLink = view.findViewById(R.id.forgot_password_link);

        loginButton.setOnClickListener(v -> handleLogin());

        registerLink.setOnClickListener(v ->
            Navigation.findNavController(v).navigate(R.id.registerFragment)
        );

        forgotPasswordLink.setOnClickListener(v ->
            Navigation.findNavController(v).navigate(R.id.forgotPasswordFragment)
        );

        return view;
    }

    private void handleLogin() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check for admin credentials
        if (email.equals("admin") && password.equals("admin")) {
            sharedPreferences.edit()
                .putBoolean("is_logged_in", true)
                .putBoolean("is_admin", true)
                .putString("user_email", "admin@bookstore.com")
                .putString("user_name", "Admin")
                .apply();
            Toast.makeText(getContext(), "Chào mừng Admin!", Toast.LENGTH_SHORT).show();

            // Chuyển sang AdminActivity thay vì dùng nav graph của user
            Intent intent = new Intent(requireActivity(), AdminActivity.class);
            startActivity(intent);
            requireActivity().finish();
            return;
        }

        // Kiểm tra thông tin đăng nhập (demo - kiểm tra với dữ liệu đã lưu)
        String savedEmail = sharedPreferences.getString("user_email", "");
        String savedPassword = sharedPreferences.getString("user_password", "");

        if (email.equals(savedEmail) && password.equals(savedPassword)) {
            // Đăng nhập thành công
            sharedPreferences.edit()
                .putBoolean("is_logged_in", true)
                .putBoolean("is_admin", false)
                .apply();
            Toast.makeText(getContext(), "Login successful!", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(requireView()).navigate(R.id.homeFragment);
        } else if (email.equals("demo@bookstore.com") && password.equals("demo123")) {
            // Tài khoản demo
            sharedPreferences.edit()
                .putBoolean("is_logged_in", true)
                .putBoolean("is_admin", false)
                .putString("user_email", email)
                .putString("user_name", "Demo User")
                .apply();
            Toast.makeText(getContext(), "Welcome back!", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(requireView()).navigate(R.id.homeFragment);
        } else {
            Toast.makeText(getContext(), "Invalid email or password", Toast.LENGTH_SHORT).show();
        }
    }
}
