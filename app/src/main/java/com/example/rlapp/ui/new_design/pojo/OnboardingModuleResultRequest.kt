package com.example.rlapp.ui.new_design.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class OnboardingModuleResultRequest {
    @SerializedName("id")
    @Expose
    var id: List<String>? = null
}