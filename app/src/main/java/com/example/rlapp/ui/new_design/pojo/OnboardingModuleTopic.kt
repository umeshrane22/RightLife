package com.example.rlapp.ui.new_design.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class OnboardingModuleTopic {
    @SerializedName("onboardingDataId")
    @Expose
    var onboardingDataId: String? = null

    @SerializedName("onboardingModuleTopic")
    @Expose
    var onboardingModuleTopic: String? = null

    @SerializedName("onboardingModuleTopicData")
    @Expose
    var onboardingModuleTopicData: String? = null

    @SerializedName("onboardingModuleThumbnail")
    @Expose
    var onboardingModuleThumbnail: String? = null

    @SerializedName("onboardingModuleDarkThemeThumbnail")
    @Expose
    var onboardingModuleDarkThemeThumbnail: String? = null

    @SerializedName("isActive")
    @Expose
    var isActive: Boolean? = null

    @SerializedName("_id")
    @Expose
    var id: String? = null
}