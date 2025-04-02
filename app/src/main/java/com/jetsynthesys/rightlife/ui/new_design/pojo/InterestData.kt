package com.jetsynthesys.rightlife.ui.new_design.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class InterestData {
    @SerializedName("sectionTitle")
    @Expose
     var sectionTitle: String? = null

    @SerializedName("sectionSubtitle")
    @Expose
     var sectionSubtitle: String? = null

    @SerializedName("data")
    @Expose
     var InterestDatata: List<InterestDataList>? = null


}