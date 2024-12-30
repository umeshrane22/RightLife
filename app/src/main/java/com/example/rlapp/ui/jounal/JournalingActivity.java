package com.example.rlapp.ui.jounal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rlapp.MainActivity;
import com.example.rlapp.R;
import com.example.rlapp.RetrofitData.ApiClient;
import com.example.rlapp.RetrofitData.ApiService;
import com.example.rlapp.apimodel.LoginResponseMobile;
import com.example.rlapp.apimodel.emaillogin.EmailLoginRequest;
import com.example.rlapp.ui.CategoryListActivity;
import com.example.rlapp.ui.moduledetail.ModuleContentDetailViewActivity;
import com.example.rlapp.ui.search.SearchCategoryAdapter;
import com.example.rlapp.ui.search.SearchQueryAdapter;
import com.example.rlapp.ui.search.SearchQueryResponse;
import com.example.rlapp.ui.search.SearchQueryResults;
import com.example.rlapp.ui.search.SearchResponse;
import com.example.rlapp.ui.search.SearchResult;
import com.example.rlapp.ui.utility.SharedPreferenceConstants;
import com.example.rlapp.ui.utility.SharedPreferenceManager;
import com.example.rlapp.ui.utility.Utils;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JournalingActivity extends AppCompatActivity {

    private LinearLayout ll_normal_journal, ll_guided_journal,
            ll_journal_selection_guided, ll_journal_selection_normal, ll_journal_selection;
    private RelativeLayout rlMoveRight;

    private TextInputEditText etTitle, et_title_normal_journal, et_your_journal_normal, etFeeling, etSituation, etMood;
    private Button btnSave, btnSaveNormal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);

        findViewById(R.id.ic_back_dialog).setOnClickListener(view -> {
            if (ll_journal_selection.getVisibility() == View.VISIBLE){
            finish();
        }else {
                ll_journal_selection.setVisibility(View.VISIBLE);
                ll_guided_journal.setVisibility(View.GONE);
                ll_normal_journal.setVisibility(View.GONE);
            }
        });

        // Initialize views
        initializeViews();

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
        ll_journal_selection_guided.setOnClickListener(new View.OnClickListener() {
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
        });

    }


    /**
     * Initialize the input fields and button.
     */
    private void initializeViews() {
        ll_guided_journal = findViewById(R.id.ll_guided_journal);
        ll_normal_journal = findViewById(R.id.ll_normal_journal);
        ll_journal_selection_guided = findViewById(R.id.ll_journal_selection_guided);
        ll_journal_selection_normal = findViewById(R.id.ll_journal_selection_normal);
        ll_journal_selection = findViewById(R.id.ll_journal_selection);


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
                        Toast.makeText(JournalingActivity.this, "Success: " + jsonResponse, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(JournalingActivity.this, "Failed: " + jsonResponse, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(JournalingActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(JournalingActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
        Toast.makeText(JournalingActivity.this, message, Toast.LENGTH_SHORT).show();
    }

}
