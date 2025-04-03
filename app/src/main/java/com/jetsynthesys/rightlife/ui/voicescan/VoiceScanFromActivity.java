package com.jetsynthesys.rightlife.ui.voicescan;

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

import com.jetsynthesys.rightlife.R;
import com.jetsynthesys.rightlife.RetrofitData.ApiClient;
import com.jetsynthesys.rightlife.RetrofitData.ApiService;
import com.jetsynthesys.rightlife.apimodel.UserAuditAnswer.Answer;
import com.jetsynthesys.rightlife.apimodel.UserAuditAnswer.Option;
import com.jetsynthesys.rightlife.apimodel.UserAuditAnswer.UserAnswerRequest;
import com.jetsynthesys.rightlife.ui.healthaudit.questionlist.Question;
import com.jetsynthesys.rightlife.ui.healthaudit.questionlist.QuestionData;
import com.jetsynthesys.rightlife.ui.healthaudit.questionlist.QuestionListHealthAudit;
import com.jetsynthesys.rightlife.ui.payment.AccessPaymentActivity;
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceConstants;
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VoiceScanFromActivity extends AppCompatActivity implements OnNextVoiceScanFragmentClickListener {

    public Button prevButton, nextButton, submitButton;
    ImageView ic_back_dialog, close_dialog;
    private ViewPager2 viewPager;
    private VoiceScanFormPagerAdapter adapter;
    private ProgressBar progressBar;
    private QuestionListHealthAudit responseObj;
    private ArrayList<Answer> answerVoiceScanData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_voice_scan_from);


        ic_back_dialog = findViewById(R.id.ic_back_dialog);
        close_dialog = findViewById(R.id.ic_close_dialog);
        viewPager = findViewById(R.id.viewPager);
        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);
        submitButton = findViewById(R.id.submitButton);

        progressBar = findViewById(R.id.progressBar);

        getQuestionerList();

        adapter = new VoiceScanFormPagerAdapter(this);
        viewPager.setAdapter(adapter);

        prevButton.setOnClickListener(v -> navigateToPreviousPage());
        nextButton.setOnClickListener(v -> navigateToNextPage("Sad"));
        submitButton.setOnClickListener(v -> callAnswerRequest());
        /*submitButton.setOnClickListener(view -> {
            Intent intent = new Intent(VoiceScanFromActivity.this, AccessPaymentActivity.class);
            startActivity(intent);
        });*/

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
            // showExitDialog();
        });

    }

    private void updateProgress(int fragmentIndex) {
        // Set progress percentage based on the current fragment (out of 8)
        int progressPercentage = (int) (((fragmentIndex + 1) / (double) adapter.getItemCount()) * 100);
        progressBar.setProgress(progressPercentage);
    }

    private void updateButtonVisibility(int position) {
        int totalItems = adapter.getItemCount();
        nextButton.setVisibility(View.VISIBLE);
        if (position == totalItems - 1) {
            nextButton.setText("Start Recording");
        } else if (position == 0) {
            nextButton.setVisibility(View.GONE);
        } else {
            nextButton.setText("Next");
        }
    }

    void callAnswerRequest() {

        List<Answer> answers = new ArrayList<>(answerVoiceScanData);
        // Make sure to replace the empty string with actual data if needed
        UserAnswerRequest request = new UserAnswerRequest("", responseObj.getQuestionData().getId(), answers);

        submitAnswerRequest(request);
    }

    void submitAnswerRequest(UserAnswerRequest requestAnswer) {
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString(SharedPreferenceConstants.ACCESS_TOKEN, null);

        Log.d("Access Token", "Token: " + accessToken);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        // Create a request body (replace with actual email and phone number)
        //SubmitLoginOtpRequest request = new SubmitLoginOtpRequest("+91"+mobileNumber,OTP,"ABC123","Asus ROG 6","hp","ABC123");

        // Make the API call
        Call<JsonElement> call = apiService.postAnswerRequest(accessToken, "CHECK_IN", requestAnswer);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful() && response.body() != null) {
                    //LoginResponseMobile loginResponse = response.body();
                    JsonElement affirmationsResponse = response.body();
                    Log.d("API Response", "Success: " + response.body().toString());
                    Log.d("API Response 2", "Success: " + response.body().toString());

                    Gson gson = new Gson();
                    String jsonResponse = gson.toJson(response.body());
                    Log.d("API Response body", "Success: " + jsonResponse);

                    VoiceScanSubmitResponse voiceScanSubmitResponse = gson.fromJson(jsonResponse, VoiceScanSubmitResponse.class);
                    SharedPreferenceManager.getInstance(VoiceScanFromActivity.this).saveVoiceScanAnswerId(voiceScanSubmitResponse.getData().getAnswerId());

                    Intent intent;
                    if (voiceScanSubmitResponse.getData().getIsSubscribed()) {
                        intent = new Intent(VoiceScanFromActivity.this, VoiceScanWaitingActivity.class);
                        intent.putExtra("answerId", voiceScanSubmitResponse.getData().getAnswerId());
                        intent.putExtra("description", VoiceRecordFragment.getDifferentTopic());
                    } else {
                        intent = new Intent(VoiceScanFromActivity.this, AccessPaymentActivity.class);
                        intent.putExtra("ACCESS_VALUE", "CHECK_IN");
                    }
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

                    Toast.makeText(VoiceScanFromActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(VoiceScanFromActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void navigateToPreviousPage() {
        if (viewPager.getCurrentItem() > 0) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    public void navigateToNextPage(String feelings) {

        int currentItem = viewPager.getCurrentItem();
        int totalItems = adapter.getItemCount();
        if (currentItem < totalItems - 1) {
            adapter.setFeelings(feelings);
            viewPager.setCurrentItem(currentItem + 1);
        } else {
            /*Intent intent = new Intent(VoiceScanFromActivity.this, AccessPaymentActivity.class);
            startActivity(intent);*/
            callAnswerRequest();
        }
    }

    private void getQuestionerList() {
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString(SharedPreferenceConstants.ACCESS_TOKEN, null);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<JsonElement> call = apiService.getsubmoduletest(accessToken, "CHECK_IN");
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("AAAA API Response", "SUB subModule list - : " + response);
                    Gson gson = new Gson();
                    String jsonResponse = gson.toJson(response.body());
                    responseObj = gson.fromJson(jsonResponse, QuestionListHealthAudit.class);
                    adapter.setData(removeQuestionDOB(responseObj.getQuestionData()));
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(VoiceScanFromActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(VoiceScanFromActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API ERROR", "onFailure: " + t.getMessage());
                t.printStackTrace();  // Print the full stack trace for more details
            }
        });
    }

    private QuestionData removeQuestionDOB(QuestionData questionData) {
        ArrayList<Question> questionsList = new ArrayList<>();
        for (Question question : questionData.getQuestionList()) {
            if (question.getQuestion().equals("dob")) {
                questionsList.remove(question);
            } else {
                questionsList.add(question);
            }
        }
        questionData.setQuestionList(questionsList);
        return questionData;
    }

    @Override
    public void onNextFragmentClick(String questionType, ArrayList<String> answer) {
        List<Option> options = new ArrayList<>();
        for (String ans : answer) {
            Option option = new Option(ans);
            options.add(option);
        }
        Answer answerToSubmit = new Answer(questionType, options);
        answerVoiceScanData.add(answerToSubmit);
        Log.d("AAAA", "Answer data = " + answer.size());
    }
}