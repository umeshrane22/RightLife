package com.jetsynthesys.rightlife.ai_package.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName

@Parcelize
data class HeartRateMoveLanding(
    @SerialName("creation_datetime") val creationDatetime: String?,
    @SerialName("unit") val unit: String?,
    @SerialName("value") val value: Int
) :Parcelable
