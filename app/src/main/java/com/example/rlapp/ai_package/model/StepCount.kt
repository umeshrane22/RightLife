package com.example.rlapp.ai_package.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
data class StepCount(
    @SerialName("creation_datetime") val creationDatetime: String?,
    @SerialName("end_datetime") val endDatetime: String?,
    @SerialName("source_version") val sourceVersion: String?,
    @SerialName("start_datetime") val startDatetime: String?,
    @SerialName("record_type") val recordType: String?,
    @SerialName("unit") val unit: String?,
    @SerialName("value") val value: String,
    @SerialName("_id") val id: String?,
    @SerialName("user_id") val userId: String?
) : Parcelable{
    fun getValueAsInt(): Int = value.toIntOrNull() ?: 0
}
