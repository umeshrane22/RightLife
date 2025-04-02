package com.jetsynthesys.rightlife.ui.NewSleepSounds.userplaylistmodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NewReleaseResponse {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("statusCode")
    @Expose
    var statusCode: Int? = null

    @SerializedName("data")
    @Expose
    var data: NewReleaseData? = null
}