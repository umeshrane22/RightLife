package com.jetsynthesys.rightlife.ai_package.model.response

import com.google.gson.annotations.SerializedName

data class ConsumedIronResponse(
    @SerializedName("status_code")
    val statusCode: Int,

    @SerializedName("message")
    val message: String,

    @SerializedName("start_date")
    val startDate: String,

    @SerializedName("end_date")
    val endDate: String,

    @SerializedName("consumed_iron_totals")
    val consumedIronTotals: List<ConsumedIronTotal>,

    @SerializedName("total_iron")
    val totalIron: Double,

    @SerializedName("current_avg_iron")
    val currentAvgIron: Double,

    @SerializedName("progress_percentage")
    val progressPercentage: Double,

    @SerializedName("progress_sign")
    val progressSign: String
)

data class ConsumedIronTotal(
    @SerializedName("iron_consumed")
    val ironConsumed: Double,

    @SerializedName("date")
    val date: String
)
