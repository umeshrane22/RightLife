package com.jetsynthesys.rightlife.ai_package.ui.eatright.model

data class LandingPageResponse(
    val message: String,
    val total_calories: Double,
    val total_protein: Double,
    val total_fat: Double,
    val total_carbs: Double,
    val max_calories: Double,
    val max_protein: Double,
    val max_carbs: Double,
    val max_fat: Double,
    val meals: List<MealList>,
    val next_meal_suggestion: MealSuggestion,
    val other_recipes_you_might_like: List<RecipeSuggestion>
)

data class MealList(
    val _id: String,
    val name: String,
    val image: String,
    val mealType: String,
    val mealQuantity: Double,
    val cookingTime: String,
    val numOfServings: Int,
    val calories: Double,
    val protein: Double,
    val fats: Double,
    val fiber: Double,
    val saturatedFat: Double,
    val carbs: Double,
    val sugar: Double,
    val calcium: Double,
    val iron: Double,
    val potassium: Double,
    val sodium: Double,
    val vitaminD: Double,
    val cholesterol: Double,
    val transFat: Double,
    val ingredients: List<Ingredient>,
    val instructions: List<String>,
    val unit: String,
    val isConsumed: Boolean,
    val isAteSomethingElse: Boolean,
    val isSkipped: Boolean,
    val isSwapped: Boolean,
    val isRepeat: Boolean,
    val isFavourite: Boolean,
    val notes: List<String>,
    val meal_plan: String
)

data class Ingredient(
    val name: String,
    val isChecked: Boolean,
    val _id: String
)

data class MealSuggestion(
    val meal_name: String,
    val calories: Double,
    val protein: Double,
    val fats: Double,
    val carbs: Double,
    val image : String
)

data class RecipeSuggestion(
    val meal_name: String,
    val calories: Double,
    val protein: Double,
    val fats: Double,
    val carbs: Double,
    val image : String
)
