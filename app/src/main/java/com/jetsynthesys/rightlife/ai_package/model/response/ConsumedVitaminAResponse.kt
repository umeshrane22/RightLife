package com.jetsynthesys.rightlife.ai_package.model.response

import com.google.gson.annotations.SerializedName

data class ConsumedVitaminAResponse(
    @SerializedName("status_code")
    val statusCode: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("start_date")
    val startDate: String,
    @SerializedName("end_date")
    val endDate: String,
    val consumed_vitamin_a_totals: List<ConsumedVitaminA>,
    val total_vitamin_a: Double,
    val current_avg_vitamin_a: Double,
    val unit: String,
    @SerializedName("progress_percentage")
    val progressPercentage: Double,
    @SerializedName("progress_sign")
    val progressSign: String,
    @SerializedName("heading")
    val heading: String,
    @SerializedName("description")
    val description: String,
    val previous_avg_vitamin_a: Double
)

data class ConsumedVitaminA(
    val vitamin_a_consumed: Double,
    val date: String
)
