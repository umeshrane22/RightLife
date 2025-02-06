package com.example.rlapp.ui.payment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.rlapp.R;
import com.example.rlapp.RetrofitData.ApiClient;
import com.example.rlapp.RetrofitData.ApiService;
import com.example.rlapp.apimodel.userdata.UserProfileResponse;
import com.example.rlapp.ui.HomeActivity;
import com.example.rlapp.ui.rlpagemain.RLPageActivity;
import com.example.rlapp.ui.utility.SharedPreferenceConstants;
import com.example.rlapp.ui.utility.SharedPreferenceManager;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccessPaymentActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout llHealthAudit, llHealthCam, llVoiceScan, llmindaudit;
    private ImageView rlmenu,img_homemenu,img_healthmenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_accesspayment);
       /* llHealthAudit = findViewById(R.id.ll_health_audit);
        llHealthCam = findViewById(R.id.ll_health_cam);
        llVoiceScan = findViewById(R.id.ll_voice_scan);
        llmindaudit = findViewById(R.id.ll_mind_audit);


        llHealthAudit.setOnClickListener(this);
        llHealthCam.setOnClickListener(this);
        llVoiceScan.setOnClickListener(this);
        llmindaudit.setOnClickListener(this);*/






        /*llHealthAudit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HealthPageMainActivity.this, "Health Audit card click", Toast.LENGTH_SHORT).show();
            }
        });*/
        findViewById(R.id.ic_back_dialog).setOnClickListener(view -> finish());

        getPaymentCardList("");
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        if (view.getId() == R.id.ll_health_audit) {

            Toast.makeText(AccessPaymentActivity.this, "Health Audit card click", Toast.LENGTH_SHORT).show();
        } else if (view.getId() == R.id.ll_health_cam) {
            Toast.makeText(AccessPaymentActivity.this, "Health Cam card click", Toast.LENGTH_SHORT).show();

        } else if (view.getId() == R.id.ll_voice_scan) {
            Toast.makeText(AccessPaymentActivity.this, "Voice Scan card click", Toast.LENGTH_SHORT).show();

        } else if (view.getId() == R.id.ll_mind_audit) {
            Toast.makeText(AccessPaymentActivity.this, "Mind Audit card click", Toast.LENGTH_SHORT).show();
        }

    }


    // get user details
    private void getPaymentCardList(String s) {
        //-----------
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString(SharedPreferenceConstants.ACCESS_TOKEN, null);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        // Create a request body (replace with actual email and phone number)
        // SignupOtpRequest request = new SignupOtpRequest("+91"+mobileNumber);

        // Make the API call
        Call<ResponseBody> call = apiService.getPaymentPlan(accessToken,"FACIAL_SCAN");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String paymentPlanResponse = null;
                    try {
                        paymentPlanResponse = response.body().string();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Log.d("API Response", "PaymentPlan: " + paymentPlanResponse);
                    Gson gson = new Gson();
                    String jsonResponse = gson.toJson(response.body());


                } else {
                    //  Toast.makeText(HomeActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(AccessPaymentActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API ERROR", "onFailure: " + t.getMessage());
                t.printStackTrace();  // Print the full stack trace for more details

            }
        });

    }


}