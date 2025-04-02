package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class WorkoutMoveResponseRoutine(
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<WorkoutData>,
    @SerializedName("pagination") val pagination: Pagination
)
