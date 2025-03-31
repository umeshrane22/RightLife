package com.example.rlapp.ai_package.model

data class WorkoutMoveMainResponseRoutine(
    val message: String,
    val period: String,
    val start_date: String,
    val end_date: String,
    val active_energy_burned: List<EnergyBurned>,
    val basal_energy_burned: List<EnergyBurned>
)

