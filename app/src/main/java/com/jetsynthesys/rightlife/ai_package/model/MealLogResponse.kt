package com.jetsynthesys.rightlife.ai_package.model

data class MealLogResponse(
    val message: String,
    val meal_data: MealData
)

data class MealData(
    val _id: String,
    val mealId: String,
    val userId: String,
    val meal: String,
    val date: String,
    val image: String,
    val mealType: String,
    val mealQuantity: Double,
    val unit: String,
    val isRepeat: Boolean,
    val isFavourite: Boolean,
    val isLogged: Boolean
)

