package com.jetsynthesys.rightlife.ai_package.model.response

import com.google.gson.annotations.SerializedName

// Top-level response for step tracking data
data class StepTrackerResponse(
    @SerializedName("status_code")
    val statusCode: Int,

    @SerializedName("data")
    val data: List<StepTrackerData>
)

// Represents a single step tracking data entry
data class StepTrackerData(
    @SerializedName("period")
    val period: String,

    @SerializedName("start_date")
    val startDate: String,

    @SerializedName("end_date")
    val endDate: String,

    @SerializedName("is_step_goal_set")
    val isStepGoalSet: Boolean,

    @SerializedName("steps_goal")
    val stepsGoal: Int,

    @SerializedName("total_steps_count")
    val totalStepsCount: Double,

    @SerializedName("total_steps_avg")
    val totalStepsAvg: Double,

    @SerializedName("record_details")
    val recordDetails: List<StepTrackerRecord>,

    @SerializedName("comparison")
    val comparison: StepTrackerComparison,

    @SerializedName("heading")
    val heading: String,

    @SerializedName("description")
    val description: String
)

// Represents a daily step record within the tracking data
data class StepTrackerRecord(
    @SerializedName("date")
    val date: String,

    @SerializedName("total_steps_count_per_day")
    val totalStepsCountPerDay: Double
)

// Represents the comparison data with the previous period
data class StepTrackerComparison(
    @SerializedName("current_average_steps_per_day")
    val currentAverageStepsPerDay: Double,

    @SerializedName("current_period_label")
    val currentPeriodLabel: String,

    @SerializedName("previous_average_steps_per_day")
    val previousAverageStepsPerDay: Double,

    @SerializedName("previous_period_label")
    val previousPeriodLabel: String,

    @SerializedName("comparison_message")
    val comparisonMessage: String
)
