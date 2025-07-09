package com.jetsynthesys.rightlife.ai_package.model.response

import com.google.gson.annotations.SerializedName

data class ConsumedVitaminDResponse(
    @SerializedName("status_code")
    val statusCode: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("start_date")
    val startDate: String,
    @SerializedName("end_date")
    val endDate: String,
    val consumed_vitamin_d_totals: List<ConsumedVitaminD>,
    val total_vitamin_d: Double,
    val current_avg_vitamin_d: Double,
    val unit: String,
    @SerializedName("progress_percentage")
    val progressPercentage: Double,
    @SerializedName("progress_sign")
    val progressSign: String,
    @SerializedName("heading")
    val heading: String,
    @SerializedName("description")
    val description: String,
    val previous_avg_vitamin_d: Double
)

data class ConsumedVitaminD(
    val vitamin_d_consumed: Double,
    val date: String
)
