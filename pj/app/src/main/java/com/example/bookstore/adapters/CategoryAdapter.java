package com.example.bookstore.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<String> categories;
    private OnCategoryClickListener listener;
    private int selectedPosition = 0;

    public interface OnCategoryClickListener {
        void onCategoryClick(String category);
    }

    public CategoryAdapter(List<String> categories, OnCategoryClickListener listener) {
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        String category = categories.get(position);
        holder.categoryName.setText(category);

        // Set icon based on category
        String icon = getCategoryIcon(category);
        holder.categoryIcon.setText(icon);

        // Highlight selected category
        if (position == selectedPosition) {
            holder.itemView.setBackgroundResource(R.color.amber_600);
            holder.categoryName.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.white));
            holder.categoryIcon.setAlpha(1.0f);
        } else {
            holder.itemView.setBackgroundResource(R.color.amber_50);
            holder.categoryName.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.amber_800));
            holder.categoryIcon.setAlpha(0.8f);
        }

        holder.itemView.setOnClickListener(v -> {
            int oldPosition = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(oldPosition);
            notifyItemChanged(selectedPosition);
            listener.onCategoryClick(category);
        });
    }

    private String getCategoryIcon(String category) {
        switch (category.toLowerCase()) {
            case "văn học": return "📖";
            case "khoa học": return "🔬";
            case "kinh tế": return "💰";
            case "lịch sử": return "🏛️";
            case "thiếu nhi": return "🧸";
            case "kỹ năng": return "💡";
            case "tâm lý": return "🧠";
            case "giáo dục": return "🎓";
            case "nghệ thuật": return "🎨";
            case "công nghệ": return "💻";
            case "y học": return "⚕️";
            case "du lịch": return "✈️";
            default: return "📚";
        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName, categoryIcon;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.category_name);
            categoryIcon = itemView.findViewById(R.id.category_icon);
        }
    }
}

