package com.jetsynthesys.rightlife.ui.healthcam

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class HealthCamFacialScanRequest {
    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("reportId")
    @Expose
    var reportId: String? = null

    @SerializedName("reportData")
    @Expose
    var reportData: ReportData? = null
}