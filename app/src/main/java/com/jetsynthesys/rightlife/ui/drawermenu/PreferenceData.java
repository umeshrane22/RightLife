package com.jetsynthesys.rightlife.ui.drawermenu;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PreferenceData {

    @SerializedName("onboardingPrompt")
    @Expose
    private List<OnboardingPrompt> onboardingPrompt;

    public List<OnboardingPrompt> getOnboardingPrompt() {
        return onboardingPrompt;
    }

    public void setOnboardingPrompt(List<OnboardingPrompt> onboardingPrompt) {
        this.onboardingPrompt = onboardingPrompt;
    }

}