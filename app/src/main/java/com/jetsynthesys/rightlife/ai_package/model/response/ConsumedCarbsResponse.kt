package com.jetsynthesys.rightlife.ai_package.model.response

import com.google.gson.annotations.SerializedName

data class ConsumedCarbsResponse(
    @SerializedName("status_code")
    val statusCode: Int,

    @SerializedName("message")
    val message: String,

    @SerializedName("start_date")
    val startDate: String,

    @SerializedName("end_date")
    val endDate: String,

    @SerializedName("consumed_carbs_totals")
    val consumedCarbsTotals: List<ConsumedCarbTotal>,

    @SerializedName("total_carbs")
    val totalCarbs: Double,

    @SerializedName("current_avg_carbs")
    val currentAvgCarbs: Double,

    @SerializedName("progress_percentage")
    val progressPercentage: Double,

    @SerializedName("progress_sign")
    val progressSign: String,
    @SerializedName("heading")
    val heading: String,
    @SerializedName("description")
    val description: String,
)

data class ConsumedCarbTotal(
    @SerializedName("carbs_consumed")
    val carbsConsumed: Double,

    @SerializedName("date")
    val date: String
)
