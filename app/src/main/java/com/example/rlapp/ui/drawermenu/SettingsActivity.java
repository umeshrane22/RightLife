package com.example.rlapp.ui.drawermenu;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rlapp.R;

public class SettingsActivity extends AppCompatActivity {
    private LinearLayout llAboutUs, llNotifications, llFAQ, llContactUs, llManageSubscription, llTheme, llTermsAndConditions,
            llPrivacyPolicy, llAccount, llLogOut;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        findViewById(R.id.ic_back_dialog).setOnClickListener(view -> finish());

        llAboutUs = findViewById(R.id.ll_about_us);
        llNotifications = findViewById(R.id.ll_notifications);
        llFAQ = findViewById(R.id.ll_FAQ);
        llContactUs = findViewById(R.id.ll_contact_us);
        llManageSubscription = findViewById(R.id.ll_manage_subs);
        llTheme = findViewById(R.id.ll_theme);
        llTermsAndConditions = findViewById(R.id.ll_terms_conditions);
        llPrivacyPolicy = findViewById(R.id.ll_privacy_policy);
        llAccount = findViewById(R.id.ll_account);
        llLogOut = findViewById(R.id.ll_logout);

        llAboutUs.setOnClickListener(view -> {
            Intent intent = new Intent(SettingsActivity.this, AboutUsActivity.class);
            startActivity(intent);
        });
        llNotifications.setOnClickListener(view -> {
            Intent intent = new Intent(SettingsActivity.this, NotificationsSettingActivity.class);
            startActivity(intent);
        });
        llFAQ.setOnClickListener(view -> {
            Intent intent = new Intent(SettingsActivity.this, FAQActivity.class);
            startActivity(intent);
        });
        llContactUs.setOnClickListener(view -> {
            Intent intent = new Intent(SettingsActivity.this, ContactUsActivity.class);
            startActivity(intent);
        });
        llManageSubscription.setOnClickListener(view -> {
            Intent intent = new Intent(SettingsActivity.this, AboutUsActivity.class);
            startActivity(intent);
        });
        llTheme.setOnClickListener(view -> {
            Intent intent = new Intent(SettingsActivity.this, AboutUsActivity.class);
            startActivity(intent);
        });
        llTermsAndConditions.setOnClickListener(view -> {
            Intent intent = new Intent(SettingsActivity.this, TermsAndConditionsActivity.class);
            startActivity(intent);
        });
        llPrivacyPolicy.setOnClickListener(view -> {
            Intent intent = new Intent(SettingsActivity.this, PrivacyPolicyActivity.class);
            startActivity(intent);
        });
        llAccount.setOnClickListener(view -> {
            Intent intent = new Intent(SettingsActivity.this, AccountActivity.class);
            startActivity(intent);
        });
        llLogOut.setOnClickListener(view -> {
            Intent intent = new Intent(SettingsActivity.this, AboutUsActivity.class);
            startActivity(intent);
        });
    }

    private void dialogLogout(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_alert);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        // Set the dim amount
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.dimAmount = 0.7f; // Adjust the dim amount (0.0 - 1.0)
        window.setAttributes(layoutParams);

        Button btnStay = dialog.findViewById(R.id.btn_stay);
        btnStay.setOnClickListener(view -> {
            dialog.dismiss();
        });

        Button btnLogout = dialog.findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(view -> {
           logOut();
        });
        dialog.show();
    }

    private void logOut() {

    }
}
