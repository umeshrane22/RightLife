package com.jetsynthesys.rightlife.ai_package.model.response

import com.google.gson.annotations.SerializedName

data class ConsumedProteinResponse(
    @SerializedName("status_code") val statusCode: Int,
    @SerializedName("message") val message: String,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("end_date") val endDate: String,
    @SerializedName("consumed_protein_totals") val consumedProteinTotals: List<ProteinTotal>,
    @SerializedName("total_protein") val totalProtein: Double,
    @SerializedName("goal")
    val goal: Double,
    @SerializedName("current_avg_protein") val currentAvgProtein: Double,
    @SerializedName("progress_percentage") val progressPercentage: Double,
    @SerializedName("progress_sign") val progressSign: String,
    @SerializedName("heading")
    val heading: String,
    @SerializedName("description")
    val description: String,
)

data class ProteinTotal(
    @SerializedName("protein_consumed") val proteinConsumed: Double,
    @SerializedName("date") val date: String
)