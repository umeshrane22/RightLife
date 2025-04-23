package com.jetsynthesys.rightlife.newdashboard.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.jetsynthesys.rightlife.apimodel.newreportfacescan.Recommendation

class FacialScanReportDataWrapper {
    @SerializedName("result")
    @Expose
    var result: FacialScanReportData? = null

    @SerializedName("range")
    @Expose
    var range: List<FacialScanRange>? = null

    @SerializedName("recommendation")
    @Expose
    var recommendation: List<Recommendation>? = null
}
