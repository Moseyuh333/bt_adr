package com.example.bookstore.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bookstore.R;
import com.example.bookstore.adapters.AdminCustomerAdapter;
import com.example.bookstore.models.User;
import java.util.ArrayList;
import java.util.List;

public class AdminCustomersFragment extends Fragment {
    private RecyclerView recyclerView;
    private AdminCustomerAdapter adapter;
    private EditText searchInput;
    private Button backBtn;
    private List<User> allCustomers, filteredCustomers;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_customers, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.admin_customers_recycler);
        searchInput = view.findViewById(R.id.search_customer_input);
        backBtn = view.findViewById(R.id.back_btn);
        allCustomers = loadCustomers();
        filteredCustomers = new ArrayList<>(allCustomers);
        adapter = new AdminCustomerAdapter(filteredCustomers, this::onCustomerClick);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        backBtn.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());
        searchInput.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) { filterCustomers(s.toString()); }
            public void afterTextChanged(Editable s) {}
        });
    }

    private List<User> loadCustomers() {
        List<User> customers = new ArrayList<>();
        SharedPreferences prefs = requireActivity().getSharedPreferences("BookstorePrefs", Context.MODE_PRIVATE);
        String savedEmail = prefs.getString("user_email", "");
        String savedName = prefs.getString("user_name", "");
        if (!savedEmail.isEmpty() && !savedEmail.equals("admin@bookstore.com")) {
            customers.add(new User(1, savedName.isEmpty() ? "User" : savedName, savedEmail, "customer", false));
        }
        customers.add(new User(2, "Demo User", "demo@bookstore.com", "customer", false));
        customers.add(new User(3, "Nguyễn Văn A", "nguyenvana@gmail.com", "customer", false));
        customers.add(new User(4, "Trần Thị B", "tranthib@gmail.com", "customer", false));
        customers.add(new User(5, "Lê Văn C", "levanc@gmail.com", "customer", true));
        return customers;
    }

    private void filterCustomers(String query) {
        filteredCustomers.clear();
        if (query.isEmpty()) {
            filteredCustomers.addAll(allCustomers);
        } else {
            String lowerQuery = query.toLowerCase();
            for (User user : allCustomers) {
                if (user.name.toLowerCase().contains(lowerQuery) || user.email.toLowerCase().contains(lowerQuery)) {
                    filteredCustomers.add(user);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void onCustomerClick(User user) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        Navigation.findNavController(requireView()).navigate(R.id.adminCustomerDetailFragment, bundle);
    }
}

