package com.example.rlapp.ui.voicescan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.rlapp.R;
import com.example.rlapp.ui.healthaudit.FormPagerAdapter;
import com.example.rlapp.ui.healthaudit.HealthAuditFormActivity;
import com.example.rlapp.ui.payment.AccessPaymentActivity;

public class VoiceScanFromActivity extends AppCompatActivity {

    ImageView ic_back_dialog, close_dialog;
    private ViewPager2 viewPager;
    private Button prevButton, nextButton, submitButton;
    private VoiceScanFormPagerAdapter adapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_voice_scan_from);


        ic_back_dialog = findViewById(R.id.ic_back_dialog);
        close_dialog = findViewById(R.id.ic_close_dialog);
        viewPager = findViewById(R.id.viewPager);
        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);
        submitButton = findViewById(R.id.submitButton);

        progressBar = findViewById(R.id.progressBar);

        adapter = new VoiceScanFormPagerAdapter(this);
        viewPager.setAdapter(adapter);

        prevButton.setOnClickListener(v -> navigateToPreviousPage());
        nextButton.setOnClickListener(v -> navigateToNextPage("Sad"));
        submitButton.setOnClickListener(v -> submitFormData());
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VoiceScanFromActivity.this, AccessPaymentActivity.class);
                startActivity(intent);
            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateButtonVisibility(position);
                updateProgress(position);
            }
        });


        ic_back_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentItem = viewPager.getCurrentItem();
                int totalItems = adapter.getItemCount();

                if (currentItem == 0) {
                    finish();
                }
                // If on any other page, move to the previous page
                else {
                    viewPager.setCurrentItem(currentItem - 1);
                }
            }
        });


        close_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //finish();
                // showExitDialog();
            }
        });

    }

    private void updateProgress(int fragmentIndex) {
        // Set progress percentage based on the current fragment (out of 8)
        int progressPercentage = (int) (((fragmentIndex + 1) / (double)adapter.getItemCount()) * 100);
        progressBar.setProgress(progressPercentage);
    }
    private void updateButtonVisibility(int position) {
        int totalItems = adapter.getItemCount();

        if (position == totalItems - 1) {
            nextButton.setText("Begin Recording");
        } else {
            nextButton.setText("Next Page");
        }
    }

    private void submitFormData() {
    }


    private void navigateToPreviousPage() {
        if (viewPager.getCurrentItem() > 0) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    public void navigateToNextPage(String feelings) {
        /*if (viewPager.getCurrentItem() < adapter.getItemCount() - 1) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        }*/

        int currentItem = viewPager.getCurrentItem();
        int totalItems = adapter.getItemCount();
        // Go to the next page if it's not the last one
        if (currentItem < totalItems - 1) {
            adapter.setFeelings(feelings);
            viewPager.setCurrentItem(currentItem + 1);
        } else {
            // If it's the last page, got to scan
             //Toast.makeText(VoiceScanFromActivity.this, "strat Recording", Toast.LENGTH_SHORT).show();
            //showBirthDayDialog();
            Intent intent = new Intent(VoiceScanFromActivity.this, AccessPaymentActivity.class);
            // Optionally pass data
            //intent.putExtra("key", "value");
            startActivity(intent);
        }
    }
}