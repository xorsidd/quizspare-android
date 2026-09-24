package com.quizsphere.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.quizsphere.app.R;
import com.quizsphere.app.adapters.AttemptHistoryAdapter;
import com.quizsphere.app.database.QuizSphereDbHelper;
import com.quizsphere.app.models.QuizAttempt;
import com.quizsphere.app.models.User;
import com.quizsphere.app.utils.SessionManager;

import java.util.List;

/**
 * ==============================================================================
 * Activity: DashboardActivity
 * Description: Personal student dashboard and attempt history (Slide 6 & 9).
 * Features:
 *   - Overall attempt count and passed ratio
 *   - Personal pass rate and highest score percentages
 *   - Chronological attempt audit log with quick-review links
 * ==============================================================================
 */
public class DashboardActivity extends AppCompatActivity {

    private QuizSphereDbHelper dbHelper;
    private User currentUser;

    private TextView tvUsername, tvTotalAttempts, tvPassedAttempts, tvPassRate, tvHighestScore, tvEmptyHistory;
    private RecyclerView rvHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Student Dashboard");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dbHelper = QuizSphereDbHelper.getInstance(this);
        SessionManager sessionManager = new SessionManager(this);
        currentUser = sessionManager.getCurrentUser();

        tvUsername = findViewById(R.id.tvDashUsername);
        tvTotalAttempts = findViewById(R.id.tvDashTotalAttempts);
        tvPassedAttempts = findViewById(R.id.tvDashPassedAttempts);
        tvPassRate = findViewById(R.id.tvDashPassRate);
        tvHighestScore = findViewById(R.id.tvDashHighestScore);
        tvEmptyHistory = findViewById(R.id.tvDashEmpty);
        rvHistory = findViewById(R.id.rvDashHistory);
        Button btnNewQuiz = findViewById(R.id.btnDashNewQuiz);

        rvHistory.setLayoutManager(new LinearLayoutManager(this));

        if (currentUser != null) {
            tvUsername.setText("Candidate: " + currentUser.getUsername());
        }

        btnNewQuiz.setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, CategoryListActivity.class));
            finish();
        });

        loadDashboardData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDashboardData();
    }

    private void loadDashboardData() {
        if (currentUser == null) return;

        List<QuizAttempt> attempts = dbHelper.getUserAttempts(currentUser.getId());

        int total = attempts.size();
        int passedCount = 0;
        double highest = 0.0;
        double sumPct = 0.0;

        for (QuizAttempt att : attempts) {
            if (att.isPassed()) passedCount++;
            if (att.getPercentage() > highest) highest = att.getPercentage();
            sumPct += att.getPercentage();
        }

        double passRate = (total > 0) ? ((double) passedCount / total * 100.0) : 0.0;

        tvTotalAttempts.setText(String.valueOf(total));
        tvPassedAttempts.setText(String.valueOf(passedCount));
        tvPassRate.setText(String.format("%.1f%%", passRate));
        tvHighestScore.setText(String.format("%.1f%%", highest));

        if (attempts.isEmpty()) {
            tvEmptyHistory.setVisibility(View.VISIBLE);
            rvHistory.setVisibility(View.GONE);
        } else {
            tvEmptyHistory.setVisibility(View.GONE);
            rvHistory.setVisibility(View.VISIBLE);
            rvHistory.setAdapter(new AttemptHistoryAdapter(this, attempts));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
