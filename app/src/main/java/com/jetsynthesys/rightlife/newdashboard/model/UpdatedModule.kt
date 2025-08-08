package com.jetsynthesys.rightlife.newdashboard.model

import com.google.gson.annotations.SerializedName


import java.io.Serializable

data class UpdatedModule(
    @SerializedName("moduleId") val moduleId: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("calorieBalance") val calorieBalance: String?, // changed to String to match JSON
    @SerializedName("intake") val intake: String?,
    @SerializedName("burned") val burned: String?,
    @SerializedName("mindfulTime") val mindfulTime: String?,
    @SerializedName("calories") val calories: String?,
    @SerializedName("sleepDuration") val sleepDuration: Double?,
    @SerializedName("isSelectedModule") val isSelectedModule: Boolean?,
    @SerializedName("mindfulnessMinutes") val mindfulnessMinutes: String?,
    @SerializedName("wellnessDays") val wellnessDays: String?,
    @SerializedName("activeBurn") val activeBurn: String?,
    @SerializedName("protein") val protein: String?,
    @SerializedName("carbs") val carbs: String?,
    @SerializedName("fats") val fats: String?,
    @SerializedName("sleepTime") val sleepTime: String?,
    @SerializedName("wakeUpTime") val wakeUpTime: String?,
    @SerializedName("totalSleepDurationMinutes") val totalSleepDurationMinutes: Double?,
    @SerializedName("rem") val rem: Double?, // numbers in JSON
    @SerializedName("core") val core: Double?,
    @SerializedName("deep") val deep: Double?,
    @SerializedName("awake") val awake: Double?,
    @SerializedName("asleep") val asleep: Double?,
    @SerializedName("goal") val goal: String?,
    @SerializedName("calorieRange") val calorieRange: List<Double>?,
    @SerializedName("sleepStages") val sleepStages: List<SleepStage>?,
    @SerializedName("sleepPerformanceDetail") val sleepPerformanceDetail: SleepPerformanceDetail?,
    @SerializedName("idealSleepRequirementData") val idealSleepRequirementData: IdealSleepRequirementData?
) : Serializable

data class SleepStage(
    @SerializedName("stage") val stage: String?,
    @SerializedName("start_datetime") val startDatetime: String?,
    @SerializedName("end_datetime") val endDatetime: String?,
    @SerializedName("duration_minutes") val durationMinutes: Double?
) : Serializable

data class SleepPerformanceDetail(
    @SerializedName("ideal_sleep_duration") val idealSleepDuration: Double?,
    @SerializedName("actual_sleep_data") val actualSleepData: ActualSleepData?,
    @SerializedName("sleep_performance_data") val sleepPerformanceData: SleepPerformanceData?
) : Serializable

data class ActualSleepData(
    @SerializedName("actual_sleep_duration_hours") val actualSleepDurationHours: Double?,
    @SerializedName("sleep_start_time") val sleepStartTime: String?,
    @SerializedName("sleep_end_time") val sleepEndTime: String?
) : Serializable

data class SleepPerformanceData(
    @SerializedName("sleep_performance") val sleepPerformance: Double?,
    @SerializedName("message") val message: String?,
    @SerializedName("action_step") val actionStep: String?
) : Serializable


data class IdealSleepRequirementData(
    @SerializedName("_id") val id: String?,
    @SerializedName("sleep_type") val sleepType: String?,
    @SerializedName("current_requirement") val currentRequirement: Double?,
    @SerializedName("sleep_datetime") val sleepDatetime: String?,
    @SerializedName("wakeup_datetime") val wakeupDatetime: String?,
    @SerializedName("is_default") val isDefault: Boolean?,
    @SerializedName("date") val date: String?
) : Serializable

/*data class UpdatedModule(
    @SerializedName("moduleId") val moduleId: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("calorieBalance") val calorieBalance: Int?,
    @SerializedName("intake") val intake: Int?,
    @SerializedName("burned") val burned: Int?,
    @SerializedName("mindfulTime") val mindfulTime: String?,
    @SerializedName("calories") val calories: String?,
    @SerializedName("sleepDuration") val sleepDuration: String?,
    @SerializedName("isSelectedModule") val isSelectedModule: Boolean?,
    @SerializedName("mindfulnessMinutes") val mindfulnessMinutes: String?,
    @SerializedName("wellnessDays") val wellnessDays: String?,
    @SerializedName("activeBurn") val activeBurn: String?,
    @SerializedName("protein") val protein: String?,
    @SerializedName("carbs") val carbs: String?,
    @SerializedName("fats") val fats: String?,
    @SerializedName("sleepTime") val sleepTime: String?,
    @SerializedName("wakeUpTime") val wakeUpTime: String?,
    @SerializedName("rem") val rem: String?,
    @SerializedName("core") val core: String?,
    @SerializedName("deep") val deep: String?,
    @SerializedName("awake") val awake: String?
)*/

