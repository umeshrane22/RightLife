package com.example.rlapp.ai_package.model

import com.google.gson.annotations.SerializedName

data class Routine(
    @SerializedName("total_time") val totalTime: Int,
    @SerializedName("total_calories") val totalCalories: Int,
    val workouts: List<Workout>
)
