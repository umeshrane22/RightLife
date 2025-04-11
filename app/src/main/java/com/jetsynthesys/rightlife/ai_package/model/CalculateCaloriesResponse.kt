package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class CalculateCaloriesResponse(
    @SerializedName("message") val message: String,
    @SerializedName("calories_burned") val caloriesBurned: Double,
    @SerializedName("activity_factor") val activityFactor: Double
)
