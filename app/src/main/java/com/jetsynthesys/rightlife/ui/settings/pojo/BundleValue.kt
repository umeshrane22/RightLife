package com.jetsynthesys.rightlife.ui.settings.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BundleValue {
    @SerializedName("INR")
    @Expose
    var inr: Int? = null

    @SerializedName("USD")
    @Expose
    var usd: Int? = null
}