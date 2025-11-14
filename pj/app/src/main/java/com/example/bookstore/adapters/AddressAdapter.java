package com.example.bookstore.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore.R;
import com.example.bookstore.ui.fragments.AddressFragment;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {
    private List<AddressFragment.Address> addresses;
    private OnDeleteClick deleteListener;
    private OnSetDefaultClick defaultListener;

    public interface OnDeleteClick {
        void onDelete(int position);
    }

    public interface OnSetDefaultClick {
        void onSetDefault(int position);
    }

    public AddressAdapter(List<AddressFragment.Address> addresses, OnDeleteClick deleteListener, OnSetDefaultClick defaultListener) {
        this.addresses = addresses;
        this.deleteListener = deleteListener;
        this.defaultListener = defaultListener;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address, parent, false);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        AddressFragment.Address address = addresses.get(position);
        holder.addressText.setText(address.address);
        holder.descriptionText.setText(address.description);
        holder.defaultRadio.setChecked(address.isDefault);

        holder.defaultRadio.setOnClickListener(v -> {
            if (defaultListener != null) {
                defaultListener.onSetDefault(position);
            }
        });

        holder.deleteBtn.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDelete(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return addresses.size();
    }

    static class AddressViewHolder extends RecyclerView.ViewHolder {
        TextView addressText, descriptionText;
        RadioButton defaultRadio;
        Button deleteBtn;

        AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            addressText = itemView.findViewById(R.id.address_item_text);
            descriptionText = itemView.findViewById(R.id.address_description_text);
            defaultRadio = itemView.findViewById(R.id.address_default_radio);
            deleteBtn = itemView.findViewById(R.id.address_delete_btn);
        }
    }
}

