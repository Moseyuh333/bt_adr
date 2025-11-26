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
        try {
            String category = categories.get(position);

            // Validate category - clean and ensure it's valid
            if (category == null || category.isEmpty()) {
                category = "Sách";
            }

            // category = danh mục (Category name) - hiển thị tên danh mục rõ ràng
            String cleanCategory = category.replaceAll("<[^>]*>", "").trim();
            if (cleanCategory.isEmpty()) {
                cleanCategory = "Sách";
            }
            if (cleanCategory.length() > 50) {
                cleanCategory = cleanCategory.substring(0, 47) + "...";
            }

            holder.categoryName.setText(cleanCategory);

            // Set icon based on category
            String icon = getCategoryIcon(category);
            holder.categoryIcon.setText(icon);

            // Highlight selected category
            if (position == selectedPosition) {
                holder.itemView.setBackgroundResource(R.color.amber_600);
                holder.categoryName.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.black));
                holder.categoryIcon.setAlpha(1.0f);
            } else {
                holder.itemView.setBackgroundResource(R.color.amber_50);
                holder.categoryName.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.amber_800));
                holder.categoryIcon.setAlpha(0.8f);
            }

            final String finalCategory = category; // Make final for lambda use
            holder.itemView.setOnClickListener(v -> {
                int oldPosition = selectedPosition;
                selectedPosition = holder.getAdapterPosition();
                notifyItemChanged(oldPosition);
                notifyItemChanged(selectedPosition);
                listener.onCategoryClick(finalCategory);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getCategoryIcon(String category) {
        switch (category.toLowerCase()) {
            // Vietnamese categories
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

            // English categories
            case "all":
            case "tất cả": return "📚";
            case "fiction":
            case "tiểu thuyết": return "📖";
            case "fantasy":
            case "kỳ ảo": return "🧙";
            case "science fiction":
            case "sci-fi":
            case "khoa học viễn tưởng": return "🚀";
            case "romance":
            case "lãng mạn": return "💕";
            case "mystery":
            case "bí ẩn": return "🔍";
            case "thriller":
            case "giật gân": return "😱";
            case "horror":
            case "kinh dị": return "👻";
            case "biography":
            case "tiểu sử": return "👤";
            case "history":
            case "historical": return "🏛️";
            case "self-help":
            case "tự trợ": return "💪";
            case "business":
            case "kinh doanh": return "💼";
            case "finance":
            case "tài chính": return "💰";
            case "psychology": return "🧠";
            case "philosophy":
            case "triết học": return "🤔";
            case "religion":
            case "tôn giáo": return "⛪";
            case "science": return "🔬";
            case "technology": return "💻";
            case "cooking":
            case "nấu ăn": return "🍳";
            case "travel": return "✈️";
            case "art": return "🎨";
            case "music":
            case "âm nhạc": return "🎵";
            case "sports":
            case "thể thao": return "⚽";
            case "health":
            case "sức khỏe": return "🏥";
            case "children": return "🧸";
            case "young adult":
            case "thanh thiếu niên": return "👦";
            case "poetry":
            case "thơ ca": return "✍️";
            case "drama":
            case "kịch": return "🎭";
            case "comics":
            case "manga":
            case "truyện tranh": return "📰";
            case "education": return "🎓";
            case "reference":
            case "tham khảo": return "📚";
            case "dystopian": return "🌆";
            case "gothic": return "🏰";

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

