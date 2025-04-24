package com.jetsynthesys.rightlife.ai_package.model.request

data class MealSaveRequest(
    val meal_type: String?,
    val meal_name: String?,
    val meal_log: List<MealLog>
)

data class MealLog(
    val receipe_id: String?,
    val meal_quantity: Int?,
    val unit: String?,
    val measure: String?
)
