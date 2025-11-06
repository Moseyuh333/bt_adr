package com.example.bookstore.ui.fragments;

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

public class ForgotPasswordFragment extends Fragment {

    private EditText emailInput;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        emailInput = view.findViewById(R.id.forgot_email);
        Button sendResetButton = view.findViewById(R.id.send_reset_btn);
        TextView backToLoginLink = view.findViewById(R.id.back_to_login_link);

        sendResetButton.setOnClickListener(v -> handlePasswordReset());

        backToLoginLink.setOnClickListener(v ->
            Navigation.findNavController(v).navigate(R.id.loginFragment)
        );

        return view;
    }

    private void handlePasswordReset() {
        String email = emailInput.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(getContext(), "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getContext(), "Please enter a valid email", Toast.LENGTH_SHORT).show();
            return;
        }

        // Giả lập gửi email reset password
        Toast.makeText(getContext(), "Reset link sent to " + email, Toast.LENGTH_LONG).show();

        // Quay về màn hình login sau 2 giây
        emailInput.postDelayed(() ->
            Navigation.findNavController(requireView()).navigate(R.id.loginFragment)
        , 2000);
    }
}

