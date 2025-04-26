package com.jetsynthesys.rightlife.ui.healthcam;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
import com.jetsynthesys.rightlife.databinding.ItemScanCircleBinding;
import com.jetsynthesys.rightlife.databinding.LayoutScanProgressBinding;
import com.jetsynthesys.rightlife.newdashboard.model.DashboardChecklistManager;
import com.jetsynthesys.rightlife.ui.CommonAPICall;
import com.jetsynthesys.rightlife.ui.utility.AppConstants;
import com.jetsynthesys.rightlife.ui.utility.ConversionUtils;
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
    LayoutScanProgressBinding scanBinding;
    List<HealthCamItem> allHealthCamItems = new ArrayList<>();
    private FacialReportResponseNew facialReportResponseNew;
    private String reportId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newhealthcamreport);
        binding = ActivityNewhealthcamreportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        scanBinding = LayoutScanProgressBinding.bind(binding.scanProgressLayout.getRoot());
        reportId = getIntent().getStringExtra("REPORT_ID");

        findViewById(R.id.ic_back_dialog).setOnClickListener(view -> finish());
        binding.icCloseDialog.setOnClickListener(v -> showDisclaimerDialog());
        binding.cardviewLastCheckin.setOnClickListener(v -> {
            startActivity(new Intent(NewHealthCamReportActivity.this, HealthCamBasicDetailsActivity.class));
        });

        binding.btnBuyFacescan.setOnClickListener(v -> {
            // put check here if facescan remaning count is 0 buy new else Scan again
            if (facialReportResponseNew.data.boosterLimit > 0 && facialReportResponseNew.data.boosterUsed < facialReportResponseNew.data.boosterLimit) {
                startActivity(new Intent(NewHealthCamReportActivity.this, HealthCamBasicDetailsActivity.class));
            } else {
                Toast.makeText(NewHealthCamReportActivity.this, "Buy New Process here...", Toast.LENGTH_SHORT).show();
            }

        });
        binding.scanProgressLayout.btnScanAgain.setOnClickListener(v -> startActivity(new Intent(NewHealthCamReportActivity.this, HealthCamBasicDetailsActivity.class)));
        getMyRLHealthCamResult();

        UserProfileResponse userProfileResponse = SharedPreferenceManager.getInstance(this).getUserProfile();
        if (userProfileResponse == null) return;
        Userdata userdata = userProfileResponse.getUserdata();
        /*if (userdata != null) {
            binding.txtuserName.setText("Hi " + userdata.getFirstName());
        }*/
        updateChecklistStatus();
        if (DashboardChecklistManager.INSTANCE.getPaymentStatus()) {
            binding.cardFacescanBooster.setVisibility(View.GONE);
            binding.scanProgressLayout.scanContainer.setVisibility(View.VISIBLE);
        } else {
            binding.cardFacescanBooster.setVisibility(View.VISIBLE);
            binding.scanProgressLayout.scanContainer.setVisibility(View.GONE);
        }

        binding.btnSyncNow.setOnClickListener(v -> DownLaodReport(facialReportResponseNew.data.pdf));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getMyRLHealthCamResult();
    }

    private void updateChecklistStatus() {
        CommonAPICall.INSTANCE.updateChecklistStatus(this, "vital_facial_scan", AppConstants.CHECKLIST_COMPLETED);
    }

    private void getMyRLHealthCamResult() {
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString(SharedPreferenceConstants.ACCESS_TOKEN, null);
        Utils.showLoader(this);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<ResponseBody> call;
        if (reportId != null && !reportId.isEmpty())
            call = apiService.getHealthCamByReportId(accessToken, reportId);
        else
            call = apiService.getMyRLHealthCamResult(accessToken);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Utils.dismissLoader(NewHealthCamReportActivity.this);
                if (response.isSuccessful() && response.body() != null) {
                    //Toast.makeText(NewHealthCamReportActivity.this, "Success: " + response.code(), Toast.LENGTH_SHORT).show();
                    try {
                        String jsonString = response.body().string();
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

            if (facialReportResponseNew.data.overallWellnessScore != null) {
                String formatted = ConversionUtils.decimalFormat0Decimal.format(facialReportResponseNew.data.overallWellnessScore.value);
                Log.d("FormattedValue", formatted);
                binding.txtWellnessScore1.setText(formatted);
                setTextAccordingToWellnessScore(facialReportResponseNew.data.overallWellnessScore.value);
                binding.halfCurveProgressBar.setProgress(facialReportResponseNew.data.overallWellnessScore.value.floatValue());
            }

            binding.txtAlertMessage.setText(facialReportResponseNew.data.summary);
            binding.tvLastReportDate.setText(DateTimeUtils.convertAPIDateMonthFormatWithTime(facialReportResponseNew.data.createdAt));

            List<HealthCamItem> healthCamGoodItems = facialReportResponseNew.data.healthCamReportByCategory.healthCamGood;
            List<HealthCamItem> healthCamPayAttentionItems = facialReportResponseNew.data.healthCamReportByCategory.healthCamPayAttention;

            // Combine the lists if you want to display them together
            allHealthCamItems.clear();
            allHealthCamItems.addAll(healthCamGoodItems);
            allHealthCamItems.addAll(healthCamPayAttentionItems);

            HealthCamVitalsAdapter adapter = new HealthCamVitalsAdapter(this, allHealthCamItems);

            binding.recyclerViewVitalCards.setLayoutManager(new GridLayoutManager(this, 2)); // 2 columns
            binding.recyclerViewVitalCards.setAdapter(adapter);

            setupScanTracker(scanBinding, facialReportResponseNew.data.usedCount, facialReportResponseNew.data.limit);
            setupBoosterTracker(facialReportResponseNew.data.boosterUsed, facialReportResponseNew.data.boosterLimit);
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
        if (!facialReportResponseNew.data.recommendation.isEmpty()) {
            HealthCamRecommendationAdapter adapter = new HealthCamRecommendationAdapter(this, facialReportResponseNew.data.recommendation);
            LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            binding.recyclerViewContinue.setLayoutManager(horizontalLayoutManager);
            binding.recyclerViewContinue.setAdapter(adapter);
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


    // new Booster ui
    private void setupBoosterTracker(int boosterUsed, int boosterLimit) {
        if (boosterLimit == 0) {
            binding.txtBoosterCount.setText("0");
        } else if (boosterLimit > 0 && boosterUsed < boosterLimit) {
            binding.btnBuyFacescan.setText("Scan Again");
            binding.txtBoosterCount.setText(String.valueOf(boosterLimit - boosterUsed));
        }
    }

    // new scan ui
    private void setupScanTracker(LayoutScanProgressBinding layout, int usedCount, int limit) {
        layout.scanIndicators.removeAllViews();

        LayoutInflater inflater = LayoutInflater.from(layout.getRoot().getContext());

        for (int i = 1; i <= limit; i++) {
            ItemScanCircleBinding itemBinding = ItemScanCircleBinding.inflate(inflater);

            itemBinding.circleNumber.setText(String.valueOf(i));

            if (i <= usedCount) {
                itemBinding.circleImage.setImageResource(R.drawable.ic_checklist_complete);
                itemBinding.circleNumber.setTextColor(ContextCompat.getColor(layout.getRoot().getContext(), android.R.color.black));
                itemBinding.txtGreenDot.setVisibility(View.VISIBLE);
            } else {
                itemBinding.circleImage.setImageResource(R.drawable.ic_checklist_tick_bg);
                itemBinding.circleNumber.setTextColor(ContextCompat.getColor(layout.getRoot().getContext(), android.R.color.black));
                itemBinding.txtGreenDot.setVisibility(View.INVISIBLE);
            }

            layout.scanIndicators.addView(itemBinding.getRoot());
        }
        if (usedCount == 1) {
            layout.infoText.setText("One scan a week is all you need to stay on top of your vitals.");
        } else {
            layout.infoText.setText("One step closer to better health! Your scans refresh monthly—stay on track!");
        }
        if (usedCount == limit) {
            layout.buttonText.setText("Scan Again @ 99");
        } else {
            layout.buttonText.setText("Scan Again");
        }
        layout.btnScanAgain.setOnClickListener(v -> startActivity(new Intent(NewHealthCamReportActivity.this, HealthCamBasicDetailsActivity.class)));
    }

    private void DownLaodReport(String pdf) {
        String pdfUrl = pdf;//"https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf";

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(pdfUrl));
        request.setTitle("Downloading PDF");
        request.setDescription("Downloading file, please wait...");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "sample.pdf");

// Enqueue download
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);
        Toast.makeText(this, "Download Started...", Toast.LENGTH_SHORT).show();
    }

}
