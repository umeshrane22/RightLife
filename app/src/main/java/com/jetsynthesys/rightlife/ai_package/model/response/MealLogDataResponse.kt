package com.jetsynthesys.rightlife.ai_package.model.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MealLogDataResponse(
    val status_code: Int,
    val message: String,
    val data: MealLogSummary
):Parcelable

@Parcelize
data class MealLogSummary(
    val user_id: String,
    val date: String,
    val meal_detail: Map<String, MealDetailsLog>,
    val full_day_summary : FullDaySummary
):Parcelable

@Parcelize
data class MealDetailsLog(
    val regular_receipes: List<RegularRecipeEntry>,
    val snap_meals: List<SnapMeal>,
    val meal_nutrition_summary: List<MealNutritionSummary>
):Parcelable

@Parcelize
data class RegularRecipeEntry(
    val receipe: Recipe,
    val quantity: Int,
    val unit: String,
    val measure: String,
    val meal_id : String
):Parcelable

@Parcelize
data class Recipe(
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
):Parcelable

@Parcelize
data class SnapMeal(
    val _id : String,
    val meal_type : String?,
    val meal_name : String?,
    val date : String?,
    val dish : List<SnapDishList>?,
    val meal_nutrition_summary : SnapMealNutritionSummary
):Parcelable

@Parcelize
data class SnapDishList(
    val name: String,
    val b12_mcg: Double?,
    val b1_mg: Double?,
    val b2_mg: Double?,
    val b3_mg: Double?,
    val b6_mg: Double?,
    val calcium_mg: Double?,
    val calories_kcal: Double?,
    val carb_g: Double?,
    val cholesterol_mg: Double?,
    val copper_mg: Double?,
    val fat_g: Double?,
    val folate_mcg: Double?,
    val fiber_g: Double?,
    val iron_mg: Double?,
    val is_beverage: Double?,
    val magnesium_mg: Double?,
    val mass_g: Double?,
    val monounsaturated_g: Double?,
    val omega_3_fatty_acids_g: Double?,
    val omega_6_fatty_acids_g: Double?,
    val percent_fruit: Double?,
    val percent_legume_or_nuts: Double?,
    val percent_vegetable: Double?,
    val phosphorus_mg: Double?,
    val polyunsaturated_g: Double?,
    val potassium_mg: Double?,
    val protein_g: Double?,
    val saturated_fats_g: Double?,
    val selenium_mcg: Double?,
    val sodium_mg: Double?,
    val source_urls: List<String>?,
    val sugar_g: Double?,
    val vitamin_a_mcg: Double?,
    val vitamin_c_mg: Double?,
    val vitamin_d_iu: Double?,
    val vitamin_e_mg: Double?,
    val vitamin_k_mcg: Double?,
    val zinc_mg: Double?,
    val _id : String?
):Parcelable

@Parcelize
data class SnapMealNutritionSummary(
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
    val sugar_g: Double,
    val vitamin_a_mcg: Double,
    val vitamin_c_mg: Double,
    val vitamin_d_iu: Double,
    val vitamin_e_mg: Double,
    val vitamin_k_mcg: Double,
    val zinc_mg: Double
):Parcelable


@Parcelize
data class MealNutritionSummary(
    val time_in_seconds: Double,
    val servings: Double,
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
    val quantity: Double
):Parcelable

@Parcelize
data class FullDaySummary(
    val time_in_seconds: Double?,
    val servings: Double?,
    val serving_weight: Double?,
    val calories: Double?,
    val carbs: Double?,
    val sugar: Double?,
    val fiber: Double?,
    val protein: Double?,
    val fats: Double?,
    val saturated_fat: Double?,
    val trans_fat: Double?,
    val cholesterol: Double?,
    val sodium: Double?,
    val potassium: Double?,
    val quantity: Double?,
    val b12_mcg: Double?,
    val b1_mg: Double?,
    val b2_mg: Double?,
    val b3_mg: Double?,
    val b6_mg: Double?,
    val calcium_mg: Double?,
    val calories_kcal: Double?,
    val carb_g: Double?,
    val cholesterol_mg: Double?,
    val copper_mg: Double?,
    val fat_g: Double?,
    val folate_mcg: Double?,
    val fiber_g: Double?,
    val iron_mg: Double?,
    val is_beverage: Double?,
    val magnesium_mg: Double?,
    val mass_g: Double?,
    val monounsaturated_g: Double?,
    val omega_3_fatty_acids_g: Double?,
    val omega_6_fatty_acids_g: Double?,
    val percent_fruit: Double?,
    val percent_legume_or_nuts: Double?,
    val percent_vegetable: Double?,
    val phosphorus_mg: Double?,
    val polyunsaturated_g: Double?,
    val potassium_mg: Double?,
    val protein_g: Double?,
    val saturated_fats_g: Double?,
    val selenium_mcg: Double?,
    val sodium_mg: Double?,
    val sugar_g: Double?,
    val vitamin_a_mcg: Double?,
    val vitamin_c_mg: Double?,
    val vitamin_d_iu: Double?,
    val vitamin_e_mg: Double?,
    val vitamin_k_mcg: Double?,
    val zinc_mg: Double?
):Parcelable

