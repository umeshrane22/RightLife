package com.example.rlapp.ui.drawermenu;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rlapp.MainActivity;
import com.example.rlapp.R;
import com.example.rlapp.RetrofitData.ApiClient;
import com.example.rlapp.RetrofitData.ApiService;
import com.example.rlapp.RetrofitData.LogoutUserRequest;
import com.example.rlapp.apimodel.PromotionResponse;
import com.example.rlapp.ui.HomeActivity;
import com.example.rlapp.ui.new_design.ImageSliderActivity;
import com.example.rlapp.ui.utility.SharedPreferenceConstants;
import com.example.rlapp.ui.utility.SharedPreferenceManager;
import com.example.rlapp.ui.utility.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
            Intent intent = new Intent(SettingsActivity.this, ThemeActivity.class);
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
            dialogLogout();
        });
    }

    private void dialogLogout() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_alert);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = dialog.getWindow();
        // Set the dim amount
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.dimAmount = 0.7f; // Adjust the dim amount (0.0 - 1.0)
        window.setAttributes(layoutParams);

        dialog.findViewById(R.id.iv_dialog_close).setOnClickListener(view -> dialog.dismiss());

        dialog.findViewById(R.id.btn_stay).setOnClickListener(view -> dialog.dismiss());

        dialog.findViewById(R.id.btn_logout).setOnClickListener(view -> logOut());
        dialog.show();
    }

    private void logOut() {
        postUserLogout("");
    }


    private void postUserLogout(String s) {
        //-----------

        String accessToken = SharedPreferenceManager.getInstance(this).getAccessToken();

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        // Create a request body (replace with actual email and phone number)
        String deviceId = Utils.getDeviceId(this);
         LogoutUserRequest request = new LogoutUserRequest();
         request.setDeviceId(deviceId);

        // Make the API call
        Call<JsonElement> call = apiService.LogoutUser(accessToken,request);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonElement promotionResponse2 = response.body();
                    Log.d("API Response", "Logout: " + promotionResponse2.toString());
                    Gson gson = new Gson();
                    String jsonResponse = gson.toJson(response.body());
//                    PromotionResponse promotionResponse = gson.fromJson(jsonResponse, PromotionResponse.class);
                    Log.d("API Response body", "Success: Logout " + jsonResponse);
                    clearUserDataAndFinish();

                } else {
                      Toast.makeText(SettingsActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(SettingsActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API ERROR", "onFailure: " + t.getMessage());
                t.printStackTrace();  // Print the full stack trace for more details

            }
        });

    }

    private void clearUserDataAndFinish() {
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        SharedPreferenceManager.getInstance(this).clearData();

        Intent intent = new Intent(this, ImageSliderActivity.class);
        startActivity(intent);

        finishAffinity();
    }

}
