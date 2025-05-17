package com.jetsynthesys.rightlife.ai_package.model

import kotlinx.serialization.Serializable

@kotlinx.serialization.Serializable
data class StoreHealthDataRequest(
    val user_id: String,
    val source: String,
    val active_energy_burned: List<EnergyBurnedRequest>?,
    val basal_energy_burned: List<EnergyBurnedRequest>?,
    val distance_walking_running: List<Distance>?,
    val step_count: List<StepCountRequest>?,
    val heart_rate: List<HeartRateRequest>?,
    val heart_rate_variability_SDNN: List<HeartRateVariabilityRequest>?,
    val resting_heart_rate: List<HeartRateRequest>?,
    val respiratory_rate: List<RespiratoryRate>?,
    val oxygen_saturation: List<OxygenSaturation>?,
    val blood_pressure_systolic: List<BloodPressure>?,
    val blood_pressure_diastolic: List<BloodPressure>?,
    val body_mass: List<BodyMass>?,
    val body_fat_percentage: List<BodyFatPercentage>?,
    val sleep_stage: List<SleepStageJson>,
    val workout: List<WorkoutRequest>?
)

@kotlinx.serialization.Serializable
data class EnergyBurnedRequest(
    val start_datetime: String,
    val end_datetime: String,
    val record_type: String,
    val unit: String,
    val value: String,
    val source_name: String
)

@kotlinx.serialization.Serializable
data class Distance(
    val start_datetime: String,
    val end_datetime: String,
    val record_type: String,
    val unit: String,
    val value: String,
    val source_name: String
)

@kotlinx.serialization.Serializable
data class StepCountRequest(
    val start_datetime: String,
    val end_datetime: String,
    val record_type: String,
    val unit: String,
    val value: String,
    val source_name: String
)

@kotlinx.serialization.Serializable
data class HeartRateRequest(
    val start_datetime: String,
    val end_datetime: String,
    val record_type: String,
    val unit: String,
    val value: String,
    val source_name: String
)

@kotlinx.serialization.Serializable
data class HeartRateVariabilityRequest(
    val start_datetime: String,
    val end_datetime: String,
    val record_type: String,
    val unit: String,
    val value: String,
    val source_name: String
)

@kotlinx.serialization.Serializable
data class RespiratoryRate(
    val start_datetime: String,
    val end_datetime: String,
    val record_type: String,
    val unit: String,
    val value: String,
    val source_name: String
)

@kotlinx.serialization.Serializable
data class OxygenSaturation(
    val start_datetime: String,
    val end_datetime: String,
    val record_type: String,
    val unit: String,
    val value: String,
    val source_name: String
)

@kotlinx.serialization.Serializable
data class BloodPressure(
    val start_datetime: String,
    val end_datetime: String,
    val record_type: String,
    val unit: String,
    val value: String,
    val source_name: String
)

@kotlinx.serialization.Serializable
data class BodyMass(
    val start_datetime: String,
    val end_datetime: String,
    val record_type: String,
    val unit: String,
    val value: String,
    val source_name: String
)

@Serializable
data class BodyFatPercentage(
    val start_datetime: String,
    val end_datetime: String,
    val record_type: String,
    val unit: String,
    val value: String,
    val source_name: String
)

@kotlinx.serialization.Serializable
data class SleepStage(
    val start_datetime: String,
    val end_datetime: String,
    val record_type: String,
    val unit: String,
    val value: String,
    val source_name: String
)

@kotlinx.serialization.Serializable
data class WorkoutRequest(
    val start_datetime: String,
    val end_datetime: String,
    val source_name: String,
    val record_type: String,
    val workout_type: String,
    val duration: String,
    val calories_burned: String,
    val distance: String,
    val duration_unit: String,
    val calories_unit: String,
    val distance_unit: String
)