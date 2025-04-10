package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class FitnessResponse(
    val message: String,
    @SerializedName("heart_rate")
    val heartRate: List<HeartRateData>,
    @SerializedName("resting_heart_rate")
    val restingHeartRate: List<HeartRateData>,
    @SerializedName("heart_rate_variability_SDNN")
    val heartRateVariabilitySDNN: List<HeartRateVariabilityData>,
    @SerializedName("calories")
    val calories: List<CalorieData>,
    @SerializedName("heart_rate_zones")
    val heartRateZones: Map<String, List<Int>>,
    @SerializedName("steps")
    val steps: List<StepData>,
    @SerializedName("total_steps_sum")
    val totalStepsSum: Int,
    @SerializedName("avg_steps")
    val avgSteps: List<AvgStepData>,
    @SerializedName("avg_step_sum")
    val avgStepSum: Int,
    @SerializedName("steps_goal")
    val stepsGoal: String,
    @SerializedName("total_burned")
    val totalBurned: List<BurnedData>,
    @SerializedName("total_burned_sum")
    val totalBurnedSum: Double,
    @SerializedName("total_intake_calories")
    val totalIntakeCalories: List<IntakeCalorieData>,
    @SerializedName("total_intake_calories_sum")
    val totalIntakeCaloriesSum: Double
)

data class HeartRateData(
    @SerializedName("creation_datetime")
    val creationDatetime: String,
    @SerializedName("unit")
    val unit: String,
    @SerializedName("value")
    val value: Int
)

data class HeartRateVariabilityData(
    @SerializedName("creation_datetime")
    val creationDatetime: String,
    @SerializedName("unit")
    val unit: String,
    @SerializedName("value")
    val value: Int
)

data class CalorieData(
    @SerializedName("creation_datetime")
    val creationDatetime: String,
    @SerializedName("unit")
    val unit: String,
    @SerializedName("value")
    val value: Double
)

data class StepData(
    @SerializedName("creation_datetime")
    val creationDatetime: String,
    @SerializedName("end_datetime")
    val endDatetime: String,
    @SerializedName("source_version")
    val sourceVersion: String,
    @SerializedName("start_datetime")
    val startDatetime: String,
    @SerializedName("record_type")
    val recordType: String,
    @SerializedName("unit")
    val unit: String,
    @SerializedName("value")
    val value: String,
    @SerializedName("_id")
    val id: String,
    @SerializedName("user_id")
    val userId: String
)

data class AvgStepData(
    @SerializedName("time")
    val time: String,
    @SerializedName("avg_steps")
    val avgSteps: Int
)

data class BurnedData(
    @SerializedName("creation_datetime")
    val creationDatetime: String,
    @SerializedName("end_datetime")
    val endDatetime: String,
    @SerializedName("source_version")
    val sourceVersion: String,
    @SerializedName("start_datetime")
    val startDatetime: String,
    @SerializedName("record_type")
    val recordType: String,
    @SerializedName("unit")
    val unit: String,
    @SerializedName("value")
    val value: String, // Stored as String in JSON, convert to Double if needed
    @SerializedName("_id")
    val id: String,
    @SerializedName("user_id")
    val userId: String
)

data class IntakeCalorieData(
    @SerializedName("_id")
    val id: String,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("calories")
    val calories: Double
)
