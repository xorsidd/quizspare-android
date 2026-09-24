package com.quizsphere.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.quizsphere.app.R;
import com.quizsphere.app.database.QuizSphereDbHelper;
import com.quizsphere.app.models.User;
import com.quizsphere.app.utils.SessionManager;

/**
 * ==============================================================================
 * Activity: RegisterActivity
 * Description: Student registration screen.
 * Allows new learners to join QuizSphere, take quizzes, and track scores.
 * ==============================================================================
 */
public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etEmail, etPassword, etConfirmPassword;
    private QuizSphereDbHelper dbHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Student Registration");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dbHelper = QuizSphereDbHelper.getInstance(this);
        sessionManager = new SessionManager(this);

        // Bind views
        etUsername = findViewById(R.id.etRegUsername);
        etEmail = findViewById(R.id.etRegEmail);
        etPassword = findViewById(R.id.etRegPassword);
        etConfirmPassword = findViewById(R.id.etRegConfirmPassword);
        Button btnRegister = findViewById(R.id.btnRegSubmit);
        TextView tvBackToLogin = findViewById(R.id.tvRegBackToLogin);

        btnRegister.setOnClickListener(v -> performRegistration());
        tvBackToLogin.setOnClickListener(v -> finish());
    }

    private void performRegistration() {
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Username is required");
            etUsername.requestFocus();
            return;
        }

        if (username.length() < 3) {
            etUsername.setError("Username must be at least 3 characters");
            etUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Valid email is required");
            etEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password) || password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            etPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            etConfirmPassword.requestFocus();
            return;
        }

        if (dbHelper.checkUsernameExists(username)) {
            etUsername.setError("This username is already taken");
            etUsername.requestFocus();
            return;
        }

        // Register as student
        User newUser = new User(username, email, password, "student");
        boolean success = dbHelper.registerUser(newUser);

        if (success) {
            User authenticatedUser = dbHelper.authenticateUser(username, password);
            if (authenticatedUser != null) {
                sessionManager.createLoginSession(authenticatedUser);
            }
            Toast.makeText(this, "Student account created successfully!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
