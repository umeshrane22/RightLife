package com.jetsynthesys.rightlife.ai_package.model.response

import com.google.gson.annotations.SerializedName

data class UpdateCaloriesResponse(
    @SerializedName("message")
    val message: String,

    @SerializedName("calories_burned")
    val caloriesBurned: Double,

    @SerializedName("activity_factor")
    val activityFactor: Double
)