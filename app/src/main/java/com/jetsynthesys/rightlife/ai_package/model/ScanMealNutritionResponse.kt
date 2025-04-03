package com.jetsynthesys.rightlife.ai_package.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ScanMealNutritionResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("data")
    val data: List<NutritionData>
): Parcelable

@Parcelize
data class NutritionData(
    @SerializedName("name")
    val name: String,
    @SerializedName("brand")
    val brand: String,
    @SerializedName("nutrition_per_100g")
    val nutrition_per_100g: NutritionDetails,
    @SerializedName("alt_units")
    val alt_units: List<AltUnit>,
    @SerializedName("default_portion")
    val default_portion: Portion,
    @SerializedName("selected_portion")
    val selected_portion: Portion,
    @SerializedName("selected_portion_nutrition")
    val selected_portion_nutrition: NutritionDetails
): Parcelable

@Parcelize
data class NutritionDetails(
    @SerializedName("mass_g")
    val mass_g: Double,
    @SerializedName("calories_kcal")
    val calories_kcal: Double,
    @SerializedName("carb_g")
    val carb_g: Double,
    @SerializedName("fat_g")
    val fat_g: Double,
    @SerializedName("protein_g")
    val protein_g: Double,
    @SerializedName("b12_mcg")
    val b12_mcg : Double?,
    @SerializedName("vitamin_d_iu")
    val vitamin_d_iu: Double?,
    @SerializedName("calcium_mg")
    val calcium_mg: Double?,
    @SerializedName("iron_mg")
    val iron_mg: Double?,
    @SerializedName("magnesium_mg")
    val magnesium_mg : Double?,
    @SerializedName("phosphorus_mg")
    val phosphorus_mg : Double?,
    @SerializedName("potassium_mg")
    val potassium_mg: Double?,
    @SerializedName("sodium_mg")
    val sodium_mg: Double?,
    @SerializedName("fiber_g")
    val fiber_g: Double?,
    @SerializedName("sugar_g")
    val sugar_g: Double?,
    @SerializedName("saturated_fats_g")
    val saturated_fats_g: Double?,
    @SerializedName("trans_fats_g")
    val trans_fats_g: Double?,
    @SerializedName("cholesterol_mg")
    val cholesterol_mg: Double?,
    @SerializedName("percent_fruit")
    val percent_fruit: Double?,
    @SerializedName("percent_vegetable")
    val percent_vegetable: Double?,
    @SerializedName("percent_legume_or_nuts")
    val percent_legume_or_nuts: Double?,
    @SerializedName("is_beverage")
    val is_beverage: Boolean?,
    @SerializedName("zinc_mg")
    val zinc_mg : Double?,
    @SerializedName("source_urls")
    val source_urls: List<String>?
): Parcelable

@Parcelize
data class AltUnit(
    @SerializedName("unit")
    val unit: String,
    @SerializedName("weight_in_grams")
    val weight_in_grams: Double
): Parcelable

@Parcelize
data class Portion(
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("unit")
    val unit: String
): Parcelable

