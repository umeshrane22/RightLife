package com.jetsynthesys.rightlife.ai_package.model.response

import com.google.gson.annotations.SerializedName

data class ConsumedCalciumResponse(
    @SerializedName("status_code")
    val statusCode: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("start_date")
    val startDate: String,
    @SerializedName("end_date")
    val endDate: String,
    val consumed_calcium_totals: List<ConsumedCalcium>,
    val total_calcium: Double,
    val current_avg_calcium: Double,
    val unit: String,
    @SerializedName("progress_percentage")
    val progressPercentage: Double,
    @SerializedName("progress_sign")
    val progressSign: String,
    @SerializedName("heading")
    val heading: String,
    @SerializedName("description")
    val description: String,
    val previous_avg_calcium: Double
)

data class ConsumedCalcium(
    val calcium_consumed: Double,
    val date: String
)
