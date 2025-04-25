package com.jetsynthesys.rightlife.ui.settings.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PurchaseHistoryResponse {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("statusCode")
    @Expose
    var statusCode: Int? = null

    @SerializedName("data")
    @Expose
    var data: PurchaseHistoryData? = null
}