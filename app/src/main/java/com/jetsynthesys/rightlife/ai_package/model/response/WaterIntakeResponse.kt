package com.jetsynthesys.rightlife.ai_package.model.response

import com.google.gson.annotations.SerializedName

data class WaterIntakeResponse(
    @SerializedName("status_code")
    val statusCode: Int,

    @SerializedName("message")
    val message: String,

    @SerializedName("start_date")
    val startDate: String,

    @SerializedName("end_date")
    val endDate: String,

    @SerializedName("water_intake_totals")
    val waterIntakeTotals: List<WaterIntakeTotal>,

    @SerializedName("current_avg_water_ml")
    val currentAvgWaterMl: Double,

    @SerializedName("progress_percentage")
    val progressPercentage: Double,

    @SerializedName("progress_sign")
    val progressSign: String
)

data class WaterIntakeTotal(
    @SerializedName("water_ml")
    val waterMl: Double,

    @SerializedName("date")
    val date: String
)
