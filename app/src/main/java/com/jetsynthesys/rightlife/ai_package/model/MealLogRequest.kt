package com.jetsynthesys.rightlife.ai_package.model

data class MealLogRequest(
    val mealId: String,
    val userId: String,
    val meal: String,
    val date: String, // Consider using String if you're sending ISO 8601 format (e.g., "2025-04-08T12:00:00")
    val image: String,
    val mealType: String,
    val mealQuantity: Double,
    val unit: String,
    val isRepeat: Boolean,
    val isFavourite: Boolean,
    val isLogged: Boolean
)
