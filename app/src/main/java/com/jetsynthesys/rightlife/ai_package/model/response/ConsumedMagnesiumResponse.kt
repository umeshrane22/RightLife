package com.jetsynthesys.rightlife.ai_package.model.response

import com.google.gson.annotations.SerializedName

data class ConsumedMagnesiumResponse(
    @SerializedName("status_code")
    val statusCode: Int,

    @SerializedName("message")
    val message: String,

    @SerializedName("start_date")
    val startDate: String,

    @SerializedName("end_date")
    val endDate: String,

    @SerializedName("consumed_magnesium_totals")
    val consumedMagnesiumTotals: List<ConsumedMagnesiumTotal>,

    @SerializedName("total_magnesium")
    val totalMagnesium: Double,

    @SerializedName("current_avg_magnesium")
    val currentAvgMagnesium: Double,

    @SerializedName("progress_percentage")
    val progressPercentage: Double,

    @SerializedName("progress_sign")
    val progressSign: String
)

data class ConsumedMagnesiumTotal(
    @SerializedName("magnesium_consumed")
    val magnesiumConsumed: Double,

    @SerializedName("date")
    val date: String
)
