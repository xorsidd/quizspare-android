package com.quizsphere.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.quizsphere.app.R;
import com.quizsphere.app.database.QuizSphereDbHelper;
import com.quizsphere.app.models.Category;
import com.quizsphere.app.models.Question;
import com.quizsphere.app.models.QuizAttempt;
import com.quizsphere.app.models.User;
import com.quizsphere.app.utils.SessionManager;

import java.util.List;

/**
 * ==============================================================================
 * Activity: QuizDetailActivity
 * Description: Pre-quiz examination briefing screen.
 * Displays:
 *   - Category overview & topics
 *   - Duration countdown limit in minutes
 *   - Total question count & maximum points
 *   - Pass percentage mark
 *   - Examination rules and instructions
 *   - "Begin Timed Quiz" action trigger
 * ==============================================================================
 */
public class QuizDetailActivity extends AppCompatActivity {

    private QuizSphereDbHelper dbHelper;
    private Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dbHelper = QuizSphereDbHelper.getInstance(this);
        SessionManager sessionManager = new SessionManager(this);
        User currentUser = sessionManager.getCurrentUser();

        int categoryId = getIntent().getIntExtra("category_id", -1);
        category = dbHelper.getCategoryById(categoryId);

        if (category == null) {
            Toast.makeText(this, "Category not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(category.getName());
        }

        // Fetch questions to compute total marks
        List<Question> questions = dbHelper.getQuestionsForCategory(category.getId());
        int totalMarks = 0;
        for (Question q : questions) {
            totalMarks += q.getMarks();
        }

        // Bind UI Elements
        TextView tvTitle = findViewById(R.id.tvDetailTitle);
        TextView tvDescription = findViewById(R.id.tvDetailDesc);
        TextView tvQuestionsCount = findViewById(R.id.tvDetailQuestions);
        TextView tvTimeLimit = findViewById(R.id.tvDetailTime);
        TextView tvTotalMarks = findViewById(R.id.tvDetailMarks);
        TextView tvPassPct = findViewById(R.id.tvDetailPassPct);
        TextView tvBestScore = findViewById(R.id.tvDetailBestScore);
        Button btnStartQuiz = findViewById(R.id.btnDetailStart);

        tvTitle.setText(category.getName());
        tvDescription.setText(category.getDescription());
        tvQuestionsCount.setText(String.valueOf(questions.size()));
        tvTimeLimit.setText(category.getTimeLimitMins() + "m");
        tvTotalMarks.setText(String.valueOf(totalMarks));
        tvPassPct.setText(category.getPassPercentage() + "%");

        // Inspect previous best attempt if candidate already attempted this quiz
        if (currentUser != null) {
            List<QuizAttempt> userAttempts = dbHelper.getUserAttempts(currentUser.getId());
            double bestPct = -1;
            for (QuizAttempt att : userAttempts) {
                if (att.getCategoryId() == category.getId() && att.getPercentage() > bestPct) {
                    bestPct = att.getPercentage();
                }
            }
            if (bestPct >= 0) {
                tvBestScore.setVisibility(View.VISIBLE);
                tvBestScore.setText("🏆 Your Previous Best: " + String.format("%.1f%%", bestPct));
            } else {
                tvBestScore.setVisibility(View.GONE);
            }
        }

        // Start Quiz
        btnStartQuiz.setOnClickListener(v -> {
            if (questions.isEmpty()) {
                Toast.makeText(QuizDetailActivity.this, "This category currently has no published questions.", Toast.LENGTH_LONG).show();
                return;
            }
            Intent intent = new Intent(QuizDetailActivity.this, QuizTakeActivity.class);
            intent.putExtra("category_id", category.getId());
            startActivity(intent);
            finish();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
