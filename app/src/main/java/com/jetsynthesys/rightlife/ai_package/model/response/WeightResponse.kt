package com.jetsynthesys.rightlife.ai_package.model.response

import com.google.gson.annotations.SerializedName

data class WeightResponse(
    @SerializedName("status_code")
    val statusCode: Int,

    @SerializedName("message")
    val message: String,

    @SerializedName("start_date")
    val startDate: String,

    @SerializedName("end_date")
    val endDate: String,

    @SerializedName("weight_totals")
    val weightTotals: List<WeightTotal>,

    @SerializedName("current_avg_weight")
    val currentAvgWeight: Double,

    @SerializedName("progress_percentage")
    val progressPercentage: Double,

    @SerializedName("progress_sign")
    val progressSign: String,
    @SerializedName("heading")
    val heading: String,
    @SerializedName("description")
    val description: String,
)

data class WeightTotal(
    @SerializedName("weight")
    val weight: Double,

    @SerializedName("date")
    val date: String
)

