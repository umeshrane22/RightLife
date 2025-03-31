package com.example.rlapp.ai_package.model

import com.google.gson.annotations.SerializedName

data class SleepLandingResponse(
    @SerializedName("message")
    var message : String?,
    @SerializedName("data")
    var sleepLandingData    : SleepLandingData?
)
