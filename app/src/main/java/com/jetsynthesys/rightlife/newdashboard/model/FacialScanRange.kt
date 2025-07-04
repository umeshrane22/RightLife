package com.jetsynthesys.rightlife.newdashboard.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FacialScanRange {
    @SerializedName("lowerRange")
    @Expose
    var lowerRange: Double? = null

    @SerializedName("upperRange")
    @Expose
    var upperRange: Double? = null

    @SerializedName("unit")
    @Expose
    var unit: String? = null

    @SerializedName("indicator")
    @Expose
    var indicator: String? = null

    @SerializedName("colour")
    @Expose
    var colour: String? = null

    @SerializedName("implication")
    @Expose
    var implication: String? = null

    @SerializedName("homeScreen")
    @Expose
    var homeScreen: String? = null

    @SerializedName("value")
    @Expose
    var value: String? = null
}
