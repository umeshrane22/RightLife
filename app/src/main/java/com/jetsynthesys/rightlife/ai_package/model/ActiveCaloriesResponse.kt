package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class ActiveCaloriesResponse(
    @SerializedName("message") val message: String,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("end_date") val endDate: String,
    @SerializedName("active_calorie_totals") val activeCaloriesTotals: List<ActiveCalorieTotals>
)
data class ActiveCalorieTotals(
    @SerializedName("calories_burned") val caloriesBurned : String,
    @SerializedName("date") val date: String
)
