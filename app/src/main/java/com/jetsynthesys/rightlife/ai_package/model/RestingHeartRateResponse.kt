package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class RestingHeartRateResponse(
    @SerializedName("status_code") val statusCode: Int,
    @SerializedName("message") val message: String,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("end_date") val endDate: String,
    @SerializedName("resting_heart_rate_totals") val restingHeartRate: List<RestingHeartRateMain>,
    @SerializedName("current_avg_bpm") val currentAvgBpm: Double,
    @SerializedName("progress_percentage") val progressPercentage: Double,
    @SerializedName("progress_sign") val progressSign: String,
    @SerializedName("heading") val heading: String,
    @SerializedName("description") val description: String
)

// Data class for each resting heart rate record
data class RestingHeartRateMain(
    @SerializedName("bpm") val bpm: Double,
    @SerializedName("date") val date: String
)