package com.jetsynthesys.rightlife.ai_package.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName

@Parcelize
data class AvgSteps(
    @SerialName("date") val date: String?,
    @SerialName("avg_steps") val avgSteps: Int
) :Parcelable
