package com.example.rlapp.ai_package.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
data class RestingHeartRate(
    @SerialName("creation_datetime") val creationDatetime: String?,
    @SerialName("unit") val unit: String?,
    @SerialName("value") val value: Int
): Parcelable
