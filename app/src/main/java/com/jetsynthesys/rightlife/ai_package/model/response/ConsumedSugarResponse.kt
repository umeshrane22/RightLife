package com.jetsynthesys.rightlife.ai_package.model.response

import com.google.gson.annotations.SerializedName

data class ConsumedSugarResponse(
    @SerializedName("status_code")
    val statusCode: Int,

    @SerializedName("message")
    val message: String,

    @SerializedName("start_date")
    val startDate: String,

    @SerializedName("end_date")
    val endDate: String,

    @SerializedName("consumed_sugar_totals")
    val consumedSugarTotals: List<ConsumedSugarTotal>,

    @SerializedName("total_sugar")
    val totalSugar: Double,

    @SerializedName("current_avg_sugar")
    val currentAvgSugar: Double,

    @SerializedName("progress_percentage")
    val progressPercentage: Double,

    @SerializedName("heading")
    val heading: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("progress_sign")
    val progressSign: String,
)

data class ConsumedSugarTotal(
    @SerializedName("sugar_consumed")
    val sugarConsumed: Double,

    @SerializedName("date")
    val date: String
)
