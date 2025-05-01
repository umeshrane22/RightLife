package com.jetsynthesys.rightlife.ai_package.model.response

data class MealLogsHistoryResponse(
    val status_code: Int,
    val message: String,
    val start_date: String,
    val end_date: String,
    val is_logged_meal_list: List<LoggedMeal>?,
    val total_calories_data: TotalCaloriesData
)

data class LoggedMeal(
    val date: String,
    val data: Any?, // can be an object or list or empty, based on your structure
    val is_available: Boolean,
    val calories_data: CaloriesData
)

data class CaloriesData(
    val workout: Double,
    val manual: Double,
    val total_calories_burned: Double
)

data class TotalCaloriesData(
    val total_workout_burned: Double,
    val total_manual_burned: Double,
    val total_all_burned: Double
)
