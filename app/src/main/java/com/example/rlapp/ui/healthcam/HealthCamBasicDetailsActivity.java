package com.example.rlapp.ui.healthcam;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rlapp.R;
import com.example.rlapp.RetrofitData.ApiClient;
import com.example.rlapp.RetrofitData.ApiService;
import com.example.rlapp.apimodel.UserAuditAnswer.Answer;
import com.example.rlapp.apimodel.UserAuditAnswer.UserAnswerRequest;
import com.example.rlapp.ui.healthaudit.questionlist.Option;
import com.example.rlapp.ui.healthaudit.questionlist.Question;
import com.example.rlapp.ui.healthaudit.questionlist.QuestionListHealthAudit;
import com.example.rlapp.ui.payment.AccessPaymentActivity;
import com.example.rlapp.ui.sdkpackage.HealthCamRecorderActivity;
import com.example.rlapp.ui.utility.SharedPreferenceConstants;
import com.example.rlapp.ui.utility.SharedPreferenceManager;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.rlapp.ui.utility.ConversionUtils.convertCentimeterToFtInch;
import static com.example.rlapp.ui.utility.ConversionUtils.convertCentimeterToInch;
import static com.example.rlapp.ui.utility.ConversionUtils.convertFeetToCentimeter;
import static com.example.rlapp.ui.utility.ConversionUtils.convertFeetToInch;
import static com.example.rlapp.ui.utility.ConversionUtils.convertInchToCentimeter;
import static com.example.rlapp.ui.utility.ConversionUtils.convertInchToFeet;
import static com.example.rlapp.ui.utility.ConversionUtils.convertKgToLbs;
import static com.example.rlapp.ui.utility.ConversionUtils.convertLbsToKgs;

public class HealthCamBasicDetailsActivity extends AppCompatActivity {

    private EditText edtFirstName, edtLastName, edtHeight, edtWeight, edtAge, edtFt, edtInch;
    private TextView edtSpinnerHeight, edtSpinnerWeight, edtGender, edtSmoke, edtBPMedication,
            edtDiabetic, tvFirstName, tvLastName, tvHeight, tvWeight, tvAge, tvGender, tvSmoke,
            tvBpMedication, tvDiabetic, tvFt, tvInch;
    private LinearLayout llHeight, llWeight, llHeightFtInch;
    private Button btnStartScan;
    private QuestionListHealthAudit responseObj;
    private ArrayList<Option> heightUnits = new ArrayList<>();
    private ArrayList<Option> weightUnits = new ArrayList<>();
    private ArrayList<Option> smokeOptions = new ArrayList<>();
    private ArrayList<Option> diabeticsOptions = new ArrayList<>();
    private ArrayList<Option> bpMedicationOptions = new ArrayList<>();
    private ArrayList<Option> genderOptions = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_cam_basic_details);

        getViews();

        getQuestionerList();
        findViewById(R.id.ic_back_dialog).setOnClickListener(view -> {
            finish();
        });

        btnStartScan.setOnClickListener(view -> {
            String firstName = edtFirstName.getText().toString();
            String lastName = edtLastName.getText().toString();
            String height = edtHeight.getText().toString();
            String weight = edtWeight.getText().toString();
            String age = edtAge.getText().toString();
            String gender = edtGender.getText().toString();
            String smoke = edtSmoke.getText().toString();
            String bpMedication = edtBPMedication.getText().toString();
            String diabetic = edtDiabetic.getText().toString();

            if (firstName.isEmpty()) {
                Toast.makeText(this, "First Name is required", Toast.LENGTH_SHORT).show();
            } else if (lastName.isEmpty()) {
                Toast.makeText(this, "Last Name is required", Toast.LENGTH_SHORT).show();
            } else if (height.isEmpty()) {
                Toast.makeText(this, "Height is required", Toast.LENGTH_SHORT).show();
            } else if (weight.isEmpty()) {
                Toast.makeText(this, "Weight is required", Toast.LENGTH_SHORT).show();
            } else if (age.isEmpty()) {
                Toast.makeText(this, "Age is required", Toast.LENGTH_SHORT).show();
            } else if (smoke.isEmpty() || bpMedication.isEmpty() || diabetic.isEmpty()) {
                Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show();
            } else {
                double heightInt = Double.parseDouble(height);
                double weightInt = Double.parseDouble(weight);
                int ageInt = Integer.parseInt(age);
                if (heightInt == 0) {
                    Toast.makeText(this, "Height can not be 0", Toast.LENGTH_SHORT).show();
                } else if (weightInt == 0) {
                    Toast.makeText(this, "Weight can not be 0", Toast.LENGTH_SHORT).show();
                } else if (ageInt == 0) {
                    Toast.makeText(this, "Age can not be 0", Toast.LENGTH_SHORT).show();
                } else {
                    ArrayList<Answer> answers = new ArrayList<>();

                    for (Question question : responseObj.getQuestionData().getQuestionList()) {
                        switch (question.getQuestion()) {
                            case "first_name":
                                answers.add(new Answer(question.getQuestion(), List.of(new com.example.rlapp.apimodel.UserAuditAnswer.Option(firstName))));
                                break;
                            case "last_name":
                                answers.add(new Answer(question.getQuestion(), List.of(new com.example.rlapp.apimodel.UserAuditAnswer.Option(lastName))));
                                break;
                            case "height":
                                answers.add(new Answer(question.getQuestion(), List.of(new com.example.rlapp.apimodel.UserAuditAnswer.Option(height))));
                                break;
                            case "weight":
                                answers.add(new Answer(question.getQuestion(), List.of(new com.example.rlapp.apimodel.UserAuditAnswer.Option(weight))));
                                break;
                            case "age":
                                answers.add(new Answer(question.getQuestion(), List.of(new com.example.rlapp.apimodel.UserAuditAnswer.Option(age))));
                                break;
                            case "gender":
                                String selectedGender = "";
                                for (Option gen : genderOptions) {
                                    if (gen.getOptionText().equals(gender)) {
                                        selectedGender = gen.getOptionPosition();
                                    }
                                }
                                answers.add(new Answer(question.getQuestion(), List.of(new com.example.rlapp.apimodel.UserAuditAnswer.Option(selectedGender))));
                                break;
                            case "smoking":
                                String selectedSmoke = "";
                                for (Option sm : smokeOptions) {
                                    if (sm.getOptionText().equals(smoke)) {
                                        selectedSmoke = sm.getOptionPosition();
                                    }
                                }
                                answers.add(new Answer(question.getQuestion(), List.of(new com.example.rlapp.apimodel.UserAuditAnswer.Option(selectedSmoke))));
                                break;
                            case "bloodpressuremedication":
                                String selectedBP = "";
                                for (Option bp : bpMedicationOptions) {
                                    if (bp.getOptionText().equals(bpMedication)) {
                                        selectedBP = bp.getOptionPosition();
                                    }
                                }
                                answers.add(new Answer(question.getQuestion(), List.of(new com.example.rlapp.apimodel.UserAuditAnswer.Option(selectedBP))));
                                break;
                            case "diabetes":
                                String selectedDiabetic = "";
                                for (Option dia : diabeticsOptions) {
                                    if (dia.getOptionText().equals(gender)) {
                                        selectedDiabetic = dia.getOptionPosition();
                                    }
                                }
                                answers.add(new Answer(question.getQuestion(), List.of(new com.example.rlapp.apimodel.UserAuditAnswer.Option(selectedDiabetic))));
                                break;
                        }
                    }

                    submitData(answers);
                }
            }
        });

    }

    private void setData() {
        for (Question question : responseObj.getQuestionData().getQuestionList()) {
            switch (question.getQuestion()) {
                case "first_name":
                    tvFirstName.setText(question.getQuestionTxt());
                    tvFirstName.setVisibility(View.VISIBLE);
                    edtFirstName.setVisibility(View.VISIBLE);
                    break;
                case "last_name":
                    tvLastName.setText(question.getQuestionTxt());
                    tvLastName.setVisibility(View.VISIBLE);
                    edtLastName.setVisibility(View.VISIBLE);
                    break;
                case "height":
                    tvHeight.setText(question.getQuestionTxt());
                    tvHeight.setVisibility(View.VISIBLE);
                    llHeight.setVisibility(View.VISIBLE);
                    break;
                case "height_unit":
                    heightUnits.addAll(question.getOptions());
                    break;
                case "weight":
                    tvWeight.setText(question.getQuestionTxt());
                    tvWeight.setVisibility(View.VISIBLE);
                    llWeight.setVisibility(View.VISIBLE);
                    break;
                case "weight_unit":
                    weightUnits.addAll(question.getOptions());
                    break;
                case "age":
                    tvAge.setText(question.getQuestionTxt());
                    tvAge.setVisibility(View.VISIBLE);
                    edtAge.setVisibility(View.VISIBLE);
                    break;
                case "gender":
                    tvGender.setText(question.getQuestionTxt());
                    tvGender.setVisibility(View.VISIBLE);
                    edtGender.setVisibility(View.VISIBLE);
                    genderOptions.addAll(question.getOptions());
                    break;
                case "smoking":
                    tvSmoke.setText(question.getQuestionTxt());
                    tvSmoke.setVisibility(View.VISIBLE);
                    edtSmoke.setVisibility(View.VISIBLE);
                    smokeOptions.addAll(question.getOptions());
                    break;
                case "bloodpressuremedication":
                    tvBpMedication.setText(question.getQuestionTxt());
                    tvBpMedication.setVisibility(View.VISIBLE);
                    edtBPMedication.setVisibility(View.VISIBLE);
                    bpMedicationOptions.addAll(question.getOptions());
                    break;
                case "diabetes":
                    tvDiabetic.setText(question.getQuestionTxt());
                    tvDiabetic.setVisibility(View.VISIBLE);
                    edtDiabetic.setVisibility(View.VISIBLE);
                    diabeticsOptions.addAll(question.getOptions());
                    break;
            }
        }

        edtSpinnerHeight.setOnClickListener(view -> openHeightPopUp());
        edtSpinnerWeight.setOnClickListener(view -> openWeightPopup());
        edtGender.setOnClickListener(view -> openGenderPopup());
        edtSmoke.setOnClickListener(view -> openSmokePopup());
        edtBPMedication.setOnClickListener(view -> openBPMedicationPopUp());
        edtDiabetic.setOnClickListener(view -> openDiabeticPopup());
    }

    private void getViews() {
        edtFirstName = findViewById(R.id.edt_first_name);
        edtLastName = findViewById(R.id.edt_last_name);
        edtHeight = findViewById(R.id.edt_height);
        edtSpinnerHeight = findViewById(R.id.edt_spinnerheight);
        edtWeight = findViewById(R.id.edt_weight);
        edtSpinnerWeight = findViewById(R.id.edt_spinner_weight);
        edtAge = findViewById(R.id.edt_age);
        edtGender = findViewById(R.id.edt_spinner_gender);
        edtSmoke = findViewById(R.id.edt_spinner_smoke);
        edtBPMedication = findViewById(R.id.edt_spinner_bp_medication);
        edtDiabetic = findViewById(R.id.edt_spinner_diabetic);
        btnStartScan = findViewById(R.id.btn_start_scan);
        tvFirstName = findViewById(R.id.tv_first_name);
        tvLastName = findViewById(R.id.tv_last_name);
        tvAge = findViewById(R.id.tv_age);
        tvGender = findViewById(R.id.tv_gender);
        tvSmoke = findViewById(R.id.tv_smoke);
        tvBpMedication = findViewById(R.id.tv_bp_medication);
        tvDiabetic = findViewById(R.id.tv_diabetic);
        llHeight = findViewById(R.id.ll_height);
        llWeight = findViewById(R.id.ll_weight);
        tvHeight = findViewById(R.id.tv_height);
        tvWeight = findViewById(R.id.tv_weight);
        llHeightFtInch = findViewById(R.id.ll_height_ft_inch);
        tvFt = findViewById(R.id.txt_ft);
        tvInch = findViewById(R.id.txt_inch);
        edtFt = findViewById(R.id.edt_ft);
        edtInch = findViewById(R.id.edt_inch);
    }

    private void openHeightPopUp() {
        PopupMenu popupMenu = new PopupMenu(this, edtSpinnerHeight);
        for (Option option : heightUnits) {
            popupMenu.getMenu().add(option.getOptionText().toLowerCase());
        }
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            String previousSelection = edtSpinnerHeight.getText().toString();
            if (previousSelection.equals(menuItem.toString())) {
                return true;
            }
            String height;
            switch (menuItem.toString()) {
                case "cm":
                    if (previousSelection.equals("inch")) {
                        height = convertInchToCentimeter(edtHeight.getText().toString());
                        edtHeight.setText(height);
                    } else { //ftInch
                        llHeightFtInch.setVisibility(View.GONE);
                        edtHeight.setVisibility(View.VISIBLE);
                        height = convertFeetToCentimeter(edtFt.getText().toString() + "." + edtInch.getText().toString());
                        edtHeight.setText(height);
                    }
                    break;
                case "inch":
                    if (previousSelection.equals("cm")) {
                        height = convertCentimeterToInch(edtHeight.getText().toString());
                        edtHeight.setText(height);
                    } else { //FtInch
                        llHeightFtInch.setVisibility(View.GONE);
                        edtHeight.setVisibility(View.VISIBLE);
                        height = convertFeetToInch(edtFt.getText().toString() + "." + edtInch.getText().toString());
                        edtHeight.setText(height);
                    }
                    break;
                case "ft_and_inches":
                    edtHeight.setVisibility(View.GONE);
                    llHeightFtInch.setVisibility(View.VISIBLE);
                    if (previousSelection.equals("cm")) {
                        String cms = convertCentimeterToFtInch(edtHeight.getText().toString());
                        String[] strings = cms.split("\\.");
                        edtFt.setText(strings[0]);
                        if (strings.length > 1) {
                            edtInch.setText(strings[1]);
                        }
                    } else { //Inch
                        String inches = convertInchToFeet(edtHeight.getText().toString());
                        String[] strings = inches.split("\\.");
                        edtFt.setText(strings[0]);
                        if (strings.length > 1) {
                            edtInch.setText(strings[1]);
                        }
                    }
                    break;
            }
            edtSpinnerHeight.setText(menuItem.toString());
            return true;
        });

        popupMenu.show();
    }

    private void openWeightPopup() {
        PopupMenu popupMenu = new PopupMenu(this, edtSpinnerWeight);
        for (Option option : weightUnits) {
            popupMenu.getMenu().add(option.getOptionText().toLowerCase());
        }
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            edtSpinnerWeight.setText(menuItem.toString());
            String weight;
            if ("kgs".equals(menuItem.toString())) {
                weight = convertKgToLbs(edtWeight.getText().toString());
            } else {
                weight = convertLbsToKgs(edtWeight.getText().toString());
            }
            edtWeight.setText(weight);
            return true;
        });
        popupMenu.show();
    }

    private void openGenderPopup() {
        PopupMenu popupMenu = new PopupMenu(this, edtGender);
        for (Option option : genderOptions) {
            popupMenu.getMenu().add(option.getOptionText());
        }
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            edtGender.setText(menuItem.toString());
            return true;
        });
        popupMenu.show();
    }

    private void openSmokePopup() {
        PopupMenu popupMenu = new PopupMenu(this, edtSmoke);
        for (Option option : smokeOptions) {
            popupMenu.getMenu().add(option.getOptionText());
        }
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            edtSmoke.setText(menuItem.toString());
            return true;
        });
        popupMenu.show();
    }

    private void openBPMedicationPopUp() {
        PopupMenu popupMenu = new PopupMenu(this, edtBPMedication);
        for (Option option : bpMedicationOptions) {
            popupMenu.getMenu().add(option.getOptionText());
        }
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            edtBPMedication.setText(menuItem.toString());
            return true;
        });
        popupMenu.show();
    }

    private void openDiabeticPopup() {
        PopupMenu popupMenu = new PopupMenu(this, edtDiabetic);
        for (Option option : diabeticsOptions) {
            popupMenu.getMenu().add(option.getOptionText());
        }
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            edtDiabetic.setText(menuItem.toString());
            return true;
        });
        popupMenu.show();
    }

    private void getQuestionerList() {
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString(SharedPreferenceConstants.ACCESS_TOKEN, null);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<JsonElement> call = apiService.getsubmoduletest(accessToken, "FACIAL_SCAN");
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("AAAA API Response", "SUB subModule list - : " + response);
                    Gson gson = new Gson();
                    String jsonResponse = gson.toJson(response.body());
                    responseObj = gson.fromJson(jsonResponse, QuestionListHealthAudit.class);
                    Log.d("AAAA API Response", "ReportId - : " + jsonResponse);
                    setData();
                } else {
                    Toast.makeText(HealthCamBasicDetailsActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(HealthCamBasicDetailsActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API ERROR", "onFailure: " + t.getMessage());
                t.printStackTrace();  // Print the full stack trace for more details
            }
        });
    }

    private void submitData(ArrayList formData) {

        List<Answer> answers = new ArrayList<>(formData);
        UserAnswerRequest request = new UserAnswerRequest("", responseObj.getQuestionData().getId(), answers);
        submitAnswerRequest(request);
    }

    void submitAnswerRequest(UserAnswerRequest requestAnswer) {
        String accessToken = SharedPreferenceManager.getInstance(this).getAccessToken();

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        // Make the API call
        Call<JsonElement> call = apiService.postAnswerRequest(accessToken, "FACIAL_SCAN", requestAnswer);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Gson gson = new Gson();
                    String jsonResponse = gson.toJson(response.body());

                    HealthCamSubmitResponse healthCamSubmitResponse = gson.fromJson(jsonResponse, HealthCamSubmitResponse.class);
                    if (healthCamSubmitResponse.getStatus().equalsIgnoreCase("PAYMENT_INPROGRESS")) {
                        Intent intent = new Intent(HealthCamBasicDetailsActivity.this, AccessPaymentActivity.class);
                        intent.putExtra("ACCESS_VALUE", "FACIAL_SCAN");
                        startActivity(intent);
                    } else {
                        submitFacialScan(healthCamSubmitResponse.getData().getAnswerId());
                    }

                } else {
                    Toast.makeText(HealthCamBasicDetailsActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(HealthCamBasicDetailsActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void submitFacialScan(String reportId) {
        String accessToken = SharedPreferenceManager.getInstance(this).getAccessToken();
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<ResponseBody> call = apiService.getMyRLHealthCamResult(accessToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Gson gson = new Gson();
                    String jsonResponse = null;
                    try {
                        //jsonResponse = gson.toJson(response.body().string());
                        HealthCamReportIdResponse reportIdResponse = gson.fromJson(response.body().string(), HealthCamReportIdResponse.class);

                        Intent intent = new Intent(HealthCamBasicDetailsActivity.this, HealthCamRecorderActivity.class);
                        intent.putExtra("reportID", reportIdResponse.getData().getId());
                        intent.putExtra("USER_PROFILE_HEIGHT", edtHeight.getText().toString());
                        intent.putExtra("USER_PROFILE_WEIGHT", edtWeight.getText().toString());
                        intent.putExtra("USER_PROFILE_AGE", edtAge.getText().toString());
                        intent.putExtra("USER_PROFILE_GENDER", edtGender.getText().toString());
                        startActivity(intent);
                        finish();

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Log.d("AAAA API Response", "ReportId submit - : " + jsonResponse);
                } else {
                    Toast.makeText(HealthCamBasicDetailsActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(HealthCamBasicDetailsActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}
