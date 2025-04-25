package com.jetsynthesys.rightlife.ai_package.model.response

import com.google.gson.annotations.SerializedName

data class ConsumedFatResponse(
    @SerializedName("status_code") val statusCode: Int,
    @SerializedName("message") val message: String,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("end_date") val endDate: String,
    @SerializedName("consumed_fat_totals") val consumedFatTotals: List<FatTotal>,
    @SerializedName("total_fat") val totalFat: Double,
    @SerializedName("current_avg_fat") val currentAvgFat: Double,
    @SerializedName("progress_percentage") val progressPercentage: Double,
    @SerializedName("progress_sign") val progressSign: String
)

data class FatTotal(
    @SerializedName("fat_consumed") val fatConsumed: Double,
    @SerializedName("date") val date: String
)