package com.example.rlapp.ui.new_design.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class OnboardingModuleRequest {
    @SerializedName("selectedModule")
    @Expose
    var selectedModule: String? = null

    @SerializedName("selectedOptions")
    @Expose
    var selectedOptions: List<String>? = null
}