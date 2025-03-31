package com.example.rlapp.ai_package.model

data class WorkoutResponseRoutine(
    val message: String,
    val routines: Map<String, Routine>
)
