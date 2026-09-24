package com.quizsphere.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quizsphere.app.R;
import com.quizsphere.app.activities.QuizResultActivity;
import com.quizsphere.app.models.QuizAttempt;

import java.util.List;

/**
 * ==============================================================================
 * Adapter: AttemptHistoryAdapter
 * Description: RecyclerView Adapter presenting candidate's historical attempts (Slide 6 & 9).
 * Shows category name, timestamp, score, pass/fail badge, and "Review" button.
 * ==============================================================================
 */
public class AttemptHistoryAdapter extends RecyclerView.Adapter<AttemptHistoryAdapter.HistoryViewHolder> {

    private final Context context;
    private final List<QuizAttempt> attemptList;

    public AttemptHistoryAdapter(Context context, List<QuizAttempt> attemptList) {
        this.context = context;
        this.attemptList = attemptList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_attempt_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        QuizAttempt item = attemptList.get(position);

        holder.tvCategory.setText(item.getCategoryName());
        holder.tvDate.setText(item.getDateTaken());
        holder.tvScore.setText(item.getScore() + " / " + item.getTotalMarks() + " (" + String.format("%.1f", item.getPercentage()) + "%)");
        holder.tvTimeTaken.setText("⏱ " + item.getTimeTakenSeconds() + "s");

        if (item.isPassed()) {
            holder.tvStatusBadge.setText("PASSED");
            holder.tvStatusBadge.setTextColor(Color.parseColor("#10b981"));
            holder.tvStatusBadge.setBackgroundColor(Color.parseColor("#ecfdf5"));
        } else {
            holder.tvStatusBadge.setText("FAILED");
            holder.tvStatusBadge.setTextColor(Color.parseColor("#ef4444"));
            holder.tvStatusBadge.setBackgroundColor(Color.parseColor("#fef2f2"));
        }

        holder.btnReview.setOnClickListener(v -> {
            Intent intent = new Intent(context, QuizResultActivity.class);
            intent.putExtra("attempt_id", item.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return attemptList != null ? attemptList.size() : 0;
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory, tvDate, tvScore, tvStatusBadge, tvTimeTaken;
        Button btnReview;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tvHistCategory);
            tvDate = itemView.findViewById(R.id.tvHistDate);
            tvScore = itemView.findViewById(R.id.tvHistScore);
            tvStatusBadge = itemView.findViewById(R.id.tvHistStatusBadge);
            tvTimeTaken = itemView.findViewById(R.id.tvHistTimeTaken);
            btnReview = itemView.findViewById(R.id.btnHistReview);
        }
    }
}
