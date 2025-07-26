package com.jetsynthesys.rightlife.ui.healthcam

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class HealthCamReportIdResponse {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("statusCode")
    @Expose
    var statusCode: Int? = null

    @SerializedName("successMessage")
    @Expose
    var successMessage: String? = null

    @SerializedName("data")
    @Expose
    var data: ReportIdData? = null
}