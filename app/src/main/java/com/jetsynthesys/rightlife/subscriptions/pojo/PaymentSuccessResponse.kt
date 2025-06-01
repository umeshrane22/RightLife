package com.jetsynthesys.rightlife.subscriptions.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PaymentSuccessResponse {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("statusCode")
    @Expose
    var statusCode: Int? = null

    @SerializedName("data")
    @Expose
    var data: PaymentSuccessData? = null
}