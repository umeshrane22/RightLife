package com.jetsynthesys.rightlife.ai_package.ui.eatright.model

data class WeeklyCalorieData(
    val weekStart: String,
    val weekEnd: String,
    val totalCalories: Int,
    val averageCalories: Int,
    val goalCalories: Int,
    val dailyData: List<DailyCalorieData>
)

data class DailyCalorieData(
    val date: String,
    val day: String,
    val calories: Int
)
