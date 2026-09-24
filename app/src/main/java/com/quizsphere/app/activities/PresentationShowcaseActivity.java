package com.quizsphere.app.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.quizsphere.app.R;

/**
 * ==============================================================================
 * Activity: PresentationShowcaseActivity
 * Description: Interactive 12-Slide Presentation Walkthrough directly based on
 * QuizSphere_Final.pptx:
 *   - Slide 1: Cover & Team Presentation (Giri Siddharth 40013, Ankit Singh 40003, Raju Ranjan 40043)
 *   - Slide 2: Problem Statement & Sample Question
 *   - Slide 3: 6 Core Objectives
 *   - Slide 4: Technology Stack
 *   - Slide 5: System Architecture (Django MVT Pattern)
 *   - Slide 6: Key Features
 *   - Slide 7: Database Design (Core Relational Models)
 *   - Slide 8: End-to-End User Workflow
 *   - Slide 9: Application Modules
 *   - Slide 10: Security Considerations
 *   - Slide 11: Future Roadmap
 *   - Slide 12: Conclusion & Q&A
 * ==============================================================================
 */
public class PresentationShowcaseActivity extends AppCompatActivity {

    private int currentSlideIndex = 0;

    private TextView tvSlideBadge, tvSlideTitle, tvSlideSubtitle, tvSlideContent, tvSlideExtra;
    private Button btnPrevSlide, btnNextSlide;

    // Slide Data Structures
    private final String[] slideTitles = {
            "QuizSphere Presentation",
            "Introduction & Problem Statement",
            "Project Objectives",
            "Technology Stack",
            "System Architecture (MVT)",
            "Key Features",
            "Database Design (Models)",
            "End-to-End User Workflow",
            "Application Modules",
            "Security Considerations",
            "Future Roadmap",
            "Conclusion & Discussion"
    };

    private final String[] slideSubtitles = {
            "A DJANGO + PYTHON WEB APPLICATION • Mini Project",
            "Traditional Pen-and-Paper vs QuizSphere",
            "Six Key Technical Milestones",
            "Frontend, Backend, Database & Admin Tools",
            "Django Model-View-Template Pattern",
            "Eight Interactive Platform Capabilities",
            "Slide 7: Core Relational Database Design",
            "Step-by-Step Candidate Experience",
            "Student, Admin, and Engine Modules",
            "Slide 10: Security & Access Integrity",
            "Slide 11: Upcoming Capabilities",
            "Slide 12: Project Wrap-Up & Authors"
    };

    private final String[] slideContents = {
            // Slide 1
            "QuizSphere: An Interactive Online Quiz & Assessment Platform\n\n" +
            "Presented by:\n" +
            "• Giri Siddharth — Roll No: 40013\n" +
            "• Ankit Singh — Roll No: 40003\n" +
            "• Raju Ranjan Choudhary — Roll No: 40043\n\n" +
            "Project Type: Django + Python Web Application (Mini Project)",

            // Slide 2
            "Problem Statement:\n" +
            "Traditional pen-and-paper testing is slow, labor-intensive, error-prone to grade, and deprives learners of immediate feedback.\n\n" +
            "The QuizSphere Solution:\n" +
            "A full-stack web application built on Python and Django that lets educators create quizzes and lets learners take them online with instant scoring, ranked results, and a clean, responsive interface.\n\n" +
            "Sample Question from Slide 2:\n" +
            "\"Which language is Django written in?\"\n" +
            "A) Java    B) Python (✓)\n" +
            "C) C++     D) PHP",

            // Slide 3
            "01. Role-Based Access:\n" +
            "Separate, secure experiences for Admin (quiz creators) and Students (quiz takers).\n\n" +
            "02. Dynamic Quiz Engine:\n" +
            "Create categories, questions, options, and countdown limits without touching code.\n\n" +
            "03. Automated Evaluation:\n" +
            "Instantly calculate scores, correct answers, and pass/fail status on submission.\n\n" +
            "04. Result Analytics:\n" +
            "Store every attempt and show students their score history and rank.\n\n" +
            "05. Responsive UI:\n" +
            "Clean Bootstrap 5 & Android interface for desktop, tablet, and mobile.\n\n" +
            "06. Secure Authentication:\n" +
            "Django's built-in auth system with hashed passwords and session control.",

            // Slide 4
            "1. Frontend:\n" +
            "HTML5, CSS3, Bootstrap 5, JavaScript / Android Java SDK with XML Layouts.\n\n" +
            "2. Backend:\n" +
            "Python 3 & Django Framework / Java Native Engine handling business logic and sessions.\n\n" +
            "3. Database:\n" +
            "SQLite (dev) / PostgreSQL (prod) via Django ORM & SQLiteOpenHelper.\n\n" +
            "4. Admin Tooling:\n" +
            "Django Admin Panel & custom mobile admin portal to manage content.\n\n" +
            "5. Authentication:\n" +
            "Session-based login, signup, password hashing, and access control.\n\n" +
            "6. Deployment:\n" +
            "Gunicorn, Nginx, Render / Heroku / Android APK.",

            // Slide 5
            "Django MVT Pattern Request Flow:\n\n" +
            "[Browser / Mobile Client]\n" +
            "       ↓ (Sends HTTP Request / Action)\n" +
            "[URL Dispatcher (urls.py)]\n" +
            "       ↓ (Routes to matching view)\n" +
            "[View (views.py / Activity)]\n" +
            "       ↓ (Queries / persists)\n" +
            "[Model (models.py / SQLite)]\n" +
            "       ↓ (Fetches data)\n" +
            "[Database (SQLite / PostgreSQL)]\n" +
            "       ↓ (Returns records)\n" +
            "[Template (HTML / XML UI)]\n" +
            "       ↓ (Renders final response back to client)",

            // Slide 6
            "1. User Registration & Login (Secure session control)\n" +
            "2. Category-wise Quizzes (Grouped by subject/topic)\n" +
            "3. Timed Quiz Attempts (Countdown timer with auto-submit)\n" +
            "4. Instant Auto-Grading (Answers scored in milliseconds)\n" +
            "5. Score Dashboard (Personal attempt history and pass rate)\n" +
            "6. Admin Content Manager (Dynamic CRUD for categories & questions)\n" +
            "7. Leaderboard (Top scorers ranked per category and globally)\n" +
            "8. Responsive Design (Adapts seamlessly across phones & tablets)",

            // Slide 7
            "Core Relational Models:\n\n" +
            "• User:\n" +
            "  id (PK), username, email, password, role/is_admin\n\n" +
            "• Category:\n" +
            "  id (PK), name, slug, description, time_limit_mins, pass_percentage\n\n" +
            "• Question:\n" +
            "  id (PK), category_id (FK), text, marks, explanation\n\n" +
            "• Option:\n" +
            "  id (PK), question_id (FK), text, is_correct\n\n" +
            "• QuizAttempt:\n" +
            "  id (PK), user_id (FK), category_id (FK), score, total_marks, percentage, passed, date_taken\n\n" +
            "Relationships:\n" +
            "1 Category → Many Questions\n" +
            "1 Question → Many Options\n" +
            "1 User → Many QuizAttempts",

            // Slide 8
            "1. Register / Login:\n" +
            "User creates an account or signs in securely.\n\n" +
            "2. Choose Category:\n" +
            "Browse available subjects (Django, Python, Web Tech, Database).\n\n" +
            "3. Attempt Quiz:\n" +
            "Answer timed multiple-choice questions with live countdown timer.\n\n" +
            "4. Auto-Submit & Grade:\n" +
            "System automatically scores answers the moment time expires or on submit.\n\n" +
            "5. View Result & Review:\n" +
            "See score, correct vs incorrect options, explanations, and leaderboard rank.",

            // Slide 9
            "Student Module:\n" +
            "• Sign up / Login\n" +
            "• Browse quiz categories\n" +
            "• Attempt timed quizzes\n" +
            "• View score history and personal analytics\n\n" +
            "Admin Module:\n" +
            "• Manage categories & questions\n" +
            "• Add/edit options & correct answers\n" +
            "• View all student attempts\n" +
            "• Admin management dashboard\n\n" +
            "Core Quiz Engine:\n" +
            "• Countdown timer & auto-submit logic\n" +
            "• Instant score calculation\n" +
            "• Leaderboard ranking generator",

            // Slide 10
            "1. Password Hashing:\n" +
            "PBKDF2/Argon2 hashing — plain-text passwords are never stored.\n\n" +
            "2. CSRF Protection:\n" +
            "Every form includes unique tokens to block cross-site request forgery.\n\n" +
            "3. Session Management:\n" +
            "Secure, server-side / shared-preference sessions track logged-in users.\n\n" +
            "4. SQL Injection Safe:\n" +
            "The ORM / SQLite parameterizes all queries automatically.\n\n" +
            "5. Role-Based Access:\n" +
            "Decorators & permission checks restrict admin-only views.\n\n" +
            "6. Input Validation:\n" +
            "Form sanitization cleans all user-submitted data.",

            // Slide 11
            "1. REST API with Django REST Framework for mobile sync\n" +
            "2. AI-based question difficulty tagging & adaptive quizzes\n" +
            "3. Real-time multiplayer quiz mode using WebSockets\n" +
            "4. Detailed analytics dashboard with charts (MPAndroidChart / Chart.js)\n" +
            "5. Push, Email & SMS notifications for quiz results\n" +
            "6. Export results to PDF / Excel for offline records",

            // Slide 12
            "Conclusion:\n" +
            "QuizSphere proves that a robust, secure, and user-friendly quiz platform can be delivered with a lean, well-structured tech stack — from ORM-backed relational models to a clean, responsive front end.\n\n" +
            "Thank You!\n\n" +
            "Presented by:\n" +
            "• Giri Siddharth (40013)\n" +
            "• Ankit Singh (40003)\n" +
            "• Raju Ranjan Choudhary (40043)\n\n" +
            "Questions & Discussion Welcome!"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation_showcase);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Project Presentation (12 Slides)");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        tvSlideBadge = findViewById(R.id.tvSlideBadge);
        tvSlideTitle = findViewById(R.id.tvSlideTitle);
        tvSlideSubtitle = findViewById(R.id.tvSlideSubtitle);
        tvSlideContent = findViewById(R.id.tvSlideContent);
        tvSlideExtra = findViewById(R.id.tvSlideExtra);

        btnPrevSlide = findViewById(R.id.btnSlidePrev);
        btnNextSlide = findViewById(R.id.btnSlideNext);

        renderSlide(0);

        btnPrevSlide.setOnClickListener(v -> {
            if (currentSlideIndex > 0) {
                renderSlide(currentSlideIndex - 1);
            }
        });

        btnNextSlide.setOnClickListener(v -> {
            if (currentSlideIndex < slideTitles.length - 1) {
                renderSlide(currentSlideIndex + 1);
            }
        });
    }

    private void renderSlide(int index) {
        currentSlideIndex = index;

        tvSlideBadge.setText("Slide " + (index + 1) + " of " + slideTitles.length);
        tvSlideTitle.setText(slideTitles[index]);
        tvSlideSubtitle.setText(slideSubtitles[index]);
        tvSlideContent.setText(slideContents[index]);

        btnPrevSlide.setEnabled(index > 0);
        btnNextSlide.setEnabled(index < slideTitles.length - 1);

        if (index == 0) {
            tvSlideExtra.setText("🎯 Welcome to the QuizSphere Technical Architecture Showcase!");
        } else if (index == 4) {
            tvSlideExtra.setText("⚡ Django MVT Pattern matches the system architecture of our web & mobile app.");
        } else if (index == 6) {
            tvSlideExtra.setText("💾 Models implemented via Django ORM on backend and SQLiteOpenHelper in Android.");
        } else {
            tvSlideExtra.setText("QuizSphere | Django & Android Quiz Platform");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
