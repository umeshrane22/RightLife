package com.jetsynthesys.rightlife.ai_package.ui.eatright.model

data class RecipeResponseNew(
    val status_code: Int,
    val data: RecipeData
)

data class RecipeData(
    val id: String,
    val recipe_name: String,
    val ingredients: List<String>,
    val instructions: List<String>,
    val author: String,
    val total_time: String,
    val servings: Int,
    val course: String,
    val tags: String,
    val cuisine: String,
    val photo_url: String,
    val serving_weight: Double,
    val calories: Double,
    val carbs: Double,
    val sugar: Double,
    val fiber: Double,
    val protein: Double,
    val fat: Double,
    val saturated_fat: Double,
    val trans_fat: Double,
    val cholesterol: Double,
    val sodium: Double,
    val potassium: Double,
    val recipe_id: String
)

