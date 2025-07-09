package com.jetsynthesys.rightlife.ai_package.model.response

import com.google.gson.annotations.SerializedName

data class ConsumedZincResponse(
    @SerializedName("status_code")
    val statusCode: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("start_date")
    val startDate: String,
    @SerializedName("end_date")
    val endDate: String,
    val consumed_zinc_totals: List<ConsumedZinc>,
    val total_zinc: Double,
    val current_avg_zinc: Double,
    val unit: String,
    @SerializedName("progress_percentage")
    val progressPercentage: Double,
    @SerializedName("progress_sign")
    val progressSign: String,
    @SerializedName("heading")
    val heading: String,
    @SerializedName("description")
    val description: String,
    val previous_avg_zinc: Double
)

data class ConsumedZinc(
    val zinc_consumed: Double,
    val date: String
)
