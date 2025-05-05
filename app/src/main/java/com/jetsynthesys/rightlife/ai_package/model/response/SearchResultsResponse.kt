package com.jetsynthesys.rightlife.ai_package.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchResultsResponse(
    val status_code: Int,
    val message: String,
    val data: SearchData
):Parcelable

@Parcelize
data class SearchData(
    val total_found: Int,
    val ingredients_found: Int,
    val recipes_found: Int,
    val ingredient_priority_used: Boolean,
    val results: List<SearchResultItem>
):Parcelable

@Parcelize
data class SearchResultItem(
    val id: String,
    val name: String,
    val category: String,
    val photo_url: String,
    val servings: Int,
    val cooking_time_in_seconds: Int,
    val calories: Double?,
    val nutrients: Nutrients,
    val source: String,
    val unit: String?,
    val mealQuantity: Double?,
):Parcelable

@Parcelize
data class Nutrients(
    val macros: Macros,
    val micros: Micros
):Parcelable

@Parcelize
data class Macros(
    val Calories: Double?,
    val Carbs: Double?,
    val Fats: Double?,
    val Protein: Double?
):Parcelable

@Parcelize
data class Micros(
    val Cholesterol: Double?,
    val Vitamin_A: Double?,
    val Vitamin_C: Double?,
    val Vitamin_K: Double?,
    val Vitamin_D: Double?,
    val Folate: Double?,
    val Iron: Double?,
    val Calcium: Double?,
    val Magnesium: Double?,
    val Potassium: Double?,
    val Fiber: Double?,
    val Zinc: Double?,
    val Sodium: Double?,
    val Sugar: Double?,
    val b12_mcg : Double?,
    val b1_mg : Double?,
    val b2_mg : Double?,
    val b3_mg : Double?,
    val b5_mg : Double?,
    val b6_mg : Double?,
    val vitamin_e_mg: Double?,
    val omega_3_fatty_acids_g : Double?,
    val omega_6_fatty_acids_g: Double?,
    val copper_mg: Double?,
    val phosphorus_mg : Double?,
    val saturated_fats_g: Double?,
    val selenium_mcg: Double?,
    val trans_fats_g: Double?,
    val polyunsaturated_g: Double?,
    val is_beverage: Boolean?,
    val mass_g: Double,
    val monounsaturated_g: Double?,
    val percent_fruit: Double?,
    val percent_vegetable: Double?,
    val percent_legume_or_nuts: Double?,
    val source_urls: List<String>?
):Parcelable
