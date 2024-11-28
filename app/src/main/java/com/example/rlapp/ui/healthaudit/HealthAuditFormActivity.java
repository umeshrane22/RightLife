package com.example.rlapp.ui.healthaudit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.rlapp.R;
import com.example.rlapp.RetrofitData.ApiClient;
import com.example.rlapp.RetrofitData.ApiService;
import com.example.rlapp.apimodel.UserAuditAnswer.Answer;
import com.example.rlapp.apimodel.UserAuditAnswer.Option;
import com.example.rlapp.apimodel.UserAuditAnswer.UserAnswerRequest;
import com.example.rlapp.ui.healthaudit.questionlist.QuestionListHealthAudit;
import com.example.rlapp.ui.payment.AccessPaymentActivity;
import com.example.rlapp.ui.utility.SharedPreferenceConstants;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HealthAuditFormActivity extends AppCompatActivity implements OnNextFragmentClickListener {
    ImageView ic_back_dialog, close_dialog;
    private ViewPager2 viewPager;
    private Button prevButton, nextButton, submitButton;
    private FormPagerAdapter adapter;
    private ArrayList<Answer> formData = new ArrayList<>();
    private ProgressBar progressBar;
    public static final String ARG_QUESTION = "QUESTION";
    QuestionListHealthAudit ResponseObj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthaudit_form);

        ic_back_dialog = findViewById(R.id.ic_back_dialog);
        close_dialog = findViewById(R.id.ic_close_dialog);

        viewPager = findViewById(R.id.viewPager);
        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);
        submitButton = findViewById(R.id.submitButton);

        progressBar = findViewById(R.id.progressBar);

        //call to
        getQuestionerList("");

        //viewPager.setUserInputEnabled(false);

        adapter = new FormPagerAdapter(this);
        viewPager.setAdapter(adapter);

        prevButton.setOnClickListener(v -> navigateToPreviousPage());
        nextButton.setOnClickListener(v -> navigateToNextPage());
        submitButton.setOnClickListener(v -> submitFormData());

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateButtonVisibility(position);
                updateProgress(position);
            }
        });


        ic_back_dialog.setOnClickListener(view -> {
            int currentItem = viewPager.getCurrentItem();
            int totalItems = adapter.getItemCount();

            // Go to the next page if it's not the last one
           /* if (currentItem < totalItems - 1) {
                viewPager.setCurrentItem(currentItem + 1);
            } else {
                // If it's the last page, go back to the first page
                viewPager.setCurrentItem(0);
            }*/

            if (currentItem == 0) {
                finish();
            }
            // If on any other page, move to the previous page
            else {
                viewPager.setCurrentItem(currentItem - 1);
            }
        });

        close_dialog.setOnClickListener(view -> {
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

    private void submitFormData() {
        callAnswerRequest();
    }


    private void updateButtonVisibility(int position) {
        submitButton.setVisibility(position == adapter.getItemCount() - 1 ? View.VISIBLE : View.GONE);
    }

    private void updateProgress(int fragmentIndex) {
        // Set progress percentage based on the current fragment (out of 8)
        int progressPercentage = (int) (((fragmentIndex + 1) / (double) adapter.getItemCount()) * 100);
        progressBar.setProgress(progressPercentage);
    }


    void submitAnswerRequest(UserAnswerRequest requestAnswer) {
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString(SharedPreferenceConstants.ACCESS_TOKEN, null);

        Log.d("Access Token", "Token: " + accessToken);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        // Create a request body (replace with actual email and phone number)
        //SubmitLoginOtpRequest request = new SubmitLoginOtpRequest("+91"+mobileNumber,OTP,"ABC123","Asus ROG 6","hp","ABC123");

        // Make the API call
        Call<JsonElement> call = apiService.postAnswerRequest(accessToken, "HEALTH_REPORT", requestAnswer);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful() && response.body() != null) {
                    //LoginResponseMobile loginResponse = response.body();
                    JsonElement affirmationsResponse = response.body();
                    Log.d("API Response", "Success: " + response.body().toString());
                    Log.d("API Response 2", "Success: " + response.body().toString());

                    Gson gson = new Gson();
                    String jsonResponse = gson.toJson(response.body().toString());
                    Log.d("API Response body", "Success: " + jsonResponse);

                    Intent intent = new Intent(HealthAuditFormActivity.this, AccessPaymentActivity.class);
                    startActivity(intent);

                } else {
                    try {
                        if (response.errorBody() != null) {
                            String errorMessage = response.errorBody().string();
                            System.out.println("Request failed with error: " + errorMessage);
                            Log.d("API Response 2", "Success: " + errorMessage);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(HealthAuditFormActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(HealthAuditFormActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    void callAnswerRequest() {

        List<Answer> answers = new ArrayList<>(formData);
        // Make sure to replace the empty string with actual data if needed
        UserAnswerRequest request = new UserAnswerRequest("", ResponseObj.getQuestionData().getId(), answers);


        // Use Retrofit to send the ApiRequest (implementation shown below)
        Log.d("API REQuest Answer", "API REQuest - : " + request.toString());
        Gson gson = new Gson();

        // Convert ApiRequest object to JSON string
        String jsonString = gson.toJson(request);

        // Print the JSON string
        System.out.println(jsonString);
        Log.d("API REQuest", "API REQuest - : " + jsonString);
        submitAnswerRequest(request);
    }


    //"THINK_RIGHT", "CATEGORY", "ygjh----g"
    private void getQuestionerList(String s) {
        //-----------
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString(SharedPreferenceConstants.ACCESS_TOKEN, null);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        // Create a request body (replace with actual email and phone number)
        // SignupOtpRequest request = new SignupOtpRequest("+91"+mobileNumber);

        // Make the API call
        Call<JsonElement> call = apiService.getsubmoduletest(accessToken, "CHECK_IN");
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonElement affirmationsResponse = response.body();
                    Log.d("API Response", "SUB subModule list - : " + affirmationsResponse.toString());
                    Gson gson = new Gson();
                    String jsonResponse = gson.toJson(response.body());

                    ResponseObj = gson.fromJson(jsonResponse, QuestionListHealthAudit.class);
                    adapter.setData(ResponseObj.getQuestionData());
                    adapter.notifyDataSetChanged();
                    Log.d("API Response body", "Success:Questionllist " + ResponseObj.getQuestionData().getQuestionList().size());
                } else {
                    Toast.makeText(HealthAuditFormActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(HealthAuditFormActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API ERROR", "onFailure: " + t.getMessage());
                t.printStackTrace();  // Print the full stack trace for more details

            }
        });

    }

    @Override
    public void getDataFromFragment(String questionNumber, ArrayList<String> answer) {
        List<Option> options = new ArrayList<>();
        for (String ans : answer) {
            Option option = new Option(ans);
            options.add(option);
        }
        Answer answerToSubmit = new Answer(questionNumber, options);
        formData.add(answerToSubmit);
    }
}

