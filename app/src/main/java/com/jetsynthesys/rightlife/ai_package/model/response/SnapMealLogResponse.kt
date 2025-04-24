package com.jetsynthesys.rightlife.ai_package.model.response

data class SnapMealLogResponse(
    val status_code: Int,
    val message: String,
    val inserted_ids: InsertedIds
)

data class InsertedIds(
    val meal_log_id: String,
    val meal_list_id: String
)
