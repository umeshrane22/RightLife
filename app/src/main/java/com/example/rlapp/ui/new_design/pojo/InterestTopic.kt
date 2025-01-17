package com.example.rlapp.ui.new_design.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class InterestTopic {
    @SerializedName("moduleName")
    @Expose
    var moduleName: String? = null

    @SerializedName("topic")
    @Expose
    var topic: String? = null

    @SerializedName("_id")
    @Expose
    var id: String? = null
}