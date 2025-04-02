package com.jetsynthesys.rightlife.ui.drawermenu;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PreferenceAnswer {

    @SerializedName("onboardingQuestionId")
    @Expose
    private String onboardingQuestionId;
    @SerializedName("userAnswerId")
    @Expose
    private String userAnswerId;
    @SerializedName("onboardingQuestionsOptions")
    @Expose
    private List<String> onboardingQuestionsOptions;

    public String getOnboardingQuestionId() {
        return onboardingQuestionId;
    }

    public void setOnboardingQuestionId(String onboardingQuestionId) {
        this.onboardingQuestionId = onboardingQuestionId;
    }

    public String getUserAnswerId() {
        return userAnswerId;
    }

    public void setUserAnswerId(String userAnswerId) {
        this.userAnswerId = userAnswerId;
    }

    public List<String> getOnboardingQuestionsOptions() {
        return onboardingQuestionsOptions;
    }

    public void setOnboardingQuestionsOptions(List<String> onboardingQuestionsOptions) {
        this.onboardingQuestionsOptions = onboardingQuestionsOptions;
    }

}