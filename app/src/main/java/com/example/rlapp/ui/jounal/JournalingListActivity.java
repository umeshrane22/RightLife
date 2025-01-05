package com.example.rlapp.ui.jounal;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rlapp.R;
import com.example.rlapp.RetrofitData.ApiClient;
import com.example.rlapp.RetrofitData.ApiService;
import com.example.rlapp.apimodel.exploremodules.affirmations.AffrimationRecyclerViewAdapter;
import com.example.rlapp.apimodel.rlpagemodels.journal.RLpageJournalResponse;
import com.example.rlapp.ui.rlpagemain.RLPageActivity;
import com.example.rlapp.ui.utility.SharedPreferenceConstants;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JournalingListActivity extends AppCompatActivity {

    private LinearLayout ll_normal_journal, ll_guided_journal,
            ll_journal_selection_guided, ll_journal_selection_normal, ll_journal_selection;
    private RelativeLayout rlMoveRight;
    private RecyclerView recyclerView;
    private TextInputEditText etTitle, et_title_normal_journal, et_your_journal_normal, etFeeling, etSituation, etMood;
    private Button btnSave, btnSaveNormal,btn_continue_journal;
    private RadioButton rd_guided,rd_normal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journaling_list);

        // Initialize views
        initializeViews();
        //get Journal list
        MyRLJournal();


        findViewById(R.id.ic_back_dialog).setOnClickListener(view -> {
            if (ll_journal_selection.getVisibility() == View.VISIBLE){
                finish();
            }else {
                ll_journal_selection.setVisibility(View.VISIBLE);
                ll_guided_journal.setVisibility(View.GONE);
                ll_normal_journal.setVisibility(View.GONE);
            }
        });


        // Set click listener on Save button
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSaveButtonClick();
            }
        });
        btnSaveNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSaveButtonClickNormal();
            }
        });
/*        btn_continue_journal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rd_guided.isChecked()){
                    ll_journal_selection.setVisibility(View.GONE);
                    ll_guided_journal.setVisibility(View.VISIBLE);
                }else if (rd_normal.isChecked()){
                    ll_journal_selection.setVisibility(View.GONE);
                    ll_normal_journal.setVisibility(View.VISIBLE);
                }else {
                    Toast.makeText(JournalingListActivity.this, "Please select a journal type", Toast.LENGTH_SHORT).show();
                }
            }
        });*/
     /*   ll_journal_selection_guided.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_journal_selection.setVisibility(View.GONE);
                ll_guided_journal.setVisibility(View.VISIBLE);
            }
        });
        ll_journal_selection_normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_journal_selection.setVisibility(View.GONE);
                ll_normal_journal.setVisibility(View.VISIBLE);
            }
        });*/

    }


    /**
     * Initialize the input fields and button.
     */
    private void initializeViews() {
        recyclerView  = findViewById(R.id.recyclerView);
        ll_guided_journal = findViewById(R.id.ll_guided_journal);
        ll_normal_journal = findViewById(R.id.ll_normal_journal);
        ll_journal_selection_guided = findViewById(R.id.ll_journal_selection_guided);
        ll_journal_selection_normal = findViewById(R.id.ll_journal_selection_normal);
        ll_journal_selection = findViewById(R.id.ll_journal_selection);
        btn_continue_journal = findViewById(R.id.btn_continue_journal);

        etTitle = findViewById(R.id.et_title_guided_journal);
        et_title_normal_journal = findViewById(R.id.et_title_normal_journal);
        et_your_journal_normal = findViewById(R.id.et_your_journal_normal);
        etFeeling = findViewById(R.id.et_feeling_guided_journal);
        etSituation = findViewById(R.id.et_situation_guided_journal);
        etMood = findViewById(R.id.et_mood_guided_journal);
        btnSave = findViewById(R.id.btn_save_guided_journal);
        btnSaveNormal = findViewById(R.id.btn_save_normal_journal);
    }





    /**
     * Handle the Save button click.
     * Validate inputs and send data to the API.
     */
    private void handleSaveButtonClick() {
        String title = etTitle.getText().toString().trim();
        String journal = etFeeling.getText().toString().trim();
        String situation = etSituation.getText().toString().trim();
        String mood = etMood.getText().toString().trim();

        // Validate input fields
        if (validateInputs(title, journal, situation, mood)) {
            // Send data to the API
            sendDataToApi(title, "GUIDED", journal, situation, mood);
        }
    }

    private void handleSaveButtonClickNormal() {
        String title = et_title_normal_journal.getText().toString().trim();
        String journal = et_your_journal_normal.getText().toString().trim();
        String situation = "";
        String mood = "";

        // Validate input fields
        if (validateNormalInputs(title, journal)) {
            // Send data to the API
            sendDataToApi(title, "SELF", journal, situation, mood);
        }
    }


    private void sendDataToApi(String title, String type, String journal, String situation, String mood) {
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString(SharedPreferenceConstants.ACCESS_TOKEN, null);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        // Create a request body (replace with actual email and phone number)

        Map<String, String> requestData = new HashMap<>();
        requestData.put("title", title);
        requestData.put("type", type);
        requestData.put("journal", journal);
        requestData.put("particularSitutation", situation);
        requestData.put("changedMood", mood);


        // Make the API call
        Call<ResponseBody> call = apiService.createJournal(accessToken, requestData);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ResponseBody loginResponse = response.body();
                    Gson gson = new Gson();
                    String jsonResponse = gson.toJson(response.body());
                    Log.d("API Response body", "Success: " + jsonResponse);
                    if (response.isSuccessful()) {
                        Toast.makeText(JournalingListActivity.this, "Success: " + jsonResponse, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(JournalingListActivity.this, "Failed: " + jsonResponse, Toast.LENGTH_SHORT).show();
                    }

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
                    Toast.makeText(JournalingListActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(JournalingListActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    /**
     * Validate the user inputs.
     *
     * @return true if all inputs are valid, false otherwise.
     */
    private boolean validateInputs(String title, String journal, String situation, String mood) {
        if (TextUtils.isEmpty(title)) {
            showToast("Title cannot be empty.");
            return false;
        }
        if (TextUtils.isEmpty(journal)) {
            showToast("Journal cannot be empty.");
            return false;
        }
        if (TextUtils.isEmpty(situation)) {
            showToast("Situation cannot be empty.");
            return false;
        }
        if (TextUtils.isEmpty(mood)) {
            showToast("Mood cannot be empty.");
            return false;
        }
        return true;
    }

    private boolean validateNormalInputs(String title, String journal) {
        if (TextUtils.isEmpty(title)) {
            showToast("Title cannot be empty.");
            return false;
        }
        if (TextUtils.isEmpty(journal)) {
            showToast("Journal cannot be empty.");
            return false;
        }

        return true;
    }

    /**
     * Display a toast message.
     *
     * @param message The message to display.
     */
    private void showToast(String message) {
        Toast.makeText(JournalingListActivity.this, message, Toast.LENGTH_SHORT).show();
    }


    private void MyRLJournal() {
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString(SharedPreferenceConstants.ACCESS_TOKEN, null);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<ResponseBody> call = apiService.getMyRLJournal(accessToken,0,10);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(JournalingListActivity.this, "Success: " + response.code(), Toast.LENGTH_SHORT).show();
                    try {
                        String jsonString = response.body().string();
                        Log.d("Response Body"," My RL journal - "+jsonString);
                        Gson gson = new Gson();
                        RLpageJournalResponse rLpageJournalResponse = gson.fromJson(jsonString, RLpageJournalResponse.class);
                        Log.d("Response Body"," My RL journal - "+jsonString);
                        HandleJournalUI(rLpageJournalResponse);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Toast.makeText(JournalingListActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(JournalingListActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void HandleJournalUI(RLpageJournalResponse rLpageJournalResponse) {
        JournalRecyclerViewAdapter adapter = new JournalRecyclerViewAdapter(this, rLpageJournalResponse.getData().getJournalsList());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        recyclerView.setAdapter(adapter);
    }

}
