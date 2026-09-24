package com.quizsphere.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quizsphere.app.R;
import com.quizsphere.app.activities.QuizDetailActivity;
import com.quizsphere.app.models.Category;

import java.util.List;

/**
 * ==============================================================================
 * Adapter: CategoryAdapter
 * Description: RecyclerView Adapter presenting quiz categories in clean cards.
 * Shows title, overview description, question count, time limit, and pass threshold.
 * ==============================================================================
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    // Context for starting activities and inflating layouts
    private final Context context;

    // List of categories to display
    private List<Category> categoryList;

    // Constructor initializing adapter with context and dataset
    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    /**
     * Updates dataset when search query or filter changes.
     */
    public void updateData(List<Category> newList) {
        this.categoryList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate single category card XML layout
        View view = LayoutInflater.from(context).inflate(R.layout.item_category_card, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        // Fetch category at position
        Category category = categoryList.get(position);

        // Populate card views
        holder.tvName.setText(category.getName());
        holder.tvDescription.setText(category.getDescription());
        holder.tvTimeLimit.setText(category.getTimeLimitMins() + " mins");
        holder.tvQuestionCount.setText(category.getQuestionCount() + " Questions");
        holder.tvPassPct.setText("Pass: " + category.getPassPercentage() + "%");

        // Handle card click to open pre-quiz briefing
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, QuizDetailActivity.class);
            intent.putExtra("category_id", category.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return categoryList != null ? categoryList.size() : 0;
    }

    /**
     * ViewHolder caching card view references for optimal scrolling performance.
     */
    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDescription, tvTimeLimit, tvQuestionCount, tvPassPct;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvCatName);
            tvDescription = itemView.findViewById(R.id.tvCatDesc);
            tvTimeLimit = itemView.findViewById(R.id.tvCatTimeLimit);
            tvQuestionCount = itemView.findViewById(R.id.tvCatQuestionCount);
            tvPassPct = itemView.findViewById(R.id.tvCatPassPct);
        }
    }
}
