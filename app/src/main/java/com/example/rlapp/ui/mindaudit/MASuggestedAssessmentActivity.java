package com.example.rlapp.ui.mindaudit;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rlapp.R;
import com.example.rlapp.RetrofitData.ApiClient;
import com.example.rlapp.RetrofitData.ApiService;
import com.example.rlapp.ui.mindaudit.curated.AssessmentUndertaken;
import com.example.rlapp.ui.mindaudit.curated.Context;
import com.example.rlapp.ui.mindaudit.curated.CuratedUserData;
import com.example.rlapp.ui.utility.SharedPreferenceConstants;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MASuggestedAssessmentActivity extends AppCompatActivity {
    private ImageView ic_back_dialog, close_dialog;
    private RecyclerView rvSuggestedAssessment, rvAllAssessment, rvCurated;

    private SuggestedAssessmentAdapter suggestedAssessmentAdapter;
    private AllAssessmentAdapter allAssessmentAdapter;
    private Assessments assessments;
    private ArrayList<String> suggestedAssessmentString = new ArrayList<>();
    private ArrayList<String> allAssessments = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ma_suggested_assessment);

        ic_back_dialog = findViewById(R.id.ic_back_dialog);
        close_dialog = findViewById(R.id.ic_close_dialog);

        ic_back_dialog.setOnClickListener(view -> {
            finish();
        });
        close_dialog.setOnClickListener(view -> {
            finish();
        });

        getCurated();

        rvSuggestedAssessment = findViewById(R.id.rv_suggested_assessment);
        rvAllAssessment = findViewById(R.id.rv_all_assessment);
        rvCurated = findViewById(R.id.rv_curated);

        assessments = (Assessments) getIntent().getSerializableExtra("AssessmentData");

        SuggestedAssessments suggestedAssessments = assessments.getSuggestedAssessments();

        rvSuggestedAssessment.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvAllAssessment.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvCurated.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        suggestedAssessmentAdapter = new SuggestedAssessmentAdapter(this, suggestedAssessmentString, this::showDisclaimerDialog);
        rvSuggestedAssessment.setAdapter(suggestedAssessmentAdapter);
        rvSuggestedAssessment.scrollToPosition(0);

        AllAssessment assessment = assessments.getAllAssessment();

        if (assessment.getDass21() != null) {
            allAssessments.add(assessment.getDass21());
        } else {
            suggestedAssessmentString.add("DASS-21");
        }

        if (assessment.getSleepAudit() != null) {
            allAssessments.add(assessment.getSleepAudit());
        } else {
            suggestedAssessmentString.add("Sleep Audit");
        }

        if (assessment.getGad7() != null) {
            allAssessments.add(assessment.getGad7());
        } else {
            suggestedAssessmentString.add("GAD-7");
        }
        if (assessment.getOhq() != null) {
            allAssessments.add(assessment.getOhq());
        } else {
            suggestedAssessmentString.add("OHQ");
        }

        if (assessment.getCas() != null) {
            allAssessments.add(assessment.getCas());
        } else {
            suggestedAssessmentString.add("CAS");
        }

        if (assessment.getPhq9() != null) {
            allAssessments.add(assessment.getPhq9());
        } else {
            suggestedAssessmentString.add("PHQ-9");
        }


        allAssessmentAdapter = new AllAssessmentAdapter(this, allAssessments, this::showDisclaimerDialog);
        rvAllAssessment.setAdapter(allAssessmentAdapter);
        rvAllAssessment.scrollToPosition(0);

    }

    private void showDisclaimerDialog(String header) {
        // Create the dialog
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_mind_audit_disclaimer);
        dialog.setCancelable(true);
        Window window = dialog.getWindow();
        // Set the dim amount
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.dimAmount = 0.7f; // Adjust the dim amount (0.0 - 1.0)
        window.setAttributes(layoutParams);

        Button btnTakeAssessment = dialog.findViewById(R.id.btn_take_assessment);
        TextView tvItem1 = dialog.findViewById(R.id.item_text1);
        TextView tvItem2 = dialog.findViewById(R.id.item_text2);
        TextView tvItem3 = dialog.findViewById(R.id.item_text3);
        TextView tvHeader = dialog.findViewById(R.id.tv_selected_assessment);
        tvHeader.setText(header);

        ImageView imgClose = dialog.findViewById(R.id.ic_close_dialog);
        imgClose.setOnClickListener(view -> {
            dialog.dismiss();
        });

        btnTakeAssessment.setOnClickListener(view -> {
            Intent intent = new Intent(MASuggestedAssessmentActivity.this, MAAssessmentQuestionaireActivity.class);
            intent.putExtra("AssessmentType", header);
            startActivity(intent);
        });
        // Show the dialog
        dialog.show();
    }

    private void getCurated() {
        CuratedUserData curatedUserData = new CuratedUserData();
        Context context = new Context();
        context.setModule("EAT_RIGHT");
        AssessmentUndertaken assessmentUndertaken = new AssessmentUndertaken();
        assessmentUndertaken.setAssessment("MIND_AUDIT");
        assessmentUndertaken.setTarget(new ArrayList<>());
        context.setAssessmentUndertaken(assessmentUndertaken);
        curatedUserData.setContext(context);

        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, android.content.Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString(SharedPreferenceConstants.ACCESS_TOKEN, null);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ResponseBody> call = apiService.getCuratedAssessment(accessToken, curatedUserData);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(MASuggestedAssessmentActivity.this, "Success: " + response.code(), Toast.LENGTH_SHORT).show();
                    try {
                        String jsonString = response.body().string();
                        Gson gson = new Gson();
                        /*****
                         * Sudhir will complete this part
                         */
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Toast.makeText(MASuggestedAssessmentActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MASuggestedAssessmentActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
