package com.jetsynthesys.rightlife.ui.profile_new.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PreSignedUrlResponse {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("statusCode")
    @Expose
    var statusCode: Int? = null

    @SerializedName("successMessage")
    @Expose
    var successMessage: String? = null

    @SerializedName("data")
    @Expose
    var data: PreSignedUrlData? = null
}