package com.quizsphere.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.quizsphere.app.R;
import com.quizsphere.app.utils.SessionManager;

/**
 * ==============================================================================
 * Activity: SplashActivity
 * Description: Application launch screen.
 * Displays QuizSphere branding, subtitle ("A DJANGO + PYTHON WEB APPLICATION"),
 * and official team credits (Roll No: 40013, 40003, 40043).
 * Checks session state to redirect to MainActivity or LoginActivity.
 * ==============================================================================
 */
public class SplashActivity extends AppCompatActivity {

    // Duration the splash screen remains visible (in milliseconds)
    private static final int SPLASH_DURATION = 1800;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Hide action bar for full splash experience
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Initialize session manager to inspect authentication state
        SessionManager sessionManager = new SessionManager(this);

        // Delay transition by 1.8 seconds to allow branding to render
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent;
            if (sessionManager.isLoggedIn()) {
                // User is already signed in -> route to Main Dashboard
                intent = new Intent(SplashActivity.this, MainActivity.class);
            } else {
                // User needs authentication -> route to Login
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            }
            startActivity(intent);
            finish(); // Close splash screen so user cannot back into it
        }, SPLASH_DURATION);
    }
}
