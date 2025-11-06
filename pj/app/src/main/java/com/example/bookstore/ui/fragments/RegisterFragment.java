package com.example.bookstore.ui.fragments;

import android.content.Context;
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
import com.example.bookstore.R;

public class RegisterFragment extends Fragment {

    private EditText nameInput, emailInput, passwordInput, confirmPasswordInput;
    private Button registerButton;
    private TextView loginLink;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        sharedPreferences = requireActivity().getSharedPreferences("BookstorePrefs", Context.MODE_PRIVATE);

        nameInput = view.findViewById(R.id.register_name);
        emailInput = view.findViewById(R.id.register_email);
        passwordInput = view.findViewById(R.id.register_password);
        confirmPasswordInput = view.findViewById(R.id.register_confirm_password);
        registerButton = view.findViewById(R.id.register_btn);
        loginLink = view.findViewById(R.id.login_link);

        registerButton.setOnClickListener(v -> handleRegister());

        loginLink.setOnClickListener(v ->
            Navigation.findNavController(v).navigate(R.id.loginFragment)
        );

        return view;
    }

    private void handleRegister() {
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getContext(), "Please enter a valid email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(getContext(), "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lưu thông tin đăng ký
        sharedPreferences.edit()
            .putString("user_name", name)
            .putString("user_email", email)
            .putString("user_password", password)
            .putBoolean("is_logged_in", true)
            .apply();

        Toast.makeText(getContext(), "Registration successful!", Toast.LENGTH_SHORT).show();
        Navigation.findNavController(requireView()).navigate(R.id.homeFragment);
    }
}

