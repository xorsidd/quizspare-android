package com.quizsphere.app.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.quizsphere.app.R;
import com.quizsphere.app.adapters.CategoryAdapter;
import com.quizsphere.app.database.QuizSphereDbHelper;
import com.quizsphere.app.models.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * ==============================================================================
 * Activity: CategoryListActivity
 * Description: Displays all available quiz subject categories with dynamic search.
 * ==============================================================================
 */
public class CategoryListActivity extends AppCompatActivity {

    private QuizSphereDbHelper dbHelper;
    private CategoryAdapter categoryAdapter;
    private List<Category> allCategories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Quiz Subjects");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dbHelper = QuizSphereDbHelper.getInstance(this);

        EditText etSearch = findViewById(R.id.etCatSearch);
        RecyclerView rvCategories = findViewById(R.id.rvAllCategories);
        rvCategories.setLayoutManager(new LinearLayoutManager(this));

        allCategories = dbHelper.getAllCategories();
        categoryAdapter = new CategoryAdapter(this, allCategories);
        rvCategories.setAdapter(categoryAdapter);

        // Real-time search filter
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterCategories(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filterCategories(String query) {
        if (query == null || query.trim().isEmpty()) {
            categoryAdapter.updateData(allCategories);
            return;
        }

        String lower = query.toLowerCase().trim();
        List<Category> filtered = new ArrayList<>();
        for (Category c : allCategories) {
            if (c.getName().toLowerCase().contains(lower) ||
                    (c.getDescription() != null && c.getDescription().toLowerCase().contains(lower))) {
                filtered.add(c);
            }
        }
        categoryAdapter.updateData(filtered);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
