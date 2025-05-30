package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class ActiveCaloriesResponse(
    @SerializedName("status_code") val statusCode : Int,
    @SerializedName("message") val message: String,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("end_date") val endDate: String,
    @SerializedName("active_calorie_totals") val activeCaloriesTotals: List<ActiveCalorieTotals>,
    @SerializedName("current_avg_calories") val currentAvgCalories: Double,
    @SerializedName("progress_percentage") val progressPercentage : Double,
    @SerializedName("progress_sign") val progressSign: String,
    @SerializedName("heading") val heading: String,
    @SerializedName("description") val description: String
)
data class ActiveCalorieTotals(
    @SerializedName("calories_burned") val caloriesBurned : Double,
    @SerializedName("date") val date: String
)
