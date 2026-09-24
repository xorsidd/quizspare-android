package com.quizsphere.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.quizsphere.app.R;
import com.quizsphere.app.adapters.CategoryAdapter;
import com.quizsphere.app.database.QuizSphereDbHelper;
import com.quizsphere.app.models.Category;
import com.quizsphere.app.models.User;
import com.quizsphere.app.utils.SessionManager;

import java.util.List;

/**
 * ==============================================================================
 * Activity: MainActivity
 * Description: Student home dashboard for QuizSphere.
 * Displays:
 *   - Current student greeting
 *   - Platform statistics metrics
 *   - Slide 2 Problem vs Solution banner
 *   - Slide 2 Sample Question preview
 *   - Available subject quizzes
 * ==============================================================================
 */
public class MainActivity extends AppCompatActivity {

    private QuizSphereDbHelper dbHelper;
    private SessionManager sessionManager;
    private User currentUser;

    private TextView tvWelcome;
    private TextView tvStatCategories, tvStatQuestions, tvStatAttempts;
    private RecyclerView rvCategories;
    private CategoryAdapter categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = QuizSphereDbHelper.getInstance(this);
        sessionManager = new SessionManager(this);
        currentUser = sessionManager.getCurrentUser();

        // Bind UI Views
        tvWelcome = findViewById(R.id.tvMainWelcome);
        tvStatCategories = findViewById(R.id.tvStatCategories);
        tvStatQuestions = findViewById(R.id.tvStatQuestions);
        tvStatAttempts = findViewById(R.id.tvStatAttempts);
        rvCategories = findViewById(R.id.rvMainCategories);

        Button btnViewAllCategories = findViewById(R.id.btnMainViewAll);
        Button btnViewLeaderboard = findViewById(R.id.btnMainLeaderboard);
        Button btnViewPresentation = findViewById(R.id.btnMainPresentation);

        // Greeting
        if (currentUser != null) {
            tvWelcome.setText("Welcome, " + currentUser.getUsername() + "!");
        }

        // Setup Categories RecyclerView
        rvCategories.setLayoutManager(new LinearLayoutManager(this));
        loadCategories();

        // Button listeners
        btnViewAllCategories.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CategoryListActivity.class);
            startActivity(intent);
        });

        btnViewLeaderboard.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LeaderboardActivity.class);
            startActivity(intent);
        });

        btnViewPresentation.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PresentationShowcaseActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCategories();
    }

    private void loadCategories() {
        List<Category> categories = dbHelper.getAllCategories();
        categoryAdapter = new CategoryAdapter(this, categories);
        rvCategories.setAdapter(categoryAdapter);

        int totalCategories = categories.size();
        int totalQuestions = 0;
        for (Category c : categories) {
            totalQuestions += c.getQuestionCount();
        }
        int totalAttempts = dbHelper.getAllAttempts().size();

        tvStatCategories.setText(String.valueOf(totalCategories));
        tvStatQuestions.setText(String.valueOf(totalQuestions));
        tvStatAttempts.setText(String.valueOf(totalAttempts));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_dashboard) {
            startActivity(new Intent(this, DashboardActivity.class));
            return true;
        } else if (id == R.id.action_leaderboard) {
            startActivity(new Intent(this, LeaderboardActivity.class));
            return true;
        } else if (id == R.id.action_presentation) {
            startActivity(new Intent(this, PresentationShowcaseActivity.class));
            return true;
        } else if (id == R.id.action_logout) {
            confirmLogout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void confirmLogout() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to sign out of QuizSphere?")
                .setPositiveButton("Logout", (dialog, which) -> {
                    sessionManager.logout();
                    Toast.makeText(MainActivity.this, "You have been logged out.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
