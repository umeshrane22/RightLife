package com.jetsynthesys.rightlife.ui.new_design.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SavedInterestResponse {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("statusCode")
    @Expose
    var statusCode: Int? = null

    @SerializedName("data")
    @Expose
    var data: SavedInterestData? = null
}