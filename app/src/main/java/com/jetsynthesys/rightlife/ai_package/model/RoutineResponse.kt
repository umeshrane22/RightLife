package com.jetsynthesys.rightlife.ai_package.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoutineResponse(
    @SerialName("routines")
    val routines: List<Routine>
) {
    @Serializable
    data class Routine(
        @SerialName("routine_id")
        val routine_id: String,
        @SerialName("user_id")
        val user_id: String,
        @SerialName("routine_name")
        val routine_name: String,
        val workouts: List<Workout>,
        @SerialName("createdAt")
        val createdAt: String,
        @SerialName("updatedAt")
        val updatedAt: String?
    )

    @Serializable
    data class Workout(
        @SerialName("activityId")
        val activityId: String,
        @SerialName("activity_name")
        val activity_name: String,
        val intensity: String,
        @SerialName("duration_min")
        val duration_min: Double,
        @SerialName("calories_burned")
        val calories_burned: Double
    )
}