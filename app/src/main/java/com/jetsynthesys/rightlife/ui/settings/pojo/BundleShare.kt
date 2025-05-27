package com.jetsynthesys.rightlife.ui.settings.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BundleShare {
    @SerializedName("INR")
    @Expose
    var inr: Double? = null

    @SerializedName("USD")
    @Expose
    var usd: Double? = null
}