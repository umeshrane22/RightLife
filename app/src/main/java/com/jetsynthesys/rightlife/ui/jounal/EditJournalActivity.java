package com.jetsynthesys.rightlife.ui.jounal;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.jetsynthesys.rightlife.R;
import com.jetsynthesys.rightlife.BaseActivity;
import com.jetsynthesys.rightlife.apimodel.rlpagemodels.journal.Journals;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditJournalActivity extends BaseActivity {

    private Journals journals;
    private LinearLayout ll_guided_journal, ll_normal_journal;
    private Button btnSave, btnSaveNormal;
    private TextInputEditText etTitle, et_title_normal_journal, et_your_journal_normal, etFeeling, etSituation, etMood;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildContentView(R.layout.activity_edit_journal);

        findViewById(R.id.ic_back_dialog).setOnClickListener(view -> finish());

        ll_guided_journal = findViewById(R.id.ll_guided_journal);
        ll_normal_journal = findViewById(R.id.ll_normal_journal);

        journals = (Journals) getIntent().getSerializableExtra("Journal");

        etTitle = findViewById(R.id.et_title_guided_journal);
        et_title_normal_journal = findViewById(R.id.et_title_normal_journal);
        et_your_journal_normal = findViewById(R.id.et_your_journal_normal);
        etFeeling = findViewById(R.id.et_feeling_guided_journal);
        etSituation = findViewById(R.id.et_situation_guided_journal);
        etMood = findViewById(R.id.et_mood_guided_journal);
        btnSave = findViewById(R.id.btn_save_guided_journal);
        btnSaveNormal = findViewById(R.id.btn_save_normal_journal);

        if (journals != null && journals.getType().equalsIgnoreCase("SELF")) {
            ll_guided_journal.setVisibility(View.GONE);
            ll_normal_journal.setVisibility(View.VISIBLE);
        } else {
            ll_guided_journal.setVisibility(View.VISIBLE);
            ll_normal_journal.setVisibility(View.GONE);
        }

        etTitle.setText(journals.getTitle());
        et_title_normal_journal.setText(journals.getJournal());
        et_your_journal_normal.setText(journals.getJournal());
        etFeeling.setText(journals.getJournal());
        etMood.setText(journals.getChangedMood());
        etSituation.setText(journals.getParticularSitutation());


        btnSave.setOnClickListener(v -> handleSaveButtonClick());
        btnSaveNormal.setOnClickListener(v -> handleSaveButtonClickNormal());

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


    private void sendDataToApi(String title, String type, String journal, String situation, String mood) {

        // Create a request body (replace with actual email and phone number)

        Map<String, String> requestData = new HashMap<>();
        requestData.put("title", title);
        requestData.put("type", type);
        requestData.put("journal", journal);
        requestData.put("particularSitutation", situation);
        requestData.put("changedMood", mood);


        // Make the API call
        Call<ResponseBody> call = apiService.updateJournal(sharedPreferenceManager.getAccessToken(), journals.getId(), requestData);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Gson gson = new Gson();
                    String jsonResponse = gson.toJson(response.body());
                    Log.d("API Response body", "Success: " + jsonResponse);
                    if (response.isSuccessful()) {
                        Toast.makeText(EditJournalActivity.this, "Success: " + jsonResponse, Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(EditJournalActivity.this, "Failed: " + jsonResponse, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(EditJournalActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                handleNoInternetView(t);
            }
        });

    }

    /**
     * Display a toast message.
     *
     * @param message The message to display.
     */
    private void showToast(String message) {
        Toast.makeText(EditJournalActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
