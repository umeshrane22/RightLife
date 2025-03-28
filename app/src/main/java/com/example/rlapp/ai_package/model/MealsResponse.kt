package com.example.rlapp.ai_package.model

data class MealsResponse(
    val message: String,
    val meals: List<MealLists>
)

data class MealLists(
    val _id: String,
    val user_id: String,
    val meal: String,
    val date: String,  // Can be parsed to LocalDate if needed.
    val quantity: Double,
    val name: String,
    val image: String,
    val mealType: String,
    val mealQuantity: Double,
    val cookingTime: String,
    val numOfServings: Int,
    val calories: Int,
    val protein: Int,
    val fats: Int,
    val fiber: Int,
    val saturatedFat: Int,
    val carbs: Int,
    val sugar: Int,
    val calcium: Int,
    val iron: Int,
    val potassium: Int,
    val sodium: Int,
    val vitaminD: Int,
    val cholesterol: Int,
    val transFat: Int,
    val ingredients: List<String>,
    val instructions: List<String>,
    val unit: String,
    val isConsumed: Boolean,
    val isAteSomethingElse: Boolean,
    val isSkipped: Boolean,
    val isSwapped: Boolean,
    val isRepeat: Boolean,
    val isFavourite: Boolean,
    val notes: List<String>
)
