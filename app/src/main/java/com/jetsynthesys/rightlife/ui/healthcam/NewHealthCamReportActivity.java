package com.jetsynthesys.rightlife.ui.healthcam;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.jetsynthesys.rightlife.R;
import com.jetsynthesys.rightlife.RetrofitData.ApiClient;
import com.jetsynthesys.rightlife.RetrofitData.ApiService;
import com.jetsynthesys.rightlife.apimodel.newreportfacescan.FacialReportResponseNew;
import com.jetsynthesys.rightlife.apimodel.newreportfacescan.HealthCamItem;
import com.jetsynthesys.rightlife.apimodel.userdata.UserProfileResponse;
import com.jetsynthesys.rightlife.apimodel.userdata.Userdata;
import com.jetsynthesys.rightlife.databinding.ActivityNewhealthcamreportBinding;
import com.jetsynthesys.rightlife.ui.CommonAPICall;
import com.jetsynthesys.rightlife.ui.healthaudit.HealthCamActivity;
import com.jetsynthesys.rightlife.ui.utility.AppConstants;
import com.jetsynthesys.rightlife.ui.utility.DateTimeUtils;
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceConstants;
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager;
import com.jetsynthesys.rightlife.ui.utility.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewHealthCamReportActivity extends AppCompatActivity {
    private static final String TAG = "NewHealthCamReportActivity";
    ActivityNewhealthcamreportBinding binding;
    private FacialReportResponseNew facialReportResponseNew;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newhealthcamreport);
        binding = ActivityNewhealthcamreportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        findViewById(R.id.ic_back_dialog).setOnClickListener(view -> {
            finish();
        });
        binding.icCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDisclaimerDialog();
            }
        });
        binding.cardviewLastCheckin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (facialReportResponseNew.data.lastCheckin) {

                }
                startActivity(new Intent(NewHealthCamReportActivity.this, HealthCamBasicDetailsActivity.class));
            }
        });
        getMyRLHealthCamResult();

        UserProfileResponse userProfileResponse = SharedPreferenceManager.getInstance(this).getUserProfile();
        if (userProfileResponse == null) return;
        Userdata userdata = userProfileResponse.getUserdata();
        /*if (userdata != null) {
            binding.txtuserName.setText("Hi " + userdata.getFirstName());
        }*/
        updateChecklistStatus();
    }

    private void updateChecklistStatus() {
        CommonAPICall.INSTANCE.updateChecklistStatus(this, "vital_facial_scan", AppConstants.CHECKLIST_COMPLETED);
    }

    private void getMyRLHealthCamResult() {
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString(SharedPreferenceConstants.ACCESS_TOKEN, null);
        Utils.showLoader(this);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<ResponseBody> call = apiService.getMyRLHealthCamResult(accessToken);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Utils.dismissLoader(NewHealthCamReportActivity.this);
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(NewHealthCamReportActivity.this, "Success: " + response.code(), Toast.LENGTH_SHORT).show();
                    try {
                        String jsonString = response.body().string();
                        Log.d("Response Body", " My RL HEalth Cam Result - " + jsonString);
                        Gson gson = new Gson();
                        facialReportResponseNew = gson.fromJson(jsonString, FacialReportResponseNew.class);
                        HandleNewReportUI(facialReportResponseNew);
                        HandleContinueWatchUI(facialReportResponseNew);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    //   Toast.makeText(RLPageActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    Log.d("MyRLHealthCamResult", "Error:" + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Utils.dismissLoader(NewHealthCamReportActivity.this);
                Toast.makeText(NewHealthCamReportActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void HandleNewReportUI(FacialReportResponseNew facialReportResponseNew) {
        if (facialReportResponseNew.success) {

            //binding.txtWellnessScore1.setText(String.valueOf(facialReportResponseNew.data.overallWellnessScore.value));
            if (facialReportResponseNew.data.overallWellnessScore != null) {
                binding.txtWellnessScore1.setText(String.format("%.2f", facialReportResponseNew.data.overallWellnessScore.value));
                setTextAccordingToWellnessScore(facialReportResponseNew.data.overallWellnessScore.value);
                binding.halfCurveProgressBar.setProgress(facialReportResponseNew.data.overallWellnessScore.value.floatValue());
            }


            binding.txtAlertMessage.setText(facialReportResponseNew.data.summary);
            binding.tvLastReportDate.setText(DateTimeUtils.convertAPIDateMonthFormatWithTime(facialReportResponseNew.data.createdAt));
            /*if (facialReportResponseNew.data.lastCheckin) {
                binding.cardviewLastCheckin.setVisibility(View.VISIBLE);
            } else {
                binding.cardviewLastCheckin.setVisibility(View.GONE);
            }
            */
            //now show always and handle conditions depend on how many scan pending , will know from backend
            binding.cardviewLastCheckin.setVisibility(View.VISIBLE);
            // list

            List<HealthCamItem> healthCamGoodItems = facialReportResponseNew.data.healthCamReportByCategory.healthCamGood;
            List<HealthCamItem> healthCamPayAttentionItems = facialReportResponseNew.data.healthCamReportByCategory.healthCamPayAttention;

// Combine the lists if you want to display them together
            List<HealthCamItem> allHealthCamItems = new ArrayList<>();
            allHealthCamItems.addAll(healthCamGoodItems);
            allHealthCamItems.addAll(healthCamPayAttentionItems);

            HealthCamVitalsAdapter adapter = new HealthCamVitalsAdapter(this, allHealthCamItems);

            binding.recyclerViewVitalCards.setLayoutManager(new GridLayoutManager(this, 2)); // 2 columns
            binding.recyclerViewVitalCards.setAdapter(adapter);
        }
    }

    private void setTextAccordingToWellnessScore(Double wellnessScore) {
        String message;

        if (wellnessScore >= 0 && wellnessScore <= 19.99) {
            message = "Your wellness needs urgent attention—let’s work on getting you back on track.";
        } else if (wellnessScore <= 40.0) {
            message = "Your wellness is under strain—time to make a few adjustments for improvement.";
        } else if (wellnessScore <= 60.0) {
            message = "You’re doing okay, but there’s room to optimize your health and resilience.";
        } else if (wellnessScore <= 80.0) {
            message = "Great job! Your vitals are strong, and you’re building a solid foundation.";
        } else if (wellnessScore <= 100.0) {
            message = "You're thriving! Your vitals are at their peak—keep up the fantastic work.";
        } else {
            message = "Invalid wellness score.";
        }

        binding.txtWellStreak.setText(message);
    }

    private void HandleContinueWatchUI(FacialReportResponseNew facialReportResponseNew) {
        if (facialReportResponseNew.data.recommendation.size() > 0) {
            //txt_continue_view_header.setVisibility(View.VISIBLE);
            HealthCamRecommendationAdapter adapter = new HealthCamRecommendationAdapter(this, facialReportResponseNew.data.recommendation);
            LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            binding.recyclerViewContinue.setLayoutManager(horizontalLayoutManager);
            binding.recyclerViewContinue.setAdapter(adapter);
        } else {
            //txt_continue_view_header.setVisibility(View.GONE);
        }

    }

    private void showDisclaimerDialog() {
        // Create the dialog
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_disclaimer_facescan_result);
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

        // Optional: Set dynamic content
        // dialogText.setText("Please find a quiet and comfortable place before starting");

        // Set button click listener
        dialogButtonStay.setOnClickListener(v -> {
            // Perform your action
            dialog.dismiss();
        });
        // Show the dialog
        dialog.show();
    }

}
