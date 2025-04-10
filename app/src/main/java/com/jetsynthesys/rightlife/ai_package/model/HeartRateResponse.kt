package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class HeartRateResponse(
    @SerializedName("message") val message: String,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("end_date") val endDate: String,
    @SerializedName("heart_rate") val heartRate: List<HeartRate>
)

// Data class for each heart rate record
data class HeartRate(
    @SerializedName("creation_datetime") val creationDatetime: String,
    @SerializedName("end_datetime") val endDatetime: String,
    @SerializedName("source_version") val sourceVersion: String,
    @SerializedName("start_datetime") val startDatetime: String,
    @SerializedName("record_type") val recordType: String,
    @SerializedName("unit") val unit: String,
    @SerializedName("value") val value: String, // Kept as String since JSON shows it as a string ("85")
    @SerializedName("_id") val id: String,
    @SerializedName("user_id") val userId: String
)
