package com.example.bookstore.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore.R;
import com.example.bookstore.adapters.AddressAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class AddressFragment extends Fragment {
    private RecyclerView addressRecycler;
    private EditText addressInput, descriptionInput;
    private Button addBtn;
    private SharedPreferences sharedPreferences;
    private List<Address> addresses = new ArrayList<>();
    private AddressAdapter adapter;
    private final Gson gson = new Gson();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_address, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = requireActivity().getSharedPreferences("BookstorePrefs", Context.MODE_PRIVATE);

        addressRecycler = view.findViewById(R.id.address_recycler);
        addressInput = view.findViewById(R.id.address_input);
        descriptionInput = view.findViewById(R.id.address_description_input);
        addBtn = view.findViewById(R.id.add_address_btn);

        loadAddresses();

        addressRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AddressAdapter(addresses, this::deleteAddress, this::setDefault);
        addressRecycler.setAdapter(adapter);

        addBtn.setOnClickListener(v -> addNewAddress());
    }

    private void addNewAddress() {
        String address = addressInput.getText().toString().trim();
        String description = descriptionInput.getText().toString().trim();

        if (address.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập địa chỉ", Toast.LENGTH_SHORT).show();
            return;
        }

        Address newAddress = new Address(address, description.isEmpty() ? "Địa chỉ khác" : description);
        addresses.add(newAddress);
        adapter.notifyItemInserted(addresses.size() - 1);

        saveAddresses();
        addressInput.setText("");
        descriptionInput.setText("");
        Toast.makeText(getContext(), "Địa chỉ đã thêm", Toast.LENGTH_SHORT).show();
    }

    private void deleteAddress(int position) {
        addresses.remove(position);
        adapter.notifyItemRemoved(position);
        saveAddresses();
        Toast.makeText(getContext(), "Địa chỉ đã xóa", Toast.LENGTH_SHORT).show();
    }

    private void setDefault(int position) {
        for (int i = 0; i < addresses.size(); i++) {
            addresses.get(i).isDefault = (i == position);
        }
        adapter.notifyDataSetChanged();
        saveAddresses();
        Toast.makeText(getContext(), "Đặt làm địa chỉ mặc định", Toast.LENGTH_SHORT).show();
    }

    private void loadAddresses() {
        String json = sharedPreferences.getString("user_addresses", "");
        if (!json.isEmpty()) {
            addresses = gson.fromJson(json, new TypeToken<List<Address>>() {}.getType());
        } else {
            addresses = new ArrayList<>();
        }
    }

    private void saveAddresses() {
        String json = gson.toJson(addresses);
        sharedPreferences.edit().putString("user_addresses", json).apply();
    }

    public static class Address {
        public String address;
        public String description;
        public boolean isDefault;

        public Address(String address, String description) {
            this.address = address;
            this.description = description;
            this.isDefault = false;
        }
    }
}

