package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class HeartRateResponse(
    @SerializedName("status_code") val statusCode: Int,
    @SerializedName("message") val message: String,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("end_date") val endDate: String,
    @SerializedName("active_heart_rate_totals") val activeHeartRateTotals: List<HeartRate>?
)

data class HeartRate(
    @SerializedName("heart_rate") val heartRate: Double?,
    @SerializedName("date") val date: String
)
