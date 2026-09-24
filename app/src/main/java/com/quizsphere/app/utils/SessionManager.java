package com.quizsphere.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.quizsphere.app.models.User;

/**
 * ==============================================================================
 * Utility: SessionManager
 * Description: Manages user login state, active user profile, and role-based
 * access permissions using Android SharedPreferences (Slide 3 & 10).
 * ==============================================================================
 */
public class SessionManager {

    // Shared Preferences file name
    private static final String PREF_NAME = "QuizSphereSession";

    // Keys for storing user session attributes
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_ROLE = "role";

    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;
    private final Context context;

    // Constructor initializing SharedPreferences
    public SessionManager(Context context) {
        this.context = context;
        this.pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.editor = pref.edit();
    }

    /**
     * Creates a login session when a user signs in successfully.
     * @param user The authenticated User instance.
     */
    public void createLoginSession(User user) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_ROLE, user.getRole());
        editor.apply();
    }

    /**
     * Checks if a user is currently logged into the app.
     * @return true if logged in, false otherwise.
     */
    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    /**
     * Checks if the currently active user has Admin / Educator role privileges.
     * @return true if admin, false otherwise.
     */
    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(pref.getString(KEY_ROLE, "student"));
    }

    /**
     * Retrieves the current logged-in user object.
     * @return User object populated from session values.
     */
    public User getCurrentUser() {
        if (!isLoggedIn()) {
            return null;
        }
        User user = new User();
        user.setId(pref.getInt(KEY_USER_ID, -1));
        user.setUsername(pref.getString(KEY_USERNAME, ""));
        user.setEmail(pref.getString(KEY_EMAIL, ""));
        user.setRole(pref.getString(KEY_ROLE, "student"));
        return user;
    }

    /**
     * Logs out the user and clears all cached session tokens and credentials.
     */
    public void logout() {
        editor.clear();
        editor.apply();
    }
}
