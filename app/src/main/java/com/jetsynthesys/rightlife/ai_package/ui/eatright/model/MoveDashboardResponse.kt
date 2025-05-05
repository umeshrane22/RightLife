package com.jetsynthesys.rightlife.ai_package.ui.eatright.model

import com.google.gson.annotations.SerializedName

data class CalorieBalance(
    @SerializedName("calorie_intake") val calorieIntake: Float,
    @SerializedName("calorie_burn_target") val calorieBurnTarget: Float,
    @SerializedName("goal") val goal: String,
    @SerializedName("difference") val difference: Float,
    @SerializedName("message") val message: String
)

data class HeartRateZone(
    @SerializedName("Light Zone") val lightZone: List<Int>,
    @SerializedName("Fat Burn Zone") val fatBurnZone: List<Int>,
    @SerializedName("Cardio Zone") val cardioZone: List<Int>,
    @SerializedName("Peak Zone") val peakZone: List<Int>
)

data class RestingHeartRateDay(
    @SerializedName("bpm") val bpm: Float,
    @SerializedName("date") val date: String
)

data class RestingHeartRate(
    @SerializedName("last_7_days") val last7Days: List<RestingHeartRateDay>,
    @SerializedName("today") val today: Float,
    @SerializedName("unit") val unit: String
)

data class AverageHeartRateDay(
    @SerializedName("heart_rate") val heartRate: Float,
    @SerializedName("date") val date: String
)

data class AverageHeartRate(
    @SerializedName("last_7_days") val last7Days: List<AverageHeartRateDay>,
    @SerializedName("today") val today: Float,
    @SerializedName("unit") val unit: String
)

data class HeartRateVariabilityDay(
    @SerializedName("hrv") val hrv: Float,
    @SerializedName("date") val date: String
)

data class HeartRateVariability(
    @SerializedName("last_7_days") val last7Days: List<HeartRateVariabilityDay>,
    @SerializedName("today") val today: Float,
    @SerializedName("unit") val unit: String
)

data class CaloriesBurnedDay(
    @SerializedName("calories_burned") val caloriesBurned: Float,
    @SerializedName("date") val date: String
)

data class CaloriesBurned(
    @SerializedName("last_7_days") val last7Days: List<CaloriesBurnedDay>,
    @SerializedName("today") val today: Float,
    @SerializedName("unit") val unit: String
)

data class CumulativeSteps(
    @SerializedName("hour") val hour: Int,
    @SerializedName("cumulative_steps") val cumulativeSteps: Int,
    @SerializedName("interval") val interval: String
)

data class Steps(
    @SerializedName("today_cumulative_steps") val todayCumulativeSteps: List<CumulativeSteps>,
    @SerializedName("average_cumulative_steps") val averageCumulativeSteps: List<CumulativeSteps>,
    @SerializedName("today_total") val todayTotal: Int,
    @SerializedName("average_steps") val averageSteps: Int,
    @SerializedName("goal_steps") val goalSteps: Int,
    @SerializedName("comparison_message") val comparisonMessage: String,
    @SerializedName("unit") val unit: String
)

data class DashboardData(
    @SerializedName("calorie_balance") val calorieBalance: CalorieBalance,
    @SerializedName("heart_rate_zones") val heartRateZones: HeartRateZone,
    @SerializedName("resting_heart_rate") val restingHeartRate: RestingHeartRate,
    @SerializedName("average_heart_rate") val averageHeartRate: AverageHeartRate,
    @SerializedName("heart_rate_variability") val heartRateVariability: HeartRateVariability,
    @SerializedName("calories_burned") val caloriesBurned: CaloriesBurned,
    @SerializedName("steps") val steps: Steps
)

data class MoveDashboardResponse(
    @SerializedName("status_code") val statusCode: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: DashboardData
)
