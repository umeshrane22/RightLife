package com.jetsynthesys.rightlife.ai_package.model.request

data class UpdateMealRequest(
    val meal_name: String?,
    val meal_log: List<DishLog>
)

