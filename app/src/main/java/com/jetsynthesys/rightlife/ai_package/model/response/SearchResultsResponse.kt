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
    val Calories: Double? = 0.0,
    val Carbs: Double? = 0.0,
    val Fats: Double? = 0.0,
    val Protein: Double? = 0.0
):Parcelable

@Parcelize
data class Micros(
    val Cholesterol: Double? = 0.0,
    val Vitamin_A: Double? = 0.0,
    val Vitamin_C: Double? = 0.0,
    val Vitamin_K: Double? = 0.0,
    val Vitamin_D: Double? = 0.0,
    val Folate: Double? = 0.0,
    val Iron: Double? = 0.0,
    val Calcium: Double? = 0.0,
    val Magnesium: Double? = 0.0,
    val Potassium: Double? = 0.0,
    val Fiber: Double? = 0.0,
    val Zinc: Double? = 0.0,
    val Sodium: Double? = 0.0,
    val Sugar: Double? = 0.0,
    val b12_mcg : Double? = 0.0,
    val b1_mg : Double? = 0.0,
    val b2_mg : Double? = 0.0,
    val b3_mg : Double? = 0.0,
    val b5_mg : Double? = 0.0,
    val b6_mg : Double? = 0.0,
    val vitamin_e_mg: Double? = 0.0,
    val omega_3_fatty_acids_g : Double? = 0.0,
    val omega_6_fatty_acids_g: Double? = 0.0,
    val copper_mg: Double? = 0.0,
    val phosphorus_mg : Double? = 0.0,
    val saturated_fats_g: Double? = 0.0,
    val selenium_mcg: Double? = 0.0,
    val trans_fats_g: Double? = 0.0,
    val polyunsaturated_g: Double? = 0.0,
    val is_beverage: Boolean? = false,
    val mass_g: Double? = 0.0,
    val monounsaturated_g: Double? = 0.0,
    val percent_fruit: Double? = 0.0,
    val percent_vegetable: Double? = 0.0,
    val percent_legume_or_nuts: Double? = 0.0,
    val source_urls: List<String>?
):Parcelable
