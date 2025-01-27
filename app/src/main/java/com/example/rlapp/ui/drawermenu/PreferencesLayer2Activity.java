package com.example.rlapp.ui.drawermenu;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.rlapp.R;
import com.example.rlapp.RetrofitData.ApiClient;
import com.example.rlapp.RetrofitData.ApiService;
import com.example.rlapp.ui.utility.SharedPreferenceConstants;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreferencesLayer2Activity extends AppCompatActivity {
    public LinearLayout layer2;
    private ViewPager2 viewPager;
    public ProgressBar progressBar;
    private Button submitButton;
    private ImageView btnBack;
    private PreferencePagerAdapter adapter;
    private int pagePosition = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences_layer2);

        btnBack = findViewById(R.id.ic_back_dialog);

        viewPager = findViewById(R.id.viewPager);
        progressBar = findViewById(R.id.progressBar);
        submitButton = findViewById(R.id.submitButton);
        layer2 = findViewById(R.id.ll_layer2);

        getPreferences();
        adapter = new PreferencePagerAdapter(this);

        viewPager.setAdapter(adapter);

        btnBack.setOnClickListener(view -> {
            int currentItem = viewPager.getCurrentItem();
            int totalItems = adapter.getItemCount();

            if (currentItem == 0) {
                finish();
            } else {
                viewPager.setCurrentItem(currentItem - 1);
            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateButtonVisibility(position);
                updateProgress(position);
                pagePosition = position;
            }
        });
    }

    private void navigateToPreviousPage() {
        if (viewPager.getCurrentItem() > 0) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    public void navigateToNextPage() {
        if (viewPager.getCurrentItem() < adapter.getItemCount() - 1) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        }
    }

    private void updateButtonVisibility(int position) {
        //submitButton.setVisibility(position == adapter.getItemCount() - 1 ? View.VISIBLE : View.GONE);
    }

    private void updateProgress(int fragmentIndex) {
        // Set progress percentage based on the current fragment (out of 8)
        int progressPercentage = (int) (((fragmentIndex + 1) / (double) adapter.getItemCount()) * 100);
        progressBar.setProgress(progressPercentage);
    }

    private void getPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString(SharedPreferenceConstants.ACCESS_TOKEN, null);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<JsonElement> call = apiService.getPreferences(accessToken);

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Gson gson = new Gson();
                    String jsonResponse = gson.toJson(response.body());
                    PreferenceResponse preferenceResponse = gson.fromJson(jsonResponse, PreferenceResponse.class);
                    adapter.setPreferenceData(preferenceResponse.getData());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(PreferencesLayer2Activity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(PreferencesLayer2Activity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updatePreferenceAnswers(PreferenceAnswer preferenceAnswer){
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString(SharedPreferenceConstants.ACCESS_TOKEN, null);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<ResponseBody> call = apiService.updatePreference(accessToken, preferenceAnswer);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Gson gson = new Gson();
                    try {
                        //String jsonResponse = gson.toJson(response.body().string());
                        String jsonResponse = response.body().string();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    if (pagePosition == adapter.getItemCount() - 1){
                        finish();
                    }else {
                        navigateToNextPage();
                    }
                }else {
                    Toast.makeText(PreferencesLayer2Activity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(PreferencesLayer2Activity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
