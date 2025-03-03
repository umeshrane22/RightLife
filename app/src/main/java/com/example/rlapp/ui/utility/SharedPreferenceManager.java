package com.example.rlapp.ui.utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.rlapp.apimodel.userdata.UserProfileResponse;
import com.example.rlapp.ui.mindaudit.MindAuditAssessmentSaveRequest;
import com.example.rlapp.ui.mindaudit.UserEmotions;
import com.example.rlapp.ui.new_design.pojo.OnboardingQuestionRequest;
import com.google.gson.Gson;

public class SharedPreferenceManager {

    private static final String PREF_NAME = "app_shared_prefs"; // File name for SharedPreferences
    private static SharedPreferenceManager instance;
    private final SharedPreferences sharedPreferences;

    private SharedPreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // Singleton instance
    public static SharedPreferenceManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferenceManager(context.getApplicationContext());
        }
        return instance;
    }

    // Method to save the access token
    public void saveAccessToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SharedPreferenceConstants.ACCESS_TOKEN, token);
        editor.apply(); // Apply changes asynchronously
    }

    // Method to retrieve the access token
    public String getAccessToken() {
        return sharedPreferences.getString(SharedPreferenceConstants.ACCESS_TOKEN, "");
    }

    // Method to save the user ID
    public void saveUserId(String userId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SharedPreferenceConstants.USER_ID, userId);
        editor.apply();
    }

    // Method to retrieve the user ID
    public String getUserId() {
        return sharedPreferences.getString(SharedPreferenceConstants.USER_ID, "");
    }

    // Clear the access token and user ID (for example, when logging out)
    public void clearData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(SharedPreferenceConstants.ACCESS_TOKEN);
        editor.remove(SharedPreferenceConstants.USER_ID);
        editor.apply();
    }

    public void saveUserProfile(UserProfileResponse userProfileResponse) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(userProfileResponse);
        prefsEditor.putString(SharedPreferenceConstants.USER_PROFILE, json);
        prefsEditor.apply();
    }

    public UserEmotions getUserEmotions() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(SharedPreferenceConstants.USER_EMOTIONS, "");
        UserEmotions obj = gson.fromJson(json, UserEmotions.class);
        return obj;
    }

    public void saveUserEmotions(UserEmotions userEmotions) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(userEmotions);
        prefsEditor.putString(SharedPreferenceConstants.USER_EMOTIONS, json);
        prefsEditor.apply();
    }

    public UserProfileResponse getUserProfile() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(SharedPreferenceConstants.USER_PROFILE, "");
        UserProfileResponse obj = gson.fromJson(json, UserProfileResponse.class);
        return obj;
    }

    public void saveVoiceScanAnswerId(String answerId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SharedPreferenceConstants.VOICE_SCAN_ANSWER_ID, answerId);
        editor.apply();
    }

    public String getVoiceScanAnswerId() {
        return sharedPreferences.getString(SharedPreferenceConstants.VOICE_SCAN_ANSWER_ID, "");
    }

    public void saveOnboardingQuestionAnswer(OnboardingQuestionRequest onboardingQuestionRequest) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(onboardingQuestionRequest);
        prefsEditor.putString(SharedPreferenceConstants.ON_BOARDING_QUESTIONS, json);
        prefsEditor.apply();
    }

    public OnboardingQuestionRequest getOnboardingQuestionRequest() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(SharedPreferenceConstants.ON_BOARDING_QUESTIONS, "");
        OnboardingQuestionRequest obj = gson.fromJson(json, OnboardingQuestionRequest.class);
        if (obj == null){
            obj = new OnboardingQuestionRequest();
        }
        return obj;
    }

    public void clearOnboardingQuestionRequest(){
        sharedPreferences.edit().remove(SharedPreferenceConstants.ON_BOARDING_QUESTIONS).apply();
    }

    public void setSelectedOnboardingModule(String moduleName){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SharedPreferenceConstants.ON_BOARDING_SELECTED_MODULE, moduleName);
        editor.apply();
    }

    public String getSelectedOnboardingModule() {
        return sharedPreferences.getString(SharedPreferenceConstants.ON_BOARDING_SELECTED_MODULE, "");
    }

    public void saveMindAuditRequest(MindAuditAssessmentSaveRequest mindAuditAssessmentSaveRequest) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mindAuditAssessmentSaveRequest);
        prefsEditor.putString(SharedPreferenceConstants.MIND_AUDIT_FEELINGS, json);
        prefsEditor.apply();
    }

    public MindAuditAssessmentSaveRequest getMindAuditRequest() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(SharedPreferenceConstants.MIND_AUDIT_FEELINGS, "");
        MindAuditAssessmentSaveRequest obj = gson.fromJson(json, MindAuditAssessmentSaveRequest.class);
        if (obj == null){
            obj = new MindAuditAssessmentSaveRequest();
        }
        return obj;
    }

    public void clearMindAuditRequest(){
        sharedPreferences.edit().remove(SharedPreferenceConstants.MIND_AUDIT_FEELINGS).apply();
    }

    public void saveAppMode(String mode){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SharedPreferenceConstants.APP_MODE, mode);
        editor.apply();
    }

    public String getAppMode() {
        return sharedPreferences.getString(SharedPreferenceConstants.APP_MODE, "System");
    }

}

