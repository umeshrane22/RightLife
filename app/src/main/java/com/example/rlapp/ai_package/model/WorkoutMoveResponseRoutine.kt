package com.example.rlapp.ai_package.model

import com.google.gson.annotations.SerializedName

data class WorkoutMoveResponseRoutine(
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<WorkoutData>,
    @SerializedName("pagination") val pagination: Pagination
)
