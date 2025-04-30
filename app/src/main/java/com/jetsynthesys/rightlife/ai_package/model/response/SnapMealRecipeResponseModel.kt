package com.jetsynthesys.rightlife.ai_package.model.response

data class SnapMealRecipeResponseModel(
    val success: Boolean,
    val message: String,
    val status_code: Int,
    val data: List<SnapRecipeList>
)

data class SnapRecipeList(
    val id: String,
    val name: String,
    val photo_url: String,
    val servings: Int,
    val cooking_time_in_seconds: Double,
    val calories: Double,
    val meal_type: String,
    val food_type: String,
    val cuisine: String
)