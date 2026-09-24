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
 * Activity: LoginActivity
 * Description: Student authentication screen.
 * Features:
 *   - Credentials validation against SQLite database
 *   - 1-Tap quick demo button for student testing ("student"/"student123")
 *   - Session persistence via SessionManager
 * ==============================================================================
 */
public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private QuizSphereDbHelper dbHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Student Sign In");
        }

        dbHelper = QuizSphereDbHelper.getInstance(this);
        sessionManager = new SessionManager(this);

        etUsername = findViewById(R.id.etLoginUsername);
        etPassword = findViewById(R.id.etLoginPassword);
        Button btnSignIn = findViewById(R.id.btnLoginSubmit);
        Button btnFillStudent = findViewById(R.id.btnQuickStudent);
        TextView tvGoToRegister = findViewById(R.id.tvGoToRegister);

        btnSignIn.setOnClickListener(v -> performLogin());

        btnFillStudent.setOnClickListener(v -> {
            etUsername.setText("student");
            etPassword.setText("student123");
            Toast.makeText(this, "Filled Demo Student credentials", Toast.LENGTH_SHORT).show();
        });

        tvGoToRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void performLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Username is required");
            etUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        User user = dbHelper.authenticateUser(username, password);

        if (user != null) {
            sessionManager.createLoginSession(user);
            Toast.makeText(this, "Welcome back, " + user.getUsername() + "!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid username or password. Please try again.", Toast.LENGTH_LONG).show();
        }
    }
}
