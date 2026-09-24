package com.quizsphere.app.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.quizsphere.app.R;
import com.quizsphere.app.adapters.QuestionReviewAdapter;
import com.quizsphere.app.database.QuizSphereDbHelper;
import com.quizsphere.app.models.AttemptAnswer;
import com.quizsphere.app.models.QuizAttempt;

import java.util.List;

/**
 * ==============================================================================
 * Activity: QuizResultActivity
 * Description: Instant evaluation and detailed review screen (Slide 6 & 8).
 * Features:
 *   - Visual score & percentage display
 *   - Passed / Failed status badge
 *   - Category rank
 *   - Question-by-question audit review via QuestionReviewAdapter
 *   - Quick navigation to Leaderboard and Dashboard
 * ==============================================================================
 */
public class QuizResultActivity extends AppCompatActivity {

    private QuizSphereDbHelper dbHelper;
    private QuizAttempt attempt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Examination Result");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dbHelper = QuizSphereDbHelper.getInstance(this);

        int attemptId = getIntent().getIntExtra("attempt_id", -1);
        attempt = dbHelper.getAttemptById(attemptId);

        if (attempt == null) {
            Toast.makeText(this, "Result record not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Fetch question answers for audit review
        List<AttemptAnswer> answers = dbHelper.getAttemptAnswers(attemptId);

        // Calculate rank in this category
        List<QuizAttempt> categoryLeaderboard = dbHelper.getLeaderboard(attempt.getCategoryId());
        int rank = 1;
        for (int i = 0; i < categoryLeaderboard.size(); i++) {
            if (categoryLeaderboard.get(i).getId() == attempt.getId()) {
                rank = i + 1;
                break;
            }
        }

        // Bind UI Elements
        TextView tvScore = findViewById(R.id.tvResScore);
        TextView tvPercentage = findViewById(R.id.tvResPercentage);
        TextView tvStatusBadge = findViewById(R.id.tvResStatusBadge);
        TextView tvTimeTaken = findViewById(R.id.tvResTimeTaken);
        TextView tvRank = findViewById(R.id.tvResRank);
        TextView tvCategory = findViewById(R.id.tvResCategory);

        Button btnRetake = findViewById(R.id.btnResRetake);
        Button btnLeaderboard = findViewById(R.id.btnResLeaderboard);
        Button btnDashboard = findViewById(R.id.btnResDashboard);

        RecyclerView rvReview = findViewById(R.id.rvResReview);
        rvReview.setLayoutManager(new LinearLayoutManager(this));
        rvReview.setAdapter(new QuestionReviewAdapter(this, answers));

        // Populate values
        tvCategory.setText(attempt.getCategoryName());
        tvScore.setText(attempt.getScore() + " / " + attempt.getTotalMarks());
        tvPercentage.setText(String.format("%.1f%%", attempt.getPercentage()));
        tvTimeTaken.setText("⏱ " + attempt.getTimeTakenSeconds() + " seconds");
        tvRank.setText("🏆 Category Rank: #" + rank);

        if (attempt.isPassed()) {
            tvStatusBadge.setText("EXAMINATION PASSED");
            tvStatusBadge.setTextColor(Color.parseColor("#10b981")); // Emerald green
            tvStatusBadge.setBackgroundColor(Color.parseColor("#ecfdf5"));
        } else {
            tvStatusBadge.setText("PASSING MARK NOT MET");
            tvStatusBadge.setTextColor(Color.parseColor("#ef4444")); // Red
            tvStatusBadge.setBackgroundColor(Color.parseColor("#fef2f2"));
        }

        // Button listeners
        btnRetake.setOnClickListener(v -> {
            Intent intent = new Intent(QuizResultActivity.this, QuizDetailActivity.class);
            intent.putExtra("category_id", attempt.getCategoryId());
            startActivity(intent);
            finish();
        });

        btnLeaderboard.setOnClickListener(v -> {
            Intent intent = new Intent(QuizResultActivity.this, LeaderboardActivity.class);
            intent.putExtra("selected_category_id", attempt.getCategoryId());
            startActivity(intent);
        });

        btnDashboard.setOnClickListener(v -> {
            Intent intent = new Intent(QuizResultActivity.this, DashboardActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
