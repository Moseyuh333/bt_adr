package com.example.bookstore.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.bookstore.R;
import com.example.bookstore.models.User;

public class ProfileFragment extends Fragment {
    private EditText nameEdit, emailEdit, phoneEdit, addressEdit;
    private Button editBtn, saveBtn, logoutBtn;
    private User user;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = requireActivity().getSharedPreferences("BookstorePrefs", Context.MODE_PRIVATE);

        try {
            nameEdit = view.findViewById(R.id.name_edit_text);
            emailEdit = view.findViewById(R.id.email_edit_text);
            phoneEdit = view.findViewById(R.id.phone_edit_text);
            addressEdit = view.findViewById(R.id.address_edit_text);
            editBtn = view.findViewById(R.id.edit_btn);
            saveBtn = view.findViewById(R.id.save_btn);
            logoutBtn = view.findViewById(R.id.logout_btn);

            if (nameEdit != null && editBtn != null && saveBtn != null) {
                // Load user data from SharedPreferences
                String name = sharedPreferences.getString("user_name", "John Doe");
                String email = sharedPreferences.getString("user_email", "user@bookstore.com");
                String phone = sharedPreferences.getString("user_phone", "123456789");
                String address = sharedPreferences.getString("user_address", "123 Main St");

                user = new User(name, email, phone, address);
                nameEdit.setText(user.name);
                emailEdit.setText(user.email);
                phoneEdit.setText(user.phone);
                addressEdit.setText(user.address);

                setEditMode(false);

                editBtn.setOnClickListener(v -> setEditMode(true));

                saveBtn.setOnClickListener(v -> {
                    user.name = nameEdit.getText().toString();
                    user.phone = phoneEdit.getText().toString();
                    user.address = addressEdit.getText().toString();

                    // Save to SharedPreferences
                    sharedPreferences.edit()
                        .putString("user_name", user.name)
                        .putString("user_phone", user.phone)
                        .putString("user_address", user.address)
                        .apply();

                    setEditMode(false);
                });

                // Logout button
                if (logoutBtn != null) {
                    logoutBtn.setOnClickListener(v -> {
                        // Clear login status
                        sharedPreferences.edit()
                            .putBoolean("is_logged_in", false)
                            .apply();

                        // Navigate to login screen
                        Navigation.findNavController(v).navigate(R.id.loginFragment);
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setEditMode(boolean edit) {
        try {
            if (nameEdit != null) nameEdit.setEnabled(edit);
            if (phoneEdit != null) phoneEdit.setEnabled(edit);
            if (addressEdit != null) addressEdit.setEnabled(edit);
            if (editBtn != null) editBtn.setVisibility(edit ? View.GONE : View.VISIBLE);
            if (saveBtn != null) saveBtn.setVisibility(edit ? View.VISIBLE : View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

