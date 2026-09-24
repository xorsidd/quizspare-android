package com.quizsphere.app.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quizsphere.app.R;
import com.quizsphere.app.models.QuizAttempt;

import java.util.List;

/**
 * ==============================================================================
 * Adapter: LeaderboardAdapter
 * Description: RecyclerView Adapter presenting competitive ranking (Slide 6 & 9).
 * Highlights:
 *   - Gold 🥇 for Rank 1
 *   - Silver 🥈 for Rank 2
 *   - Bronze 🥉 for Rank 3
 *   - Score, percentage, elapsed time, and candidate username
 * ==============================================================================
 */
public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder> {

    private final Context context;
    private List<QuizAttempt> attemptList;

    public LeaderboardAdapter(Context context, List<QuizAttempt> attemptList) {
        this.context = context;
        this.attemptList = attemptList;
    }

    public void updateData(List<QuizAttempt> newList) {
        this.attemptList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_leaderboard_row, parent, false);
        return new LeaderboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardViewHolder holder, int position) {
        QuizAttempt item = attemptList.get(position);
        int rank = position + 1;

        // Rank Display with Medals
        if (rank == 1) {
            holder.tvRank.setText("🥇 1");
            holder.tvRank.setTextColor(Color.parseColor("#d97706")); // Gold
        } else if (rank == 2) {
            holder.tvRank.setText("🥈 2");
            holder.tvRank.setTextColor(Color.parseColor("#64748b")); // Silver
        } else if (rank == 3) {
            holder.tvRank.setText("🥉 3");
            holder.tvRank.setTextColor(Color.parseColor("#b45309")); // Bronze
        } else {
            holder.tvRank.setText("#" + rank);
            holder.tvRank.setTextColor(Color.parseColor("#475569"));
        }

        // Candidate Avatar & Name
        holder.tvAvatar.setText(item.getUsername().substring(0, 1).toUpperCase());
        holder.tvUsername.setText(item.getUsername());
        holder.tvCategory.setText(item.getCategoryName());

        // Score, Percentage, Time
        holder.tvScore.setText(item.getScore() + "/" + item.getTotalMarks());
        holder.tvPercentage.setText(String.format("%.1f%%", item.getPercentage()));
        holder.tvTimeTaken.setText(item.getTimeTakenSeconds() + "s");
        holder.tvDate.setText(item.getDateTaken());
    }

    @Override
    public int getItemCount() {
        return attemptList != null ? attemptList.size() : 0;
    }

    public static class LeaderboardViewHolder extends RecyclerView.ViewHolder {
        TextView tvRank, tvAvatar, tvUsername, tvCategory, tvScore, tvPercentage, tvTimeTaken, tvDate;

        public LeaderboardViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRank = itemView.findViewById(R.id.tvLdrRank);
            tvAvatar = itemView.findViewById(R.id.tvLdrAvatar);
            tvUsername = itemView.findViewById(R.id.tvLdrUsername);
            tvCategory = itemView.findViewById(R.id.tvLdrCategory);
            tvScore = itemView.findViewById(R.id.tvLdrScore);
            tvPercentage = itemView.findViewById(R.id.tvLdrPercentage);
            tvTimeTaken = itemView.findViewById(R.id.tvLdrTimeTaken);
            tvDate = itemView.findViewById(R.id.tvLdrDate);
        }
    }
}
