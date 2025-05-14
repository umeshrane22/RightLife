package com.jetsynthesys.rightlife.ai_package.model.request

data class SaveDishLogRequest(
    val meal_type: String?,
    val meal_log: List<DishLog>
)

data class DishLog(
    val receipe_id: String?,
    val meal_quantity: Double?,
    val unit: String?,
    val measure: String?
)

