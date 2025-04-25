package com.jetsynthesys.rightlife.ai_package.model.request

data class SaveMealLogRequest(
    val meal_name: String,
    val meal_type: String,
    val meal_log: List<MealLogItem>
)

data class MealLogItem(
    val meal_id: String?,
    val meal_quantity: Int? = null,
    val unit: String? = null,
    val measure: String? = null
)
