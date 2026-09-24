package com.quizsphere.app.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.quizsphere.app.R;
import com.quizsphere.app.adapters.LeaderboardAdapter;
import com.quizsphere.app.database.QuizSphereDbHelper;
import com.quizsphere.app.models.Category;
import com.quizsphere.app.models.QuizAttempt;

import java.util.ArrayList;
import java.util.List;

/**
 * ==============================================================================
 * Activity: LeaderboardActivity
 * Description: Real-time competitive leaderboard rankings (Slide 6 & 9).
 * Features:
 *   - Global vs category-specific ranking filter
 *   - Gold, Silver, Bronze badges for top 3 candidates
 *   - High-score, percentage, and time spent metrics
 * ==============================================================================
 */
public class LeaderboardActivity extends AppCompatActivity {

    private QuizSphereDbHelper dbHelper;
    private Spinner spCategoryFilter;
    private RecyclerView rvLeaderboard;
    private TextView tvEmptyLeaderboard;
    private LeaderboardAdapter leaderboardAdapter;
    private List<Category> categories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Competitive Leaderboard");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dbHelper = QuizSphereDbHelper.getInstance(this);

        spCategoryFilter = findViewById(R.id.spLdrCategoryFilter);
        rvLeaderboard = findViewById(R.id.rvLeaderboardList);
        tvEmptyLeaderboard = findViewById(R.id.tvLdrEmpty);

        rvLeaderboard.setLayoutManager(new LinearLayoutManager(this));

        // Load Categories into spinner
        categories = dbHelper.getAllCategories();
        List<String> spinnerOptions = new ArrayList<>();
        spinnerOptions.add("Global (All Categories)");
        for (Category c : categories) {
            spinnerOptions.add(c.getName());
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerOptions);
        spCategoryFilter.setAdapter(spinnerAdapter);

        // Pre-select category if passed in intent
        int passedCatId = getIntent().getIntExtra("selected_category_id", -1);
        if (passedCatId != -1) {
            for (int i = 0; i < categories.size(); i++) {
                if (categories.get(i).getId() == passedCatId) {
                    spCategoryFilter.setSelection(i + 1);
                    break;
                }
            }
        }

        spCategoryFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int catId = 0;
                if (position > 0 && position - 1 < categories.size()) {
                    catId = categories.get(position - 1).getId();
                }
                loadLeaderboard(catId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Initial load
        loadLeaderboard(passedCatId > 0 ? passedCatId : 0);
    }

    private void loadLeaderboard(int categoryId) {
        List<QuizAttempt> attempts = dbHelper.getLeaderboard(categoryId);
        if (attempts.isEmpty()) {
            tvEmptyLeaderboard.setVisibility(View.VISIBLE);
            rvLeaderboard.setVisibility(View.GONE);
        } else {
            tvEmptyLeaderboard.setVisibility(View.GONE);
            rvLeaderboard.setVisibility(View.VISIBLE);
            if (leaderboardAdapter == null) {
                leaderboardAdapter = new LeaderboardAdapter(this, attempts);
                rvLeaderboard.setAdapter(leaderboardAdapter);
            } else {
                leaderboardAdapter.updateData(attempts);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
