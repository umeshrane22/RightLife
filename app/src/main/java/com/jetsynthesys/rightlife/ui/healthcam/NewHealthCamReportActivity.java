package com.jetsynthesys.rightlife.ui.healthcam;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
        if (userdata != null) {
            binding.txtuserName.setText("Hi " + userdata.getFirstName());
        }
        updateChecklistStatus();
    }

    private void updateChecklistStatus(){
        CommonAPICall.INSTANCE.updateChecklistStatus(this, "vital_facial_scan",AppConstants.CHECKLIST_COMPLETED);
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
            if (facialReportResponseNew.data.overallWellnessScore!=null) {
                binding.txtWellnessScore1.setText(String.format("%.2f", facialReportResponseNew.data.overallWellnessScore.value));

                binding.halfCurveProgressBar.setProgress(facialReportResponseNew.data.overallWellnessScore.value.floatValue());
            }


            binding.txtAlertMessage.setText(facialReportResponseNew.data.summary);
            binding.tvLastReportDate.setText(DateTimeUtils.convertAPIDateMonthFormatWithTime(facialReportResponseNew.data.createdAt));
            if (facialReportResponseNew.data.lastCheckin) {
                binding.cardviewLastCheckin.setVisibility(View.VISIBLE);
            } else {
                binding.cardviewLastCheckin.setVisibility(View.VISIBLE);
            }

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
}
