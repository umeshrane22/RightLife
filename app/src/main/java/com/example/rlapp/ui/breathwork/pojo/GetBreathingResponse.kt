package com.example.rlapp.ui.breathwork.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetBreathingResponse {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("statusCode")
    @Expose
    var statusCode: Int? = null

    @SerializedName("data")
    @Expose
    var data: ArrayList<BreathingData>? = null
}