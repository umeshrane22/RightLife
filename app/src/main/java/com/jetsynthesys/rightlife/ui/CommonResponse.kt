package com.jetsynthesys.rightlife.ui

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CommonResponse {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("statusCode")
    @Expose
    var statusCode: Int? = null

    @SerializedName("successMessage")
    @Expose
    var successMessage: String? = null
}