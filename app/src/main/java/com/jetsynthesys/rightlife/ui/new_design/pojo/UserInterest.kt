package com.jetsynthesys.rightlife.ui.new_design.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserInterest {
    @SerializedName("moduleName")
    @Expose
    var moduleName: String? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("topics")
    @Expose
    var topics: List<InterestTopic>? = null
}