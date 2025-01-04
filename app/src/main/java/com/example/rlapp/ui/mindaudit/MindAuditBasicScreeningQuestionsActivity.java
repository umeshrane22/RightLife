package com.example.rlapp.ui.mindaudit;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.rlapp.R;

public class MindAuditBasicScreeningQuestionsActivity extends AppCompatActivity {
    public Button nextButton, submitButton;
    ImageView ic_back_dialog, close_dialog;
    private ViewPager2 viewPager;
    private Button prevButton;
    private ProgressBar progressBar;
    private MindAuditBasicQuestionsAdapter adapter;
    private BasicScreeningQuestion basicScreeningQuestions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mind_audit_from);
        ic_back_dialog = findViewById(R.id.ic_back_dialog);
        close_dialog = findViewById(R.id.ic_close_dialog);
        viewPager = findViewById(R.id.viewPager);
        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);
        submitButton = findViewById(R.id.submitButton);
        progressBar = findViewById(R.id.progressBar);

        basicScreeningQuestions = (BasicScreeningQuestion) getIntent().getSerializableExtra(MindAuditFeelingsFragment.ARG_BASIC_QUESTION);

        adapter = new MindAuditBasicQuestionsAdapter(this);
        adapter.setData(basicScreeningQuestions);
        viewPager.setAdapter(adapter);

        prevButton.setOnClickListener(v -> navigateToPreviousPage());
        nextButton.setOnClickListener(v -> navigateToNextPage());

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateButtonVisibility(position);
                updateProgress(position);
            }
        });

        ic_back_dialog.setOnClickListener(view -> {
            int currentItem = viewPager.getCurrentItem();
            int totalItems = adapter.getItemCount();

            if (currentItem == 0) {
                finish();
            }
            // If on any other page, move to the previous page
            else {
                viewPager.setCurrentItem(currentItem - 1);
            }
        });
        close_dialog.setOnClickListener(view -> {
            finish();
        });
    }

    private void updateButtonVisibility(int position) {
        int totalItems = adapter.getItemCount();

        if (position == totalItems - 1) {
            nextButton.setText("Submit");
        } else {
            nextButton.setText("Next");
        }
    }

    private void navigateToPreviousPage() {
        if (viewPager.getCurrentItem() > 0) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    public void navigateToNextPage() {

        int currentItem = viewPager.getCurrentItem();
        int totalItems = adapter.getItemCount();
        // Go to the next page if it's not the last one
        if (currentItem < totalItems - 1) {
            viewPager.setCurrentItem(currentItem + 1);
        }
    }

    private void updateProgress(int fragmentIndex) {
        // Set progress percentage based on the current fragment (out of 8)
        int progressPercentage = (int) (((fragmentIndex + 1) / (double) adapter.getItemCount()) * 100);
        progressBar.setProgress(progressPercentage);
    }
}
