package com.example.rlapp.ai_package.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
data class AvgSteps(
    @SerialName("date") val date: String?,
    @SerialName("avg_steps") val avgSteps: Int
) :Parcelable
