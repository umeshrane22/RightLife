package com.jetsynthesys.rightlife.ui.new_design.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class OnboardingModuleResultData {
    @SerializedName("sectionTitle")
    @Expose
     val sectionTitle: String? = null

    @SerializedName("data")
    @Expose
     val data: List<OnboardingModuleResultDataList>? = null
}