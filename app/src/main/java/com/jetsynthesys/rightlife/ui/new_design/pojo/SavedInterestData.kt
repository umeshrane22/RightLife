package com.jetsynthesys.rightlife.ui.new_design.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SavedInterestData {
    @SerializedName("sectionTitle")
    @Expose
    var sectionTitle: String? = null

    @SerializedName("sectionSubtitle")
    @Expose
    var sectionSubtitle: String? = null

    @SerializedName("matchingUserIntrest")
    @Expose
    var matchingUserIntrest: List<InterestDataList>? = null
}