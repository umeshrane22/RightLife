package com.jetsynthesys.rightlife.ai_package.model.response

data class MealLogPlanResponse(
    val message: String,
    val meal_plans: List<MealPlan>
)

data class MealPlan(
    val _id: String,
    val user_id: String,
    val meal_plan_name: String,
    val dishes: Dish
)

data class Dish(
    val name: String,
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
    val meal_plan: String
)