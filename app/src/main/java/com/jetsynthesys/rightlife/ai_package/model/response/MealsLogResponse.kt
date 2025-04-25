package com.jetsynthesys.rightlife.ai_package.model.response

data class MealsLogResponse(
    val status_code: Int,
    val message: String,
    val data: MealLogData
)

data class MealLogData(
    val meal_details: List<MealDetail>,
  //  val meal_nutrition_summary_per_day: MealNutritionSummary
)

data class MealDetail(
    val _id: String,
    val user_id: String,
    val meal_type: String,
    val meal_name: String,
    val receipe_data: List<ReceipeData>,
    val date: String,
    val isFavourite: Boolean,
    val isLogged: Boolean,
    val createdAt: String,
    val updatedAt: String
)

data class ReceipeData(
   // val receipe: Receipe,
    val quantity: Int,
    val unit: String,
    val measure: String
)

//data class Receipe(
//    val _id: String,
//    val `Recipe Name`: String,
//    val `Ingredients (per serving)`: List<String>,
//    val Instructions: List<String>,
//    val Author: String,
//    val `Total Time`: String,
//    val `Time in Seconds`: Int,
//    val Servings: Int,
//    val `Course (one or more)`: String,
//    val `Tags (optional)`: String?,
//    val `Cuisine (optional)`: String?,
//    val `Photo url`: String,
//    val `Serving Weight`: Double,
//    val Calories: Double,
//    val Carbs: Double,
//    val Sugar: Double,
//    val Fiber: Double,
//    val Protein: Double,
//    val Fat: Double,
//    val `Saturated Fat`: Double,
//    val `Trans Fat`: Double,
//    val Cholesterol: Double,
//    val Sodium: Double,
//    val Potassium: Double,
//    val RecipeID: String
//)
//
//data class MealNutritionSummary(
//    val Calories: Double,
//    val Carbs: Double,
//    val Sugar: Double,
//    val Fiber: Double,
//    val Protein: Double,
//    val Fat: Double,
//    val `Saturated Fat`: Double,
//    val `Trans Fat`: Double,
//    val Cholesterol: Double,
//    val Sodium: Double,
//    val Potassium: Double
//)
