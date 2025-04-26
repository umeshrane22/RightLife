package com.jetsynthesys.rightlife.ai_package.model.response

data class EatRightLandingPageDataResponse(
    val message: String,
    val total_calories: Double,
    val total_protein: Double,
    val total_fat: Double,
    val total_carbs: Double,
    val max_calories: Int,
    val max_protein: Int,
    val max_carbs: Int,
    val max_fat: Int,
    val total_water_ml: Double,
    val last_weight_log: LastWeightLog?,
    val other_recipes_you_might_like: List<OtherRecipe>,
    val insight: String,
    val macros_message: String
)

data class LastWeightLog(
    val weight: Double,
    val type: String,
    val date: String
)

data class OtherRecipe(
    val _id: String,
    val meal_name: String,
    val calories: Double,
    val protein: Double,
    val fats: Double,
    val carbs: Double,
    val image: String
)
