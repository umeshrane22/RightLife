package com.jetsynthesys.rightlife.ui.healthcam

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Data {
    @SerializedName("answerId")
    @Expose
    var answerId: String? = null

    @SerializedName("isSubscribed")
    @Expose
    var isSubscribed: Boolean? = null

    @SerializedName("isFree")
    @Expose
    var isFree: Boolean? = null

    @SerializedName("subscriptionId")
    @Expose
    var subscriptionId: String? = null
}