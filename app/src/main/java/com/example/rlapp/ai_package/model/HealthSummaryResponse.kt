package com.example.rlapp.ai_package.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName

@Parcelize
data class HealthSummaryResponse(
    @SerialName("message") val message: String?,
    @SerialName("heart_rate") val heartRateMoveLanding: List<HeartRateMoveLanding>?,
    @SerialName("resting_heart_rate") val restingHeartRate: List<RestingHeartRate>?,
    @SerialName("heart_rate_variability_SDNN") val heartRateVariabilitySDNN: List<HeartRateVariabilitySDNN>?,
    @SerialName("calories") val calories: List<Calorie>?,
    @SerialName("heart_rate_zones") val heartRateZones: HeartRateZonesMoveLanding?,
    @SerialName("steps") val steps: List<StepCount>?,
    @SerialName("total_steps_sum") val totalStepsSum: Int,
    @SerialName("avg_steps") val avgSteps: List<AvgSteps>?,
    @SerialName("steps_goal") val stepsGoal: String?,
    @SerialName("total_burned") val totalBurned: List<ActiveEnergyBurned>?,
    @SerialName("total_burned_sum") val totalBurnedSum: Int,
    @SerialName("total_intake_calories") val totalIntakeCalories: List<IntakeCalorie>?,
    @SerialName("total_intake_calories_sum") val totalIntakeCaloriesSum: Int
):Parcelable
