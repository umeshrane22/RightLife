package com.jetsynthesys.rightlife.ui.mindaudit;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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

import com.jetsynthesys.rightlife.BaseActivity;
import com.jetsynthesys.rightlife.R;
import com.jetsynthesys.rightlife.RetrofitData.ApiClient;
import com.jetsynthesys.rightlife.RetrofitData.ApiService;
import com.jetsynthesys.rightlife.ui.mindaudit.curated.AssessmentUndertaken;
import com.jetsynthesys.rightlife.ui.mindaudit.curated.Context;
import com.jetsynthesys.rightlife.ui.mindaudit.curated.CuratedUserData;
import com.jetsynthesys.rightlife.ui.utility.AppConstants;
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceConstants;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MASuggestedAssessmentActivity extends BaseActivity {
    private ImageView ic_back_dialog, close_dialog;
    private RecyclerView rvSuggestedAssessment, rvAllAssessment, rvCurated;
    private TextView tv_curated;
    private SuggestedAssessmentAdapter suggestedAssessmentAdapter;
    private AllAssessmentAdapter allAssessmentAdapter;
    private Assessments assessments;
    private ArrayList<String> suggestedAssessmentString = new ArrayList<>();
    private ArrayList<String> allAssessments = new ArrayList<>();
    private String selectedAssessment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildContentView(R.layout.activity_ma_suggested_assessment);

        ic_back_dialog = findViewById(R.id.ic_back_dialog);
        close_dialog = findViewById(R.id.ic_close_dialog);

        ic_back_dialog.setOnClickListener(view -> {
            finish();
        });
        close_dialog.setOnClickListener(view -> {
            showExitDialog();
        });

        rvSuggestedAssessment = findViewById(R.id.rv_suggested_assessment);
        rvAllAssessment = findViewById(R.id.rv_all_assessment);
        rvCurated = findViewById(R.id.rv_curated);
        tv_curated = findViewById(R.id.tv_curated);

        assessments = (Assessments) getIntent().getSerializableExtra("AssessmentData");
        selectedAssessment = getIntent().getStringExtra("SelectedAssessment");

        if (selectedAssessment != null) {
            showDisclaimerDialog(selectedAssessment);
        } else {
            getCurated();
        }

        if (assessments != null) {
            SuggestedAssessments suggestedAssessments = assessments.suggestedAssessments;

            rvSuggestedAssessment.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            rvAllAssessment.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            rvCurated.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

            suggestedAssessmentAdapter = new SuggestedAssessmentAdapter(this, suggestedAssessmentString, this::showDisclaimerDialog);
            rvSuggestedAssessment.setAdapter(suggestedAssessmentAdapter);
            rvSuggestedAssessment.scrollToPosition(0);

            AllAssessment assessment = assessments.allAssessment;

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
        setDialogText(tvItem1, tvItem2, tvItem3, header);
        ImageView imgClose = dialog.findViewById(R.id.ic_close_dialog);
        imgClose.setOnClickListener(view -> {
            dialog.dismiss();
            if (selectedAssessment != null) {
                finish();
            }
        });

        btnTakeAssessment.setOnClickListener(view -> {
            Intent intent = new Intent(MASuggestedAssessmentActivity.this, MAAssessmentQuestionaireActivity.class);
            intent.putExtra("AssessmentType", header);
            startActivity(intent);
        });
        // Show the dialog
        dialog.show();
    }

    private void setDialogText(TextView tvItem1, TextView tvItem2, TextView tvItem3, String header) {
        switch (header) {
            case "DASS-21": {
                tvItem1.setText(AppConstants.dass21FirstPara);
                tvItem2.setText(AppConstants.dass21SecondPara);
                tvItem3.setText(AppConstants.dass21ThirdPara);
                break;
            }
            case "Sleep Audit": {
                tvItem1.setText(AppConstants.ssFirstPara);
                tvItem2.setText(AppConstants.ssSecondPara);
                tvItem3.setText(AppConstants.ssThirdPara);
                break;
            }
            case "GAD-7": {
                tvItem1.setText(AppConstants.gad7FirstPara);
                tvItem2.setText(AppConstants.gad7SecondPara);
                tvItem3.setText(AppConstants.gad7ThirdPara);
                break;
            }
            case "OHQ": {
                tvItem1.setText(AppConstants.ohqFirstPara);
                tvItem2.setText(AppConstants.ohqSecondPara);
                tvItem3.setText(AppConstants.ohqThirdPara);
                break;
            }
            case "CAS": {
                tvItem1.setText(AppConstants.casFirstPara);
                tvItem2.setText(AppConstants.casSecondPara);
                tvItem3.setText(AppConstants.casThirdPara);
                break;
            }
            case "PHQ-9": {
                tvItem1.setText(AppConstants.phq9FirstPara);
                tvItem2.setText(AppConstants.phq9SecondPara);
                tvItem3.setText(AppConstants.phq9ThirdPara);
                break;
            }

        }
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

        Call<ResponseBody> call = apiService.getCuratedAssessment(sharedPreferenceManager.getAccessToken(), curatedUserData);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String jsonString = response.body().string();
                        Gson gson = new Gson();
                        /*****
                         * Sudhir will complete this part
                         */
                        tv_curated.setVisibility(View.VISIBLE);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    tv_curated.setVisibility(View.GONE);
                    //Toast.makeText(MASuggestedAssessmentActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                handleNoInternetView(t);
            }
        });
    }

    private void showExitDialog() {
        // Create the dialog
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_exit_dialog_mind);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = dialog.getWindow();
        // Set the dim amount
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.dimAmount = 0.7f; // Adjust the dim amount (0.0 - 1.0)
        window.setAttributes(layoutParams);

        // Find views from the dialog layout
        //ImageView dialogIcon = dialog.findViewById(R.id.img_close_dialog);
        ImageView dialogImage = dialog.findViewById(R.id.dialog_image);
        TextView dialogText = dialog.findViewById(R.id.dialog_text);
        Button dialogButtonStay = dialog.findViewById(R.id.dialog_button_stay);
        Button dialogButtonExit = dialog.findViewById(R.id.dialog_button_exit);

        // Optional: Set dynamic content
        // dialogText.setText("Please find a quiet and comfortable place before starting");

        // Set button click listener
        dialogButtonStay.setOnClickListener(v -> {
            // Perform your action
            dialog.dismiss();
        });
        dialogButtonExit.setOnClickListener(v -> {
            dialog.dismiss();
            this.finish();
        });

        dialog.show();
    }
}
