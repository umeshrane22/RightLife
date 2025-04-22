package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class HeartRateFitDataResponse(
    @SerializedName("status_code") val statusCode: Int,
    @SerializedName("data") val data: List<HeartRateFitData>
)

data class HeartRateFitData(
    @SerializedName("period") val period: String,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("end_date") val endDate: String,
    @SerializedName("is_step_goal_set") val isStepGoalSet: Boolean,
    @SerializedName("steps_goal") val stepsGoal: Int,
    @SerializedName("total_steps_count") val totalStepsCount: Int,
    @SerializedName("total_steps_avg") val totalStepsAvg: Double,
    @SerializedName("record_details") val recordDetails: List<RecordDetail>
)

data class RecordDetail(
    @SerializedName("date") val date: String,
    @SerializedName("total_steps_count_per_day") val totalStepsCountPerDay: Int
)
