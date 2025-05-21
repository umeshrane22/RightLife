package com.jetsynthesys.rightlife.ai_package.model.response

import kotlinx.serialization.Serializable

@Serializable
data class CalorieAnalysisResponse(
    val status_code: Int,
    val message: String,
    val data: CalorieAnalysisData
)

@Serializable
data class CalorieAnalysisData(
    val user_id: String,
    val goal : String,
    val period: String,
    val start_date: String,
    val end_date: String,
    val maintenance_calories: Float,
    val tdee : Double,
    val calorie_data: List<CalorieData>,
    val messages: Messages,
    val heading: String,
    val description: String,
    val average_calorie_balance : Double,
    val percentage_progress : Double,
    val progress_sign : String,

)

@Serializable
data class CalorieData(
    val date: String,
    val calorie_balance: Float
)

@Serializable
data class Messages(
    val heading: String,
    val message : String
)