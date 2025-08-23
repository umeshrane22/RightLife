package com.jetsynthesys.rightlife.ai_package.model.response

data class MyMealsSaveResponse(
    val status_code: Int,
    val message: String,
    val data: MealsData
)

data class MealsData(
    val snap_meal_detail: List<SnapMealDetail>,
    val meal_details: List<MealDetails>,
    val combined_totals: CombinedTotals
)

data class SnapMealDetail(
    val _id: String,
    val user_id: String,
    val meal_type: String?,
    val meal_name: String,
    val date: String?,
    val dish: List<SnapDish>,
    val is_save: Boolean?,
    val is_snapped: Boolean?,
    val createdAt: String,
    val updatedAt: String,
    val total_servings: Double,
    val total_calories: Double,
    val total_carbs: Double,
    val total_protein: Double,
    val total_fat: Double,
    val total_sugar: Double,
    val total_cholesterol: Double,
    val total_iron: Double,
    val total_magnesium: Double,
    val image_url : String
)

data class SnapDish(
    val name: String,
    val b12_mcg: Double,
    val b1_mg: Double,
    val b2_mg: Double,
    val b3_mg: Double,
    val b6_mg: Double,
    val calcium_mg: Double,
    val calories_kcal: Double,
    val carb_g: Double,
    val cholesterol_mg: Double,
    val copper_mg: Double,
    val fat_g: Double,
    val folate_mcg: Double,
    val fiber_g: Double,
    val iron_mg: Double,
    val is_beverage: Double,
    val magnesium_mg: Double,
    val mass_g: Double,
    val monounsaturated_g: Double,
    val omega_3_fatty_acids_g: Double,
    val omega_6_fatty_acids_g: Double,
    val percent_fruit: Double,
    val percent_legume_or_nuts: Double,
    val percent_vegetable: Double,
    val phosphorus_mg: Double,
    val polyunsaturated_g: Double,
    val potassium_mg: Double,
    val protein_g: Double,
    val saturated_fats_g: Double,
    val selenium_mcg: Double,
    val sodium_mg: Double,
    val source_urls: List<String>,
    val sugar_g: Double,
    val vitamin_a_mcg: Double,
    val vitamin_c_mg: Double,
    val vitamin_d_iu: Double,
    val vitamin_e_mg: Double,
    val vitamin_k_mcg: Double,
    val zinc_mg: Double
)

data class MealDetails(
    val _id: String,
    val user_id: String,
    val meal_type: String?,
    val meal_name: String,
    val createdAt: String,
    val isFavourite: Boolean,
    val receipe_data: List<RecipeData>,
    val total_servings: Double,
    val total_calories: Double,
    val total_carbs: Double,
    val total_protein: Double,
    val total_fat: Double,
    val total_sugar: Double,
    val total_cholesterol: Double,
    val total_iron: Double,
    val total_magnesium: Double
)

data class RecipeData(
    val receipe: Receipe,
    val quantity: Double,
    val unit: String,
    val measure: String,
    val name: String?,
    val calories_kcal: Double?,
    val carb_g: Double?,
    val protein_g: Double?,
    val fat_g: Double?,
    val sugar_g: Double?,
    val cholesterol_mg: Double?,
    val iron_mg: Double?,
    val magnesium_mg: Double?
)

data class Receipe(
    val _id: String,
    val recipe_name: String,
    val ingredients_per_serving: List<String>,
    val instructions: List<String>,
    val author: String,
    val total_time: String,
    val time_in_seconds: Int,
    val servings: Int,
    val course_one_or_more: String,
    val tags_optional: String,
    val cuisine_optional: String,
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

data class CombinedTotals(
    val total_servings: Double,
    val total_calories: Double,
    val total_carbs: Double,
    val total_protein: Double,
    val total_fat: Double,
    val total_sugar: Double,
    val total_cholesterol: Double,
    val total_iron: Double,
    val total_magnesium: Double
)
