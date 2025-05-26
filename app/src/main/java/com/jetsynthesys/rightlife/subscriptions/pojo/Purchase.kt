package com.jetsynthesys.rightlife.subscriptions.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Purchase {
    @SerializedName("planName")
    @Expose
    var planName: String? = null
}