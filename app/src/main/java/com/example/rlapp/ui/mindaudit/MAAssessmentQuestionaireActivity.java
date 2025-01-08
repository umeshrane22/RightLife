package com.example.rlapp.ui.mindaudit;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.rlapp.R;
import com.example.rlapp.RetrofitData.ApiClient;
import com.example.rlapp.RetrofitData.ApiService;
import com.example.rlapp.ui.mindaudit.questions.MindAuditAssessmentQuestions;
import com.example.rlapp.ui.utility.SharedPreferenceConstants;
import com.example.rlapp.ui.utility.SharedPreferenceManager;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MAAssessmentQuestionaireActivity extends AppCompatActivity {
    public Button submitButton, nextButton;
    private TextView tvHeader;
    private ImageView imgBack;
    private ViewPager2 viewPager;
    private Button prevButton;
    private MAAssessmentPagerAdapter adapter;
    private ProgressBar progressBar;
    private MindAuditAssessmentQuestions mindAuditAssessmentQuestions;
    private String header;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maassessment_questions);

        tvHeader = findViewById(R.id.tv_header);
        imgBack = findViewById(R.id.ic_back_dialog);
        viewPager = findViewById(R.id.viewPager);
        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);
        submitButton = findViewById(R.id.submitButton);
        progressBar = findViewById(R.id.progressBar);

        header = getIntent().getStringExtra("AssessmentType");

        tvHeader.setText(header);

        getQuestionList(header);

        prevButton.setOnClickListener(v -> navigateToPreviousPage());
        nextButton.setOnClickListener(v -> navigateToNextPage());
        submitButton.setOnClickListener(v -> submitFormData(header));

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateButtonVisibility(position);
                updateProgress(position);
            }
        });
        viewPager.setUserInputEnabled(false);

        imgBack.setOnClickListener(view -> finish());

        adapter = new MAAssessmentPagerAdapter(this);
        viewPager.setAdapter(adapter);


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

    private void submitFormData(String assessment) {
        MindAuditAssessmentSaveRequest mindAuditAssessmentSaveRequest = new MindAuditAssessmentSaveRequest();
        AssessmentsTaken assessmentsTaken = new AssessmentsTaken();
        assessmentsTaken.setAssessment(assessment);
        Interpretation interpretation = new Interpretation();
        Anger anger = new Anger();
        anger.setLevel("Extremely Severe");
        anger.setScore(0);
        interpretation.setAnger(anger);
        assessmentsTaken.setInterpretation(interpretation);
        List<AssessmentsTaken> assessmentTakens = new ArrayList<>();
        assessmentTakens.add(assessmentsTaken);
        mindAuditAssessmentSaveRequest.setAssessmentsTaken(assessmentTakens);
        saveAssessment(mindAuditAssessmentSaveRequest);
    }

    private void updateButtonVisibility(int position) {
        submitButton.setVisibility(position == adapter.getItemCount() - 1 ? View.VISIBLE : View.GONE);
    }

    private void updateProgress(int fragmentIndex) {
        // Set progress percentage based on the current fragment (out of 8)
        int progressPercentage = (int) (((fragmentIndex + 1) / (double) adapter.getItemCount()) * 100);
        progressBar.setProgress(progressPercentage);
    }

    private void getQuestionList(String type) {
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString(SharedPreferenceConstants.ACCESS_TOKEN, null);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ResponseBody> call = apiService.getAssessmentByType(accessToken, type);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String jsonString = response.body().string();
                        Gson gson = new Gson();
                        mindAuditAssessmentQuestions = gson.fromJson(jsonString, MindAuditAssessmentQuestions.class);
                        adapter.setGroup(mindAuditAssessmentQuestions.getAssessmentQuestionnaires().getGroups().get(0));
                        adapter.notifyDataSetChanged();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Toast.makeText(MAAssessmentQuestionaireActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MAAssessmentQuestionaireActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAssessmentResult(String assessment) {
        String accessToken = SharedPreferenceManager.getInstance(this).getAccessToken();
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<ResponseBody> call = apiService.getMindAuditAssessmentResult(accessToken, assessment);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Gson gson = new Gson();
                    String jsonResponse = null;
                    try {
                        jsonResponse = gson.toJson(response.body().string());
                        Log.d("AAAA", jsonResponse);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    try {
                        if (response.errorBody() != null) {
                            String errorMessage = response.errorBody().string();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(MAAssessmentQuestionaireActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MAAssessmentQuestionaireActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveAssessment(MindAuditAssessmentSaveRequest mindAuditAssessmentSaveRequest) {
        String assessToken = SharedPreferenceManager.getInstance(this).getAccessToken();
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ResponseBody> call = apiService.saveMindAuditAssessment(assessToken, mindAuditAssessmentSaveRequest);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String jsonString = response.body().string();
                        Gson gson = new Gson();
                        Log.d("AAAAA", "Result  = " + jsonString);

                        getAssessmentResult(header);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Toast.makeText(MAAssessmentQuestionaireActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MAAssessmentQuestionaireActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
