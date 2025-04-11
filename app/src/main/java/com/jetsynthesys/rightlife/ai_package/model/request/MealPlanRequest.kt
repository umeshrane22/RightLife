package com.jetsynthesys.rightlife.ai_package.model.request

data class MealPlanRequest(
    val meal_plan_name: String,
    val dish_ids: List<String>,
    val date: String
)
