package com.jetsynthesys.rightlife.ai_package.model

data class WorkoutResponseRoutine(
    val message: String,
    val routines: Map<String, Routine>
)
