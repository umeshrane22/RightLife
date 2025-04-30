package com.jetsynthesys.rightlife.ai_package.model.response

import com.google.gson.annotations.SerializedName

data class ConsumedCholesterolResponse(
    @SerializedName("status_code")
    val statusCode: Int,

    @SerializedName("message")
    val message: String,

    @SerializedName("start_date")
    val startDate: String,

    @SerializedName("end_date")
    val endDate: String,

    @SerializedName("consumed_cholesterol_totals")
    val consumedCholesterolTotals: List<ConsumedCholesterolTotal>,

    @SerializedName("total_cholesterol")
    val totalCholesterol: Double,

    @SerializedName("current_avg_cholesterol")
    val currentAvgCholesterol: Double,

    @SerializedName("progress_percentage")
    val progressPercentage: Double,

    @SerializedName("progress_sign")
    val progressSign: String,
    @SerializedName("heading")
    val heading: String,
    @SerializedName("description")
    val description: String,
)

data class ConsumedCholesterolTotal(
    @SerializedName("cholesterol_consumed")
    val cholesterolConsumed: Double,

    @SerializedName("date")
    val date: String
)
