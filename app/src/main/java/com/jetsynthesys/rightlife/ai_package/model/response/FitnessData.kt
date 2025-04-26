package com.jetsynthesys.rightlife.ai_package.model.response

import java.time.LocalDateTime

data class HeartRate(
    val creationDatetime: LocalDateTime?,
    val unit: String,
    val value: Double
)

data class HeartRateZones(
    val lightZone: List<Int>,
    val fatBurnZone: List<Int>,
    val cardioZone: List<Int>,
    val peakZone: List<Int>
)

data class AvgSteps(
    val time: String,
    val avgSteps: Int
)

data class FitnessData(
    val message: String,
    val heartRate: List<HeartRate>,
    val heartRateZones: HeartRateZones,
    val steps: List<Any>,
    val totalStepsSum: Int,
    val avgSteps: List<AvgSteps>,
    val avgStepSum: Int,
    val stepsGoal: String,
    val totalBurned: List<Any>,
    val totalBurnedSum: Int,
    val totalIntakeCalories: List<Any>,
    val totalIntakeCaloriesSum: Int
)
