package com.jetsynthesys.rightlife.ai_package.model.response

import com.google.gson.annotations.SerializedName

data class ConsumedSodiumResponse(
    @SerializedName("status_code")
    val statusCode: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("start_date")
    val startDate: String,
    @SerializedName("end_date")
    val endDate: String,
    val consumed_sodium_totals: List<ConsumedSodium>,
    val total_sodium: Double,
    val current_avg_sodium: Double,
    val unit: String,
    @SerializedName("progress_percentage")
    val progressPercentage: Double,
    @SerializedName("progress_sign")
    val progressSign: String,
    @SerializedName("heading")
    val heading: String,
    @SerializedName("description")
    val description: String,
    val previous_avg_sodium: Double
)

data class ConsumedSodium(
    val sodium_consumed: Double,
    val date: String
)
