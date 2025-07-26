package com.jetsynthesys.rightlife.ui.healthcam

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Service {
    @SerializedName("identifier")
    @Expose
    var identifier: String? = null

    @SerializedName("measurementId")
    @Expose
    var measurementId: String? = null

    @SerializedName("scanUrl")
    @Expose
    var scanUrl: String? = null
}