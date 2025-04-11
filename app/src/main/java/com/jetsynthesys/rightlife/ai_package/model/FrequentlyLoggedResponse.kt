package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class FrequentlyLoggedResponse(
    @SerializedName("message")
    val message: String,

    @SerializedName("frequent_workouts")
    val frequentWorkouts: List<WorkoutItem>,

    @SerializedName("total_unique_workouts")
    val totalUniqueWorkouts: Int,

    @SerializedName("time_period")
    val timePeriod: String
)

// Data class for each workout item in frequent_workouts
data class WorkoutItem(
    @SerializedName("workout_type")
    val workoutType: String,

    @SerializedName("count")
    val count: Int
)
