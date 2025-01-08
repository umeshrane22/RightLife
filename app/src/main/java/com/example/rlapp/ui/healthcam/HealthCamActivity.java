package com.example.rlapp.ui.healthcam;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.rlapp.R;
import com.example.rlapp.RetrofitData.ApiClient;
import com.example.rlapp.RetrofitData.ApiService;
import com.example.rlapp.ui.healthaudit.HealthAuditPagerAdapter;
import com.example.rlapp.ui.healthaudit.questionlist.QuestionListHealthAudit;
import com.example.rlapp.ui.payment.AccessPaymentActivity;
import com.example.rlapp.ui.utility.SharedPreferenceConstants;
import com.example.rlapp.ui.voicescan.VoiceScanFromActivity;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.zhpan.indicator.IndicatorView;

import me.relex.circleindicator.CircleIndicator3;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HealthCamActivity extends AppCompatActivity {

    ImageView ic_back_dialog, close_dialog;
    HealthCamPagerAdapter adapter;
    Button btn_howitworks;
    /**
     * {@inheritDoc}
     * <p>
     * Perform initialization of all fragments.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthcam);

        ViewPager2 viewPager = findViewById(R.id.view_pager);
        ic_back_dialog = findViewById(R.id.ic_back_dialog);
        close_dialog = findViewById(R.id.ic_close_dialog);
        btn_howitworks = findViewById(R.id.btn_howitworks);

        CircleIndicator3 indicator = findViewById(R.id.indicator);
       IndicatorView indicator_view = findViewById(R.id.indicator_view);
       indicator_view.setSliderHeight(21);
       indicator_view.setSliderWidth(80);
        // Array of layout resources to use in the ViewPager
        int[] layouts = {
                R.layout.page_one_health_cam, // Define these layout files in your res/layout directory
        };

        // Set up the adapter
        adapter = new HealthCamPagerAdapter(layouts);

        viewPager.setAdapter(adapter);
        indicator.setViewPager(viewPager);
        indicator_view.setupWithViewPager(viewPager);

       // showCustomDialog();


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
            //finish();
            showExitDialog();
        });

        btn_howitworks.setOnClickListener(view -> {
            int currentItem = viewPager.getCurrentItem();
            int totalItems = adapter.getItemCount();
            // Go to the next page if it's not the last one
            if (currentItem < totalItems - 1) {
                viewPager.setCurrentItem(currentItem + 1);
            } else {
                showDisclaimerDialog();
            }
        });

        // Add page change callback to update button text
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateButtonText(position);
            }
        });
    }

    // Method to update button text based on the current page
    private void updateButtonText(int position) {
        int totalItems = adapter.getItemCount();

        if (position == totalItems - 1) {
            btn_howitworks.setText("Start Now");
        } else {
            btn_howitworks.setText("Start Now");
        }
    }

    private void showExitDialog() {
        // Create the dialog
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_exit_dialog_mind);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = dialog.getWindow();
        // Set the dim amount
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.dimAmount = 0.7f; // Adjust the dim amount (0.0 - 1.0)
        window.setAttributes(layoutParams);

        // Find views from the dialog layout
        //ImageView dialogIcon = dialog.findViewById(R.id.img_close_dialog);
        ImageView dialogImage = dialog.findViewById(R.id.dialog_image);
        TextView dialogText = dialog.findViewById(R.id.dialog_text);
        Button dialogButtonStay = dialog.findViewById(R.id.dialog_button_stay);
        Button dialogButtonExit = dialog.findViewById(R.id.dialog_button_exit);

        // Set button click listener
        dialogButtonStay.setOnClickListener(v -> {
            // Perform your action
            dialog.dismiss();
        });
        dialogButtonExit.setOnClickListener(v -> {
            dialog.dismiss();
            this.finish();
        });

        // Show the dialog
        dialog.show();
    }

    private void showDisclaimerDialog() {
        // Create the dialog
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_disclaimer_health_cam);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Window window = dialog.getWindow();
        // Set the dim amount
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.dimAmount = 0.85f; // Adjust the dim amount (0.0 - 1.0)
        window.setAttributes(layoutParams);

        // Find views from the dialog layout
        ImageView dialogImage = dialog.findViewById(R.id.dialog_image);
        TextView dialogText = dialog.findViewById(R.id.dialog_text);
        Button dialogButtonStay = dialog.findViewById(R.id.dialog_button_stay);
        Button dialogButtonExit = dialog.findViewById(R.id.dialog_button_exit);

        // Set button click listener
        dialogButtonStay.setOnClickListener(v -> {
            // Perform your action
            dialog.dismiss();
            Intent intent = new Intent(HealthCamActivity.this, HealthCamBasicDetailsActivity.class);
            startActivity(intent);
        });
        dialogButtonExit.setOnClickListener(v -> {
            dialog.dismiss();
            this.finish();
        });

        // Show the dialog
        dialog.show();
    }

}
