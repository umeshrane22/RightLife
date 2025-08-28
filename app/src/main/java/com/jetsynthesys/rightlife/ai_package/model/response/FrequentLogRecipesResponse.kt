package com.jetsynthesys.rightlife.ai_package.model.response

data class FrequentRecipesResponse(
    val status_code: Int,
    val message: String,
    val data: FrequentRecipesData
)

data class FrequentRecipesData(
    val user_id: String,
    val frequent_recipes: List<FrequentRecipe>
)

data class FrequentRecipe(
    val count: Int,
    val _id: String,
    val recipe_name: String,
    val ingredients_per_serving: List<String>,
    val instructions: List<String>,
    val author: String,
    val total_time: String,
    val time_in_seconds: Int,
    val servings: Int,
    val course_one_or_more: String?,
    val tags_optional: String?,
    val cuisine_optional: String?,
    val photo_url: String?,
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
    val recipe_id: String,
    var isFrequentLog : Boolean = false
)
