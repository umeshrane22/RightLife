package com.example.rlapp.ui.new_design.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class OnboardingModuleResultData {
    @SerializedName("_id")
    @Expose
    var id: String? = null

    @SerializedName("moduleName")
    @Expose
    var moduleName: String? = null

    @SerializedName("thumbnail")
    @Expose
    var thumbnail: String? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("onboardingModuleTopics")
    @Expose
    var onboardingModuleTopics: List<OnboardingModuleTopic>? = null

    @SerializedName("createdAt")
    @Expose
    var createdAt: String? = null

    @SerializedName("updatedAt")
    @Expose
    var updatedAt: String? = null

    @SerializedName("__v")
    @Expose
    var v: Int? = null
}