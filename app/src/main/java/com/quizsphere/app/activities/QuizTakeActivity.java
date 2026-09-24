package com.quizsphere.app.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.quizsphere.app.R;
import com.quizsphere.app.database.QuizSphereDbHelper;
import com.quizsphere.app.models.AttemptAnswer;
import com.quizsphere.app.models.Category;
import com.quizsphere.app.models.Option;
import com.quizsphere.app.models.Question;
import com.quizsphere.app.models.QuizAttempt;
import com.quizsphere.app.models.User;
import com.quizsphere.app.utils.SessionManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * ==============================================================================
 * Activity: QuizTakeActivity
 * Description: Real-time interactive examination engine (Slide 6, 8, 9).
 * Features:
 *   - Real-time countdown timer with auto-submit on 00:00
 *   - Question progression tracking (Previous / Next)
 *   - Question palette navigator dialog
 *   - Dynamic option binding
 *   - Instant evaluation and database record creation
 * ==============================================================================
 */
public class QuizTakeActivity extends AppCompatActivity {

    private QuizSphereDbHelper dbHelper;
    private SessionManager sessionManager;
    private User currentUser;
    private Category category;
    private List<Question> questionList;

    // Exam State
    private int currentQuestionIndex = 0;
    private CountDownTimer countDownTimer;
    private long totalTimeMillis;
    private long timeRemainingMillis;
    private int elapsedSeconds = 0;
    private boolean isSubmitted = false;

    // UI Elements
    private TextView tvTimer, tvCategoryTitle, tvQuestionCounter, tvQuestionMarks, tvQuestionPrompt;
    private ProgressBar pbTimer;
    private RadioGroup rgOptions;
    private RadioButton rbOption1, rbOption2, rbOption3, rbOption4;
    private Button btnPrev, btnNext, btnSubmitExam, btnJumpPalette;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_take);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide(); // Fullscreen exam environment
        }

        dbHelper = QuizSphereDbHelper.getInstance(this);
        sessionManager = new SessionManager(this);
        currentUser = sessionManager.getCurrentUser();

        int categoryId = getIntent().getIntExtra("category_id", -1);
        category = dbHelper.getCategoryById(categoryId);
        questionList = dbHelper.getQuestionsForCategory(categoryId);

        if (category == null || questionList.isEmpty()) {
            Toast.makeText(this, "No quiz questions found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Bind Views
        tvTimer = findViewById(R.id.tvTakeTimer);
        tvCategoryTitle = findViewById(R.id.tvTakeCategoryTitle);
        tvQuestionCounter = findViewById(R.id.tvTakeCounter);
        tvQuestionMarks = findViewById(R.id.tvTakeMarks);
        tvQuestionPrompt = findViewById(R.id.tvTakePrompt);
        pbTimer = findViewById(R.id.pbTakeTimer);

        rgOptions = findViewById(R.id.rgTakeOptions);
        rbOption1 = findViewById(R.id.rbTakeOpt1);
        rbOption2 = findViewById(R.id.rbTakeOpt2);
        rbOption3 = findViewById(R.id.rbTakeOpt3);
        rbOption4 = findViewById(R.id.rbTakeOpt4);

        btnPrev = findViewById(R.id.btnTakePrev);
        btnNext = findViewById(R.id.btnTakeNext);
        btnSubmitExam = findViewById(R.id.btnTakeSubmit);
        btnJumpPalette = findViewById(R.id.btnTakePalette);

        tvCategoryTitle.setText(category.getName());

        // Setup Countdown Timer
        totalTimeMillis = category.getTimeLimitMins() * 60L * 1000L;
        timeRemainingMillis = totalTimeMillis;
        pbTimer.setMax((int) (totalTimeMillis / 1000));
        pbTimer.setProgress((int) (totalTimeMillis / 1000));

        startCountdownTimer();

        // Render first question
        displayQuestion(0);

        // Listen for option selections to update question model
        rgOptions.setOnCheckedChangeListener((group, checkedId) -> {
            Question q = questionList.get(currentQuestionIndex);
            List<Option> opts = q.getOptions();
            if (checkedId == R.id.rbTakeOpt1 && opts.size() > 0) {
                q.setSelectedOptionId(opts.get(0).getId());
            } else if (checkedId == R.id.rbTakeOpt2 && opts.size() > 1) {
                q.setSelectedOptionId(opts.get(1).getId());
            } else if (checkedId == R.id.rbTakeOpt3 && opts.size() > 2) {
                q.setSelectedOptionId(opts.get(2).getId());
            } else if (checkedId == R.id.rbTakeOpt4 && opts.size() > 3) {
                q.setSelectedOptionId(opts.get(3).getId());
            }
        });

        // Navigation button listeners
        btnPrev.setOnClickListener(v -> {
            if (currentQuestionIndex > 0) {
                displayQuestion(currentQuestionIndex - 1);
            }
        });

        btnNext.setOnClickListener(v -> {
            if (currentQuestionIndex < questionList.size() - 1) {
                displayQuestion(currentQuestionIndex + 1);
            }
        });

        btnJumpPalette.setOnClickListener(v -> showPaletteDialog());

        btnSubmitExam.setOnClickListener(v -> confirmAndSubmit());
    }

    /**
     * Initializes and launches the live countdown timer.
     */
    private void startCountdownTimer() {
        countDownTimer = new CountDownTimer(timeRemainingMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemainingMillis = millisUntilFinished;
                elapsedSeconds++;

                long minutes = (millisUntilFinished / 1000) / 60;
                long seconds = (millisUntilFinished / 1000) % 60;
                tvTimer.setText(String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds));

                pbTimer.setProgress((int) (millisUntilFinished / 1000));

                // Urgent color warning for the final 60 seconds
                if (millisUntilFinished <= 60000) {
                    tvTimer.setTextColor(Color.parseColor("#ef4444")); // Red
                }
            }

            @Override
            public void onFinish() {
                tvTimer.setText("00:00");
                pbTimer.setProgress(0);
                Toast.makeText(QuizTakeActivity.this, "⏰ Time is up! Submitting answers automatically...", Toast.LENGTH_LONG).show();
                evaluateAndSubmitQuiz();
            }
        }.start();
    }

    /**
     * Binds a question to the UI based on its index.
     */
    private void displayQuestion(int index) {
        currentQuestionIndex = index;
        Question q = questionList.get(index);

        tvQuestionCounter.setText("Question " + (index + 1) + " of " + questionList.size());
        tvQuestionMarks.setText(q.getMarks() + " mark" + (q.getMarks() > 1 ? "s" : ""));
        tvQuestionPrompt.setText(q.getText());

        // Configure options
        List<Option> options = q.getOptions();
        rgOptions.clearCheck();

        if (options.size() > 0) {
            rbOption1.setText(options.get(0).getText());
            rbOption1.setVisibility(View.VISIBLE);
            if (q.getSelectedOptionId() == options.get(0).getId()) rbOption1.setChecked(true);
        } else {
            rbOption1.setVisibility(View.GONE);
        }

        if (options.size() > 1) {
            rbOption2.setText(options.get(1).getText());
            rbOption2.setVisibility(View.VISIBLE);
            if (q.getSelectedOptionId() == options.get(1).getId()) rbOption2.setChecked(true);
        } else {
            rbOption2.setVisibility(View.GONE);
        }

        if (options.size() > 2) {
            rbOption3.setText(options.get(2).getText());
            rbOption3.setVisibility(View.VISIBLE);
            if (q.getSelectedOptionId() == options.get(2).getId()) rbOption3.setChecked(true);
        } else {
            rbOption3.setVisibility(View.GONE);
        }

        if (options.size() > 3) {
            rbOption4.setText(options.get(3).getText());
            rbOption4.setVisibility(View.VISIBLE);
            if (q.getSelectedOptionId() == options.get(3).getId()) rbOption4.setChecked(true);
        } else {
            rbOption4.setVisibility(View.GONE);
        }

        // Navigation state
        btnPrev.setEnabled(index > 0);
        btnNext.setEnabled(index < questionList.size() - 1);
    }

    /**
     * Displays a Question Palette dialog for fast question jumping.
     */
    private void showPaletteDialog() {
        String[] items = new String[questionList.size()];
        for (int i = 0; i < questionList.size(); i++) {
            boolean answered = (questionList.get(i).getSelectedOptionId() != -1);
            items[i] = "Q" + (i + 1) + (answered ? "  [✓ Answered]" : "  [Unanswered]");
        }

        new AlertDialog.Builder(this)
                .setTitle("Question Navigator")
                .setItems(items, (dialog, which) -> displayQuestion(which))
                .setNegativeButton("Close", null)
                .show();
    }

    /**
     * Confirmation dialog before submission.
     */
    private void confirmAndSubmit() {
        int answeredCount = 0;
        for (Question q : questionList) {
            if (q.getSelectedOptionId() != -1) answeredCount++;
        }

        new AlertDialog.Builder(this)
                .setTitle("Submit Quiz")
                .setMessage("You have answered " + answeredCount + " of " + questionList.size() + " questions.\n\nAre you sure you want to finish and evaluate your attempt now?")
                .setPositiveButton("Submit", (dialog, which) -> evaluateAndSubmitQuiz())
                .setNegativeButton("Continue", null)
                .show();
    }

    /**
     * Evaluates answers, calculates score & percentage, stores records in SQLite,
     * and directs the student to the instant results audit screen.
     */
    private void evaluateAndSubmitQuiz() {
        if (isSubmitted) return;
        isSubmitted = true;

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        int earnedMarks = 0;
        int totalMarks = 0;
        List<AttemptAnswer> answerList = new ArrayList<>();

        for (Question q : questionList) {
            totalMarks += q.getMarks();
            Option correctOpt = q.getCorrectOption();
            String correctText = (correctOpt != null) ? correctOpt.getText() : "N/A";

            int selectedId = q.getSelectedOptionId();
            String selectedText = "";
            boolean isCorrect = false;
            int marksForQ = 0;

            for (Option opt : q.getOptions()) {
                if (opt.getId() == selectedId) {
                    selectedText = opt.getText();
                    if (opt.isCorrect()) {
                        isCorrect = true;
                        marksForQ = q.getMarks();
                        earnedMarks += marksForQ;
                    }
                    break;
                }
            }

            AttemptAnswer ans = new AttemptAnswer();
            ans.setQuestionId(q.getId());
            ans.setQuestionText(q.getText());
            ans.setSelectedOptionId(selectedId);
            ans.setSelectedOptionText(selectedText);
            ans.setCorrectOptionText(correctText);
            ans.setCorrect(isCorrect);
            ans.setMarksObtained(marksForQ);
            ans.setMaxMarks(q.getMarks());
            ans.setExplanation(q.getExplanation());
            answerList.add(ans);
        }

        double percentage = (totalMarks > 0) ? ((double) earnedMarks / totalMarks * 100.0) : 0.0;
        boolean passed = percentage >= category.getPassPercentage();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String dateStr = sdf.format(new Date());

        QuizAttempt attempt = new QuizAttempt(
                currentUser != null ? currentUser.getId() : 1,
                currentUser != null ? currentUser.getUsername() : "student",
                category.getId(),
                category.getName(),
                earnedMarks,
                totalMarks,
                percentage,
                passed,
                elapsedSeconds,
                dateStr
        );

        long attemptId = dbHelper.saveQuizAttempt(attempt, answerList);

        // Open Results screen
        Intent intent = new Intent(QuizTakeActivity.this, QuizResultActivity.class);
        intent.putExtra("attempt_id", (int) attemptId);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Quit Examination?")
                .setMessage("Your timer is running. If you exit now, your attempt will not be scored.")
                .setPositiveButton("Quit", (dialog, which) -> {
                    if (countDownTimer != null) countDownTimer.cancel();
                    super.onBackPressed();
                })
                .setNegativeButton("Continue", null)
                .show();
    }
}
