package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class HeartRateVariabilityResponse(
    @SerializedName("message") val message: String,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("end_date") val endDate: String,
    @SerializedName("heart_rate_variability") val heartRateVariability: List<HeartRateVariability>
)

// Data class for each heart rate variability record
data class HeartRateVariability(
    @SerializedName("creation_datetime") val creationDatetime: String,
    @SerializedName("end_datetime") val endDatetime: String,
    @SerializedName("source_version") val sourceVersion: String,
    @SerializedName("start_datetime") val startDatetime: String,
    @SerializedName("record_type") val recordType: String,
    @SerializedName("unit") val unit: String,
    @SerializedName("value") val value: String, // Kept as String since JSON shows it as a string ("50")
    @SerializedName("_id") val id: String,
    @SerializedName("user_id") val userId: String
)