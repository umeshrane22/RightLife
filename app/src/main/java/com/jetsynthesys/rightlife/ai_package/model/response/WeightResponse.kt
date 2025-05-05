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

    @SerializedName("weight_totals_kg")
    val weightTotals: List<WeightTotal>,

    @SerializedName("total_weight")
    val totalWeight: Double,

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

    @SerializedName("last_weight_log")
    val lastWeightLog: LastWeightLogData
)

data class WeightTotal(
    @SerializedName("weight_kg")
    val weight: Double,

    @SerializedName("type")
    val type: String,

    @SerializedName("date")
    val date: String
)

data class LastWeightLogData(
    @SerializedName("total_weight")
    val totalWeight: Double,

    @SerializedName("date")
    val date: String,

    @SerializedName("goal")
    val goal: Double
)
