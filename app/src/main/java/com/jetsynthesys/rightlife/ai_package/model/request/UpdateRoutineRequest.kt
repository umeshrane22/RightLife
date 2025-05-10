package com.jetsynthesys.rightlife.ai_package.model.request

data class UpdateRoutineRequest(
    val user_id: String,
    val routine_id: String,
    val workouts: List<Workout>
)

data class Workout(
    val activityId: String,
    val intensity: String,
    val duration: Int,
    val calories_burned: Double
)
