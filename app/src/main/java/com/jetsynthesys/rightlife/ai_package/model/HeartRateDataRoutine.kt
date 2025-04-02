package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class HeartRateDataRoutine(
    val timestamp: String,
    @SerializedName("heart_rate") val heartRate: String,
    val unit: String
)