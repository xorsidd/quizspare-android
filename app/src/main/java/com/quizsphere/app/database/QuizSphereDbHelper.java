package com.quizsphere.app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.quizsphere.app.models.AttemptAnswer;
import com.quizsphere.app.models.Category;
import com.quizsphere.app.models.Option;
import com.quizsphere.app.models.Question;
import com.quizsphere.app.models.QuizAttempt;
import com.quizsphere.app.models.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * ==============================================================================
 * Database: QuizSphereDbHelper
 * Description: SQLite Database Engine implementing core relational tables
 * directly reflecting Slide 7 of QuizSphere_Final.pptx:
 *   - users (id, username, email, password, role)
 *   - categories (id, name, slug, description, time_limit_mins, pass_percentage)
 *   - questions (id, category_id FK, text, marks, explanation)
 *   - options (id, question_id FK, text, is_correct)
 *   - quiz_attempts (id, user_id FK, category_id FK, score, percentage, date_taken)
 *   - attempt_answers (audit trail of each answer submitted)
 * ==============================================================================
 */
public class QuizSphereDbHelper extends SQLiteOpenHelper {

    // Database meta configuration
    private static final String DATABASE_NAME = "quizsphere.db";
    private static final int DATABASE_VERSION = 1;

    // Table names
    public static final String TABLE_USERS = "users";
    public static final String TABLE_CATEGORIES = "categories";
    public static final String TABLE_QUESTIONS = "questions";
    public static final String TABLE_OPTIONS = "options";
    public static final String TABLE_ATTEMPTS = "quiz_attempts";
    public static final String TABLE_ATTEMPT_ANSWERS = "attempt_answers";

    // Common column names
    public static final String COL_ID = "id";

    // Users columns
    public static final String COL_USER_USERNAME = "username";
    public static final String COL_USER_EMAIL = "email";
    public static final String COL_USER_PASSWORD = "password";
    public static final String COL_USER_ROLE = "role";

    // Categories columns
    public static final String COL_CAT_NAME = "name";
    public static final String COL_CAT_SLUG = "slug";
    public static final String COL_CAT_DESC = "description";
    public static final String COL_CAT_ICON = "icon_name";
    public static final String COL_CAT_TIME_LIMIT = "time_limit_mins";
    public static final String COL_CAT_PASS_PCT = "pass_percentage";

    // Questions columns
    public static final String COL_Q_CAT_ID = "category_id";
    public static final String COL_Q_TEXT = "text";
    public static final String COL_Q_MARKS = "marks";
    public static final String COL_Q_EXPLANATION = "explanation";

    // Options columns
    public static final String COL_OPT_Q_ID = "question_id";
    public static final String COL_OPT_TEXT = "text";
    public static final String COL_OPT_IS_CORRECT = "is_correct";

    // QuizAttempt columns
    public static final String COL_ATT_USER_ID = "user_id";
    public static final String COL_ATT_USERNAME = "username";
    public static final String COL_ATT_CAT_ID = "category_id";
    public static final String COL_ATT_CAT_NAME = "category_name";
    public static final String COL_ATT_SCORE = "score";
    public static final String COL_ATT_TOTAL_MARKS = "total_marks";
    public static final String COL_ATT_PERCENTAGE = "percentage";
    public static final String COL_ATT_PASSED = "passed";
    public static final String COL_ATT_TIME_TAKEN = "time_taken_seconds";
    public static final String COL_ATT_DATE = "date_taken";

    // AttemptAnswers columns
    public static final String COL_ANS_ATT_ID = "attempt_id";
    public static final String COL_ANS_Q_ID = "question_id";
    public static final String COL_ANS_Q_TEXT = "question_text";
    public static final String COL_ANS_SEL_OPT_ID = "selected_option_id";
    public static final String COL_ANS_SEL_OPT_TEXT = "selected_option_text";
    public static final String COL_ANS_CORR_OPT_TEXT = "correct_option_text";
    public static final String COL_ANS_IS_CORRECT = "is_correct";
    public static final String COL_ANS_MARKS_OBT = "marks_obtained";
    public static final String COL_ANS_MAX_MARKS = "max_marks";
    public static final String COL_ANS_EXPLANATION = "explanation";

    // Singleton instance
    private static QuizSphereDbHelper instance;

    public static synchronized QuizSphereDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new QuizSphereDbHelper(context.getApplicationContext());
        }
        return instance;
    }

    public QuizSphereDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 1. Create Users Table
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USER_USERNAME + " TEXT UNIQUE NOT NULL, " +
                COL_USER_EMAIL + " TEXT, " +
                COL_USER_PASSWORD + " TEXT NOT NULL, " +
                COL_USER_ROLE + " TEXT DEFAULT 'student'" +
                ");");

        // 2. Create Categories Table
        db.execSQL("CREATE TABLE " + TABLE_CATEGORIES + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CAT_NAME + " TEXT UNIQUE NOT NULL, " +
                COL_CAT_SLUG + " TEXT UNIQUE NOT NULL, " +
                COL_CAT_DESC + " TEXT, " +
                COL_CAT_ICON + " TEXT, " +
                COL_CAT_TIME_LIMIT + " INTEGER DEFAULT 5, " +
                COL_CAT_PASS_PCT + " INTEGER DEFAULT 60" +
                ");");

        // 3. Create Questions Table
        db.execSQL("CREATE TABLE " + TABLE_QUESTIONS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_Q_CAT_ID + " INTEGER NOT NULL, " +
                COL_Q_TEXT + " TEXT NOT NULL, " +
                COL_Q_MARKS + " INTEGER DEFAULT 1, " +
                COL_Q_EXPLANATION + " TEXT, " +
                "FOREIGN KEY(" + COL_Q_CAT_ID + ") REFERENCES " + TABLE_CATEGORIES + "(" + COL_ID + ") ON DELETE CASCADE" +
                ");");

        // 4. Create Options Table
        db.execSQL("CREATE TABLE " + TABLE_OPTIONS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_OPT_Q_ID + " INTEGER NOT NULL, " +
                COL_OPT_TEXT + " TEXT NOT NULL, " +
                COL_OPT_IS_CORRECT + " INTEGER DEFAULT 0, " +
                "FOREIGN KEY(" + COL_OPT_Q_ID + ") REFERENCES " + TABLE_QUESTIONS + "(" + COL_ID + ") ON DELETE CASCADE" +
                ");");

        // 5. Create Quiz Attempts Table
        db.execSQL("CREATE TABLE " + TABLE_ATTEMPTS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_ATT_USER_ID + " INTEGER NOT NULL, " +
                COL_ATT_USERNAME + " TEXT NOT NULL, " +
                COL_ATT_CAT_ID + " INTEGER NOT NULL, " +
                COL_ATT_CAT_NAME + " TEXT NOT NULL, " +
                COL_ATT_SCORE + " INTEGER NOT NULL, " +
                COL_ATT_TOTAL_MARKS + " INTEGER NOT NULL, " +
                COL_ATT_PERCENTAGE + " REAL NOT NULL, " +
                COL_ATT_PASSED + " INTEGER NOT NULL, " +
                COL_ATT_TIME_TAKEN + " INTEGER DEFAULT 0, " +
                COL_ATT_DATE + " TEXT NOT NULL" +
                ");");

        // 6. Create Attempt Answers Table
        db.execSQL("CREATE TABLE " + TABLE_ATTEMPT_ANSWERS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_ANS_ATT_ID + " INTEGER NOT NULL, " +
                COL_ANS_Q_ID + " INTEGER NOT NULL, " +
                COL_ANS_Q_TEXT + " TEXT NOT NULL, " +
                COL_ANS_SEL_OPT_ID + " INTEGER, " +
                COL_ANS_SEL_OPT_TEXT + " TEXT, " +
                COL_ANS_CORR_OPT_TEXT + " TEXT NOT NULL, " +
                COL_ANS_IS_CORRECT + " INTEGER NOT NULL, " +
                COL_ANS_MARKS_OBT + " INTEGER NOT NULL, " +
                COL_ANS_MAX_MARKS + " INTEGER NOT NULL, " +
                COL_ANS_EXPLANATION + " TEXT, " +
                "FOREIGN KEY(" + COL_ANS_ATT_ID + ") REFERENCES " + TABLE_ATTEMPTS + "(" + COL_ID + ") ON DELETE CASCADE" +
                ");");

        // Seed initial data matching PowerPoint presentation topics
        seedInitialData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTEMPT_ANSWERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTEMPTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OPTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    /**
     * Seeds initial users, categories, questions, and attempts into SQLite.
     */
    private void seedInitialData(SQLiteDatabase db) {
        // Demo Accounts
        insertUserRaw(db, "admin", "admin@quizsphere.local", "admin123", "admin");
        insertUserRaw(db, "student", "student@quizsphere.local", "student123", "student");
        insertUserRaw(db, "siddharth_40013", "siddharth@quizsphere.local", "pass123", "student");
        insertUserRaw(db, "ankit_40003", "ankit@quizsphere.local", "pass123", "student");
        insertUserRaw(db, "raju_40043", "raju@quizsphere.local", "pass123", "student");

        // Categories
        long cat1 = insertCategoryRaw(db, "Django Web Framework", "django-framework",
                "Learn Django MVT pattern, ORM queries, URL dispatcher, CSRF security, and session management.", "ic_django", 5, 60);
        long cat2 = insertCategoryRaw(db, "Python Core & Concepts", "python-core",
                "Master Python list comprehensions, yield generators, object identity id(), and data immutability.", "ic_python", 5, 60);
        long cat3 = insertCategoryRaw(db, "Web Technologies & Bootstrap 5", "web-bootstrap",
                "HTML5 semantic tags, CSS3 flexbox layouts, and Bootstrap 5 responsive 12-column grid.", "ic_bootstrap", 5, 60);
        long cat4 = insertCategoryRaw(db, "Database Systems & SQL", "database-sql",
                "Relational schema models, Primary & Foreign Keys, parameterized queries, SQLite vs PostgreSQL.", "ic_database", 5, 60);

        // Category 1 Questions (Django)
        // Slide 2 Sample Question
        insertQuestionWithOptions(db, cat1, "Which language is Django written in? (Slide 2 Sample Question)", 1,
                "Django is a high-level web framework written entirely in Python.",
                new String[]{"Java", "Python", "C++", "PHP"}, 1);

        insertQuestionWithOptions(db, cat1, "Which architectural design pattern does Django follow? (Slide 5)", 1,
                "Django implements the Model-View-Template (MVT) pattern.",
                new String[]{"MVC", "MVT", "MVVM", "Microservices"}, 1);

        insertQuestionWithOptions(db, cat1, "Which component maps an incoming URL to a view? (Slide 5)", 1,
                "The URL Dispatcher defined in urls.py routes incoming HTTP requests to views.py.",
                new String[]{"models.py", "URL Dispatcher (urls.py)", "admin.py", "wsgi.py"}, 1);

        insertQuestionWithOptions(db, cat1, "How does Django protect forms from Cross-Site Request Forgery? (Slide 10)", 1,
                "Django validates unique session tokens using the {% csrf_token %} tag and CSRF middleware.",
                new String[]{"By blocking POST requests", "Using {% csrf_token %} and CSRF middleware", "Base64 encoding", "Disabling cookies"}, 1);

        insertQuestionWithOptions(db, cat1, "What default password hashing is used by Django Auth? (Slide 10)", 1,
                "Django uses PBKDF2 with SHA256 and never stores plain passwords.",
                new String[]{"Plaintext MD5", "PBKDF2 with SHA256", "ROT13", "Single DES"}, 1);

        // Category 2 Questions (Python Core)
        insertQuestionWithOptions(db, cat2, "What is the correct syntax for a list comprehension of even numbers from 0 to 9?", 1,
                "[x for x in range(10) if x % 2 == 0] generates [0, 2, 4, 6, 8].",
                new String[]{"[for x in range(10) if x % 2 == 0]", "[x for x in range(10) if x % 2 == 0]", "{x: x % 2 == 0}", "(x in range(10))"}, 1);

        insertQuestionWithOptions(db, cat2, "Which built-in Python function returns the unique identity of an object?", 1,
                "id(object) returns the memory address identity integer.",
                new String[]{"loc()", "id()", "addr()", "memory()"}, 1);

        insertQuestionWithOptions(db, cat2, "What keyword is used to create a generator function in Python?", 1,
                "The yield keyword turns a function into a lazy generator.",
                new String[]{"return", "yield", "emit", "produce"}, 1);

        insertQuestionWithOptions(db, cat2, "Which Python data type is immutable?", 1,
                "Tuples are immutable; lists, dictionaries, and sets can be modified.",
                new String[]{"List", "Dictionary", "Tuple", "Set"}, 2);

        // Category 3 Questions (Web Tech & Bootstrap)
        insertQuestionWithOptions(db, cat3, "How many columns are in the standard Bootstrap 5 grid system? (Slide 4)", 1,
                "Bootstrap 5 utilizes a 12-column responsive flexbox grid.",
                new String[]{"8 columns", "10 columns", "12 columns", "16 columns"}, 2);

        insertQuestionWithOptions(db, cat3, "Which Bootstrap 5 class makes a button take full container width?", 1,
                "w-100 sets the width to 100%.",
                new String[]{"btn-block", "w-100", "btn-full", "width-max"}, 1);

        insertQuestionWithOptions(db, cat3, "Which HTML5 semantic tag represents navigational links?", 1,
                "The <nav> element is intended for major navigation blocks.",
                new String[]{"<section>", "<nav>", "<menu>", "<header>"}, 1);

        // Category 4 Questions (Database & SQL)
        insertQuestionWithOptions(db, cat4, "Which field connects Question to Category in QuizSphere models? (Slide 7)", 1,
                "category_id is a ForeignKey connecting Question to Category.",
                new String[]{"Primary Key id", "category_id (ForeignKey)", "slug text", "ManyToMany table"}, 1);

        insertQuestionWithOptions(db, cat4, "How does Django ORM prevent SQL Injection? (Slide 10)", 1,
                "Django ORM uses parameterized queries automatically.",
                new String[]{"Disables WHERE queries", "Uses parameterized queries automatically", "Converts SQL to XML", "In-memory tables only"}, 1);

        // Initial Seed Attempts for Leaderboard
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String today = sdf.format(new Date());

        insertAttemptRaw(db, 3, "siddharth_40013", (int) cat1, "Django Web Framework", 5, 5, 100.0, 1, 42, today);
        insertAttemptRaw(db, 4, "ankit_40003", (int) cat1, "Django Web Framework", 4, 5, 80.0, 1, 55, today);
        insertAttemptRaw(db, 5, "raju_40043", (int) cat2, "Python Core & Concepts", 4, 4, 100.0, 1, 38, today);
        insertAttemptRaw(db, 2, "student", (int) cat1, "Django Web Framework", 3, 5, 60.0, 1, 62, today);
    }

    private void insertUserRaw(SQLiteDatabase db, String username, String email, String password, String role) {
        ContentValues cv = new ContentValues();
        cv.put(COL_USER_USERNAME, username);
        cv.put(COL_USER_EMAIL, email);
        cv.put(COL_USER_PASSWORD, password);
        cv.put(COL_USER_ROLE, role);
        db.insert(TABLE_USERS, null, cv);
    }

    private long insertCategoryRaw(SQLiteDatabase db, String name, String slug, String desc, String icon, int timeLimit, int passPct) {
        ContentValues cv = new ContentValues();
        cv.put(COL_CAT_NAME, name);
        cv.put(COL_CAT_SLUG, slug);
        cv.put(COL_CAT_DESC, desc);
        cv.put(COL_CAT_ICON, icon);
        cv.put(COL_CAT_TIME_LIMIT, timeLimit);
        cv.put(COL_CAT_PASS_PCT, passPct);
        return db.insert(TABLE_CATEGORIES, null, cv);
    }

    private void insertQuestionWithOptions(SQLiteDatabase db, long categoryId, String text, int marks, String explanation, String[] options, int correctIndex) {
        ContentValues qcv = new ContentValues();
        qcv.put(COL_Q_CAT_ID, categoryId);
        qcv.put(COL_Q_TEXT, text);
        qcv.put(COL_Q_MARKS, marks);
        qcv.put(COL_Q_EXPLANATION, explanation);
        long qId = db.insert(TABLE_QUESTIONS, null, qcv);

        for (int i = 0; i < options.length; i++) {
            ContentValues ocv = new ContentValues();
            ocv.put(COL_OPT_Q_ID, qId);
            ocv.put(COL_OPT_TEXT, options[i]);
            ocv.put(COL_OPT_IS_CORRECT, (i == correctIndex) ? 1 : 0);
            db.insert(TABLE_OPTIONS, null, ocv);
        }
    }

    private void insertAttemptRaw(SQLiteDatabase db, int userId, String username, int catId, String catName,
                                  int score, int totalMarks, double pct, int passed, int timeTaken, String date) {
        ContentValues cv = new ContentValues();
        cv.put(COL_ATT_USER_ID, userId);
        cv.put(COL_ATT_USERNAME, username);
        cv.put(COL_ATT_CAT_ID, catId);
        cv.put(COL_ATT_CAT_NAME, catName);
        cv.put(COL_ATT_SCORE, score);
        cv.put(COL_ATT_TOTAL_MARKS, totalMarks);
        cv.put(COL_ATT_PERCENTAGE, pct);
        cv.put(COL_ATT_PASSED, passed);
        cv.put(COL_ATT_TIME_TAKEN, timeTaken);
        cv.put(COL_ATT_DATE, date);
        db.insert(TABLE_ATTEMPTS, null, cv);
    }

    // =========================================================================
    // PUBLIC API METHODS
    // =========================================================================

    /**
     * Authenticates user against SQLite database.
     */
    public User authenticateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null,
                COL_USER_USERNAME + "=? AND " + COL_USER_PASSWORD + "=?",
                new String[]{username, password}, null, null, null);

        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
            user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_USERNAME)));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_EMAIL)));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_PASSWORD)));
            user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_ROLE)));
            cursor.close();
        }
        return user;
    }

    /**
     * Registers a new user.
     */
    public boolean registerUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_USER_USERNAME, user.getUsername());
        cv.put(COL_USER_EMAIL, user.getEmail());
        cv.put(COL_USER_PASSWORD, user.getPassword());
        cv.put(COL_USER_ROLE, user.getRole());
        long result = db.insert(TABLE_USERS, null, cv);
        return result != -1;
    }

    /**
     * Checks if a username is already taken.
     */
    public boolean checkUsernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COL_ID},
                COL_USER_USERNAME + "=?", new String[]{username}, null, null, null);
        boolean exists = (cursor != null && cursor.getCount() > 0);
        if (cursor != null) cursor.close();
        return exists;
    }

    /**
     * Retrieves all categories along with their question counts.
     */
    public List<Category> getAllCategories() {
        List<Category> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT c.*, COUNT(q.id) AS q_count FROM " + TABLE_CATEGORIES + " c " +
                "LEFT JOIN " + TABLE_QUESTIONS + " q ON c.id = q.category_id " +
                "GROUP BY c.id ORDER BY c.name ASC", null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Category cat = new Category();
                cat.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
                cat.setName(cursor.getString(cursor.getColumnIndexOrThrow(COL_CAT_NAME)));
                cat.setSlug(cursor.getString(cursor.getColumnIndexOrThrow(COL_CAT_SLUG)));
                cat.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COL_CAT_DESC)));
                cat.setIconName(cursor.getString(cursor.getColumnIndexOrThrow(COL_CAT_ICON)));
                cat.setTimeLimitMins(cursor.getInt(cursor.getColumnIndexOrThrow(COL_CAT_TIME_LIMIT)));
                cat.setPassPercentage(cursor.getInt(cursor.getColumnIndexOrThrow(COL_CAT_PASS_PCT)));
                cat.setQuestionCount(cursor.getInt(cursor.getColumnIndexOrThrow("q_count")));
                list.add(cat);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    /**
     * Retrieves category by ID.
     */
    public Category getCategoryById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT c.*, COUNT(q.id) AS q_count FROM " + TABLE_CATEGORIES + " c " +
                "LEFT JOIN " + TABLE_QUESTIONS + " q ON c.id = q.category_id " +
                "WHERE c.id=? GROUP BY c.id", new String[]{String.valueOf(id)});

        Category cat = null;
        if (cursor != null && cursor.moveToFirst()) {
            cat = new Category();
            cat.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
            cat.setName(cursor.getString(cursor.getColumnIndexOrThrow(COL_CAT_NAME)));
            cat.setSlug(cursor.getString(cursor.getColumnIndexOrThrow(COL_CAT_SLUG)));
            cat.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COL_CAT_DESC)));
            cat.setIconName(cursor.getString(cursor.getColumnIndexOrThrow(COL_CAT_ICON)));
            cat.setTimeLimitMins(cursor.getInt(cursor.getColumnIndexOrThrow(COL_CAT_TIME_LIMIT)));
            cat.setPassPercentage(cursor.getInt(cursor.getColumnIndexOrThrow(COL_CAT_PASS_PCT)));
            cat.setQuestionCount(cursor.getInt(cursor.getColumnIndexOrThrow("q_count")));
            cursor.close();
        }
        return cat;
    }

    /**
     * Retrieves all questions and their options for a Category.
     */
    public List<Question> getQuestionsForCategory(int categoryId) {
        List<Question> questions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_QUESTIONS, null,
                COL_Q_CAT_ID + "=?", new String[]{String.valueOf(categoryId)},
                null, null, COL_ID + " ASC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Question q = new Question();
                q.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
                q.setCategoryId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_Q_CAT_ID)));
                q.setText(cursor.getString(cursor.getColumnIndexOrThrow(COL_Q_TEXT)));
                q.setMarks(cursor.getInt(cursor.getColumnIndexOrThrow(COL_Q_MARKS)));
                q.setExplanation(cursor.getString(cursor.getColumnIndexOrThrow(COL_Q_EXPLANATION)));

                // Fetch options for this question
                Cursor optCursor = db.query(TABLE_OPTIONS, null,
                        COL_OPT_Q_ID + "=?", new String[]{String.valueOf(q.getId())},
                        null, null, COL_ID + " ASC");

                List<Option> options = new ArrayList<>();
                if (optCursor != null && optCursor.moveToFirst()) {
                    do {
                        Option opt = new Option();
                        opt.setId(optCursor.getInt(optCursor.getColumnIndexOrThrow(COL_ID)));
                        opt.setQuestionId(optCursor.getInt(optCursor.getColumnIndexOrThrow(COL_OPT_Q_ID)));
                        opt.setText(optCursor.getString(optCursor.getColumnIndexOrThrow(COL_OPT_TEXT)));
                        opt.setCorrect(optCursor.getInt(optCursor.getColumnIndexOrThrow(COL_OPT_IS_CORRECT)) == 1);
                        options.add(opt);
                    } while (optCursor.moveToNext());
                    optCursor.close();
                }
                q.setOptions(options);
                questions.add(q);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return questions;
    }

    /**
     * Saves a completed quiz attempt and its detailed question audit trail.
     */
    public long saveQuizAttempt(QuizAttempt attempt, List<AttemptAnswer> answers) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        long attemptId = -1;
        try {
            ContentValues cv = new ContentValues();
            cv.put(COL_ATT_USER_ID, attempt.getUserId());
            cv.put(COL_ATT_USERNAME, attempt.getUsername());
            cv.put(COL_ATT_CAT_ID, attempt.getCategoryId());
            cv.put(COL_ATT_CAT_NAME, attempt.getCategoryName());
            cv.put(COL_ATT_SCORE, attempt.getScore());
            cv.put(COL_ATT_TOTAL_MARKS, attempt.getTotalMarks());
            cv.put(COL_ATT_PERCENTAGE, attempt.getPercentage());
            cv.put(COL_ATT_PASSED, attempt.isPassed() ? 1 : 0);
            cv.put(COL_ATT_TIME_TAKEN, attempt.getTimeTakenSeconds());
            cv.put(COL_ATT_DATE, attempt.getDateTaken());

            attemptId = db.insert(TABLE_ATTEMPTS, null, cv);

            if (attemptId != -1) {
                for (AttemptAnswer ans : answers) {
                    ContentValues acv = new ContentValues();
                    acv.put(COL_ANS_ATT_ID, attemptId);
                    acv.put(COL_ANS_Q_ID, ans.getQuestionId());
                    acv.put(COL_ANS_Q_TEXT, ans.getQuestionText());
                    acv.put(COL_ANS_SEL_OPT_ID, ans.getSelectedOptionId());
                    acv.put(COL_ANS_SEL_OPT_TEXT, ans.getSelectedOptionText());
                    acv.put(COL_ANS_CORR_OPT_TEXT, ans.getCorrectOptionText());
                    acv.put(COL_ANS_IS_CORRECT, ans.isCorrect() ? 1 : 0);
                    acv.put(COL_ANS_MARKS_OBT, ans.getMarksObtained());
                    acv.put(COL_ANS_MAX_MARKS, ans.getMaxMarks());
                    acv.put(COL_ANS_EXPLANATION, ans.getExplanation());
                    db.insert(TABLE_ATTEMPT_ANSWERS, null, acv);
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return attemptId;
    }

    /**
     * Retrieves attempt by ID.
     */
    public QuizAttempt getAttemptById(int attemptId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ATTEMPTS, null,
                COL_ID + "=?", new String[]{String.valueOf(attemptId)},
                null, null, null);

        QuizAttempt att = null;
        if (cursor != null && cursor.moveToFirst()) {
            att = new QuizAttempt();
            att.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
            att.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ATT_USER_ID)));
            att.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(COL_ATT_USERNAME)));
            att.setCategoryId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ATT_CAT_ID)));
            att.setCategoryName(cursor.getString(cursor.getColumnIndexOrThrow(COL_ATT_CAT_NAME)));
            att.setScore(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ATT_SCORE)));
            att.setTotalMarks(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ATT_TOTAL_MARKS)));
            att.setPercentage(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_ATT_PERCENTAGE)));
            att.setPassed(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ATT_PASSED)) == 1);
            att.setTimeTakenSeconds(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ATT_TIME_TAKEN)));
            att.setDateTaken(cursor.getString(cursor.getColumnIndexOrThrow(COL_ATT_DATE)));
            cursor.close();
        }
        return att;
    }

    /**
     * Retrieves all answers for an attempt to render post-exam audit review.
     */
    public List<AttemptAnswer> getAttemptAnswers(int attemptId) {
        List<AttemptAnswer> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ATTEMPT_ANSWERS, null,
                COL_ANS_ATT_ID + "=?", new String[]{String.valueOf(attemptId)},
                null, null, COL_ID + " ASC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                AttemptAnswer ans = new AttemptAnswer();
                ans.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
                ans.setAttemptId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ANS_ATT_ID)));
                ans.setQuestionId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ANS_Q_ID)));
                ans.setQuestionText(cursor.getString(cursor.getColumnIndexOrThrow(COL_ANS_Q_TEXT)));
                ans.setSelectedOptionId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ANS_SEL_OPT_ID)));
                ans.setSelectedOptionText(cursor.getString(cursor.getColumnIndexOrThrow(COL_ANS_SEL_OPT_TEXT)));
                ans.setCorrectOptionText(cursor.getString(cursor.getColumnIndexOrThrow(COL_ANS_CORR_OPT_TEXT)));
                ans.setCorrect(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ANS_IS_CORRECT)) == 1);
                ans.setMarksObtained(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ANS_MARKS_OBT)));
                ans.setMaxMarks(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ANS_MAX_MARKS)));
                ans.setExplanation(cursor.getString(cursor.getColumnIndexOrThrow(COL_ANS_EXPLANATION)));
                list.add(ans);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    /**
     * Retrieves all attempts by a specific user for student dashboard.
     */
    public List<QuizAttempt> getUserAttempts(int userId) {
        List<QuizAttempt> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ATTEMPTS, null,
                COL_ATT_USER_ID + "=?", new String[]{String.valueOf(userId)},
                null, null, COL_ID + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                QuizAttempt att = new QuizAttempt();
                att.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
                att.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ATT_USER_ID)));
                att.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(COL_ATT_USERNAME)));
                att.setCategoryId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ATT_CAT_ID)));
                att.setCategoryName(cursor.getString(cursor.getColumnIndexOrThrow(COL_ATT_CAT_NAME)));
                att.setScore(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ATT_SCORE)));
                att.setTotalMarks(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ATT_TOTAL_MARKS)));
                att.setPercentage(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_ATT_PERCENTAGE)));
                att.setPassed(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ATT_PASSED)) == 1);
                att.setTimeTakenSeconds(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ATT_TIME_TAKEN)));
                att.setDateTaken(cursor.getString(cursor.getColumnIndexOrThrow(COL_ATT_DATE)));
                list.add(att);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    /**
     * Retrieves all student attempts for the Admin portal.
     */
    public List<QuizAttempt> getAllAttempts() {
        List<QuizAttempt> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ATTEMPTS, null,
                null, null, null, null, COL_ID + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                QuizAttempt att = new QuizAttempt();
                att.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
                att.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ATT_USER_ID)));
                att.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(COL_ATT_USERNAME)));
                att.setCategoryId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ATT_CAT_ID)));
                att.setCategoryName(cursor.getString(cursor.getColumnIndexOrThrow(COL_ATT_CAT_NAME)));
                att.setScore(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ATT_SCORE)));
                att.setTotalMarks(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ATT_TOTAL_MARKS)));
                att.setPercentage(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_ATT_PERCENTAGE)));
                att.setPassed(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ATT_PASSED)) == 1);
                att.setTimeTakenSeconds(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ATT_TIME_TAKEN)));
                att.setDateTaken(cursor.getString(cursor.getColumnIndexOrThrow(COL_ATT_DATE)));
                list.add(att);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    /**
     * Retrieves ranked leaderboard. If categoryId > 0, filters by category.
     */
    public List<QuizAttempt> getLeaderboard(int categoryId) {
        List<QuizAttempt> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query;
        String[] args;
        if (categoryId > 0) {
            query = "SELECT * FROM " + TABLE_ATTEMPTS + " WHERE " + COL_ATT_CAT_ID + "=? ORDER BY " +
                    COL_ATT_PERCENTAGE + " DESC, " + COL_ATT_SCORE + " DESC, " + COL_ATT_TIME_TAKEN + " ASC LIMIT 50";
            args = new String[]{String.valueOf(categoryId)};
        } else {
            query = "SELECT * FROM " + TABLE_ATTEMPTS + " ORDER BY " +
                    COL_ATT_PERCENTAGE + " DESC, " + COL_ATT_SCORE + " DESC, " + COL_ATT_TIME_TAKEN + " ASC LIMIT 50";
            args = null;
        }

        Cursor cursor = db.rawQuery(query, args);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                QuizAttempt att = new QuizAttempt();
                att.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
                att.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ATT_USER_ID)));
                att.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(COL_ATT_USERNAME)));
                att.setCategoryId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ATT_CAT_ID)));
                att.setCategoryName(cursor.getString(cursor.getColumnIndexOrThrow(COL_ATT_CAT_NAME)));
                att.setScore(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ATT_SCORE)));
                att.setTotalMarks(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ATT_TOTAL_MARKS)));
                att.setPercentage(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_ATT_PERCENTAGE)));
                att.setPassed(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ATT_PASSED)) == 1);
                att.setTimeTakenSeconds(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ATT_TIME_TAKEN)));
                att.setDateTaken(cursor.getString(cursor.getColumnIndexOrThrow(COL_ATT_DATE)));
                list.add(att);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    /**
     * Admin: Insert a new Category.
     */
    public boolean insertCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_CAT_NAME, category.getName());
        cv.put(COL_CAT_SLUG, category.getSlug());
        cv.put(COL_CAT_DESC, category.getDescription());
        cv.put(COL_CAT_ICON, category.getIconName());
        cv.put(COL_CAT_TIME_LIMIT, category.getTimeLimitMins());
        cv.put(COL_CAT_PASS_PCT, category.getPassPercentage());
        return db.insert(TABLE_CATEGORIES, null, cv) != -1;
    }

    /**
     * Admin: Delete Category by ID.
     */
    public boolean deleteCategory(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_CATEGORIES, COL_ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    /**
     * Admin: Insert a Question along with its 4 Options.
     */
    public boolean insertQuestion(Question question, List<Option> options) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues qcv = new ContentValues();
            qcv.put(COL_Q_CAT_ID, question.getCategoryId());
            qcv.put(COL_Q_TEXT, question.getText());
            qcv.put(COL_Q_MARKS, question.getMarks());
            qcv.put(COL_Q_EXPLANATION, question.getExplanation());
            long qId = db.insert(TABLE_QUESTIONS, null, qcv);

            if (qId != -1) {
                for (Option opt : options) {
                    ContentValues ocv = new ContentValues();
                    ocv.put(COL_OPT_Q_ID, qId);
                    ocv.put(COL_OPT_TEXT, opt.getText());
                    ocv.put(COL_OPT_IS_CORRECT, opt.isCorrect() ? 1 : 0);
                    db.insert(TABLE_OPTIONS, null, ocv);
                }
                db.setTransactionSuccessful();
                return true;
            }
        } finally {
            db.endTransaction();
        }
        return false;
    }
}
