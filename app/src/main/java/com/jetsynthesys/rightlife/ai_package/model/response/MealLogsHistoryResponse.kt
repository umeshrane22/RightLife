package com.jetsynthesys.rightlife.ai_package.model.response

import com.google.gson.annotations.SerializedName

data class MealLogsHistoryResponse(
    @SerializedName("status_code")
    val statusCode: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("start_date")
    val startDate: String,
    @SerializedName("end_date")
    val endDate: String,
    @SerializedName("user_goal")
    val userGoal: String,
    @SerializedName("is_logged_meal_list")
    val loggedMealList: List<LoggedMealHistory>
)

data class LoggedMealHistory(
    @SerializedName("date")
    val date: String,
    @SerializedName("data")
    val data: Any?,
    @SerializedName("is_available")
    val isAvailable: Boolean,
    @SerializedName("calories_data")
    val caloriesData: CaloriesData
)

data class CaloriesData(
    @SerializedName("calories_intake")
    val caloriesIntake: Double,
    @SerializedName("calories_outtake")
    val caloriesOuttake: Double,
    @SerializedName("difference")
    val difference: Double,
    @SerializedName("sign")
    val sign: String
)




