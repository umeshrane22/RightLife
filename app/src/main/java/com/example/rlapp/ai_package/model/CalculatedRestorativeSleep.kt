package com.example.rlapp.ai_package.model

import com.google.gson.annotations.SerializedName

data class CalculatedRestorativeSleep(
    @SerializedName("restorativesleeppercentage")
    var restorativesleeppercentage : Double?,
    @SerializedName("restorativesleepmessage")
    var restorativesleepmessage    : String?,
    @SerializedName("restorativeactionstep")
    var restorativeactionstep      : String?
)
