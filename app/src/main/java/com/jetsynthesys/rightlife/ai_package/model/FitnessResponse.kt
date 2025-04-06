package com.jetsynthesys.rightlife.ai_package.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class FitnessResponse(
    val message: String,

    @SerializedName("heart_rate")
    val heartRate: List<Int>,

    @SerializedName("resting_heart_rate")
    val restingHeartRate: List<Int>,

    @SerializedName("heart_rate_variability_SDNN")
    val heartRateVariabilitySDNN: List<Int>,

    val calories: List<Int>,

    @SerializedName("heart_rate_zones")
    val heartRateZones: HeartRateZonesMove,

    val steps: List<StepData>,

    @SerializedName("total_steps_sum")
    val totalStepsSum: Int,

    @SerializedName("avg_steps")
    val avgSteps: List<AvgStepsData>,

    @SerializedName("steps_goal")
    val stepsGoal: String,

    @SerializedName("total_burned")
    val totalBurned: List<BurnedCaloriesData>,

    @SerializedName("total_burned_sum")
    val totalBurnedSum: Int,

    @SerializedName("total_intake_calories")
    val totalIntakeCalories: List<IntakeCaloriesData>,

    @SerializedName("total_intake_calories_sum")
    val totalIntakeCaloriesSum: Int
) : Parcelable

// Data class for heart_rate_zones
@Parcelize
data class HeartRateZonesMove(
    @SerializedName("Light Zone")
    val lightZone: List<Int>,

    @SerializedName("Fat Burn Zone")
    val fatBurnZone: List<Int>,

    @SerializedName("Cardio Zone")
    val cardioZone: List<Int>,

    @SerializedName("Peak Zone")
    val peakZone: List<Int>
) : Parcelable

// Data class for steps array
@Parcelize
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

    val unit: String,
    val value: String,

    @SerializedName("_id")
    val id: String,

    @SerializedName("user_id")
    val userId: String
) : Parcelable

// Data class for avg_steps array
@Parcelize
data class AvgStepsData(
    val date: String,
    val avgSteps: Int
) : Parcelable

// Data class for total_burned array
@Parcelize
data class BurnedCaloriesData(
    val creationDatetime: String,
    val endDatetime: String,
    val sourceVersion: String,
    val startDatetime: String,
    val recordType: String,
    val unit: String,
    val value: Int, // Changed from String to Int
    val id: String, // JSON field `_id`
    val userId: String
) : Parcelable

// Data class for total_intake_calories array
@Parcelize
data class IntakeCaloriesData(
    val id: String, // JSON field `_id`
    val userId: String,
    val calories: Int
) : Parcelable
