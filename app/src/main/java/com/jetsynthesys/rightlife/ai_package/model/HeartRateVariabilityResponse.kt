package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class HeartRateVariabilityResponse(
    @SerializedName("status_code") val statusCode: Int,
    @SerializedName("message") val message: String,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("end_date") val endDate: String,
    @SerializedName("active_hrv_totals") val heartRateVariability: List<HeartRateVariability>,
    @SerializedName("current_avg_hrv") val currentAvgHrv: Double,
    @SerializedName("progress_percentage") val progressPercentage: Double,
    @SerializedName("progress_sign") val progressSign: String,
    @SerializedName("heading") val heading: String,
    @SerializedName("description") val description: String
)

// Data class for each heart rate variability record
data class HeartRateVariability(
    @SerializedName("hrv") val hrv: Double,
    @SerializedName("date") val date: String
)