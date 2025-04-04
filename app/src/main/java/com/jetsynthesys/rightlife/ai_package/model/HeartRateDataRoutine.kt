package com.jetsynthesys.rightlife.ai_package.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class HeartRateDataRoutine(
    val timestamp: String,
    @SerializedName("heart_rate") val heartRate: Int,
    val unit: String
) : Parcelable