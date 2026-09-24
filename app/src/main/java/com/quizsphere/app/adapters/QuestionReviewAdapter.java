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
import com.quizsphere.app.models.AttemptAnswer;

import java.util.List;

/**
 * ==============================================================================
 * Adapter: QuestionReviewAdapter
 * Description: RecyclerView Adapter presenting post-quiz answer audit review.
 * Highlights:
 *   - Correct answers in emerald green
 *   - User's incorrect choice in soft red
 *   - Marks obtained per question
 *   - Helpful technical explanation
 * ==============================================================================
 */
public class QuestionReviewAdapter extends RecyclerView.Adapter<QuestionReviewAdapter.ReviewViewHolder> {

    private final Context context;
    private final List<AttemptAnswer> answerList;

    public QuestionReviewAdapter(Context context, List<AttemptAnswer> answerList) {
        this.context = context;
        this.answerList = answerList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_question_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        AttemptAnswer ans = answerList.get(position);

        // Header: Q# and Correctness Badge
        holder.tvQuestionNumber.setText("Question " + (position + 1));
        if (ans.isCorrect()) {
            holder.tvStatusBadge.setText("✓ Correct (+" + ans.getMarksObtained() + " mark)");
            holder.tvStatusBadge.setTextColor(Color.parseColor("#10b981")); // Green
            holder.tvStatusBadge.setBackgroundColor(Color.parseColor("#ecfdf5"));
        } else {
            holder.tvStatusBadge.setText("✗ Incorrect (0/" + ans.getMaxMarks() + ")");
            holder.tvStatusBadge.setTextColor(Color.parseColor("#ef4444")); // Red
            holder.tvStatusBadge.setBackgroundColor(Color.parseColor("#fef2f2"));
        }

        // Question Prompt
        holder.tvQuestionText.setText(ans.getQuestionText());

        // Student's Pick
        if (ans.getSelectedOptionText() != null && !ans.getSelectedOptionText().isEmpty()) {
            holder.tvYourAnswer.setText("Your Choice: " + ans.getSelectedOptionText());
            holder.tvYourAnswer.setTextColor(ans.isCorrect() ? Color.parseColor("#10b981") : Color.parseColor("#ef4444"));
        } else {
            holder.tvYourAnswer.setText("Your Choice: (No answer submitted)");
            holder.tvYourAnswer.setTextColor(Color.parseColor("#ef4444"));
        }

        // Correct Answer
        holder.tvCorrectAnswer.setText("Correct Answer: " + ans.getCorrectOptionText());

        // Explanation (if present)
        if (ans.getExplanation() != null && !ans.getExplanation().trim().isEmpty()) {
            holder.tvExplanation.setVisibility(View.VISIBLE);
            holder.tvExplanation.setText("💡 Explanation: " + ans.getExplanation());
        } else {
            holder.tvExplanation.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return answerList != null ? answerList.size() : 0;
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestionNumber, tvStatusBadge, tvQuestionText, tvYourAnswer, tvCorrectAnswer, tvExplanation;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestionNumber = itemView.findViewById(R.id.tvReviewQNum);
            tvStatusBadge = itemView.findViewById(R.id.tvReviewStatusBadge);
            tvQuestionText = itemView.findViewById(R.id.tvReviewQText);
            tvYourAnswer = itemView.findViewById(R.id.tvReviewYourAnswer);
            tvCorrectAnswer = itemView.findViewById(R.id.tvReviewCorrectAnswer);
            tvExplanation = itemView.findViewById(R.id.tvReviewExplanation);
        }
    }
}
