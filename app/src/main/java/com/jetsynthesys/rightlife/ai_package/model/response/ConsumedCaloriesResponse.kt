package com.jetsynthesys.rightlife.ai_package.model.response

import com.google.gson.annotations.SerializedName

data class ConsumedCaloriesResponse(
    @SerializedName("status_code")
    val statusCode: Int,

    val message: String,

    @SerializedName("start_date")
    val startDate: String,

    @SerializedName("end_date")
    val endDate: String,

    @SerializedName("consumed_calorie_totals")
    val consumedCalorieTotals: List<ConsumedCalorieTotal>,

    @SerializedName("total_calories")
    val totalCalories: Double,

    @SerializedName("current_avg_calories")
    val currentAvgCalories: Double,

    @SerializedName("progress_percentage")
    val progressPercentage: Double,

    @SerializedName("progress_sign")
    val progressSign: String
)

data class ConsumedCalorieTotal(
    @SerializedName("calories_consumed")
    val caloriesConsumed: Double,

    val date: String
)
