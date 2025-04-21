package com.jetsynthesys.rightlife.newdashboard.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class FacialScanReportData {
    @SerializedName("_id")
    @Expose
    var id: Any? = null

    @SerializedName("totalCount")
    @Expose
    var totalCount: Double? = null

    @SerializedName("minValue")
    @Expose
    var minValue: Double? = null

    @SerializedName("maxValue")
    @Expose
    var maxValue: Double? = null

    @SerializedName("avgValue")
    @Expose
    var avgValue: Double? = null

    @SerializedName("deffination")
    @Expose
    var deffination: String? = null


    @SerializedName("yAxisValue")
    @Expose
    var yAxisValue: ArrayList<Int>? = null

    @SerializedName("data")
    @Expose
    var data: ArrayList<FacialScanReport>? = null

}
