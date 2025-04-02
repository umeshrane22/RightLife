package com.jetsynthesys.rightlife.newdashboard.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FacialScanReportResponse {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("statusCode")
    @Expose
    var statusCode: Int? = null

    @SerializedName("data")
    @Expose
    var data: FacialScanReportData? = null
}