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
    @SerializedName("b1_mg")
    val b1_mg : Double?,
    @SerializedName("b2_mg")
    val b2_mg : Double?,
    @SerializedName("b3_mg")
    val b3_mg : Double?,
    @SerializedName("b5_mg")
    val b5_mg : Double?,
    @SerializedName("b6_mg")
    val b6_mg : Double?,
    @SerializedName("b12_mcg")
    val b12_mcg : Double?,
    @SerializedName("folate_mcg")
    val folate_mcg : Double?,
    @SerializedName("vitamin_a_mcg")
    val vitamin_a_mcg: Double?,
    @SerializedName("vitamin_c_mg")
    val vitamin_c_mg: Double?,
    @SerializedName("vitamin_d_iu")
    val vitamin_d_iu: Double?,
    @SerializedName("vitamin_e_mg")
    val vitamin_e_mg: Double?,
    @SerializedName("vitamin_k_mcg")
    val vitamin_k_mcg: Double?,
    @SerializedName("calcium_mg")
    val calcium_mg: Double?,
    @SerializedName("copper_mg")
    val copper_mg: Double?,
    @SerializedName("iron_mg")
    val iron_mg: Double?,
    @SerializedName("magnesium_mg")
    val magnesium_mg : Double?,
    @SerializedName("manganese_mg")
    val manganese_mg : Double?,
    @SerializedName("phosphorus_mg")
    val phosphorus_mg : Double?,
    @SerializedName("potassium_mg")
    val potassium_mg: Double?,
    @SerializedName("selenium_mcg")
    val selenium_mcg: Double?,
    @SerializedName("sodium_mg")
    val sodium_mg: Double?,
    @SerializedName("zinc_mg")
    val zinc_mg : Double?,
    @SerializedName("fiber_g")
    val fiber_g: Double?,
    @SerializedName("starch_g")
    val starch_g: Double?,
    @SerializedName("sugar_g")
    val sugar_g: Double?,
    @SerializedName("monounsaturated_g")
    val monounsaturated_g: Double?,
    @SerializedName("polyunsaturated_g")
    val polyunsaturated_g: Double?,
    @SerializedName("omega_3_fatty_acids_g")
    val omega_3_fatty_acids_g: Double?,
    @SerializedName("omega_6_fatty_acids_g")
    val omega_6_fatty_acids_g: Double?,
    @SerializedName("saturated_fats_g")
    val saturated_fats_g: Double?,
    @SerializedName("trans_fats_g")
    val trans_fats_g: Double?,
    @SerializedName("cholesterol_mg")
    val cholesterol_mg: Double?,
    @SerializedName("cystine_g")
    val cystine_g: Double?,
    @SerializedName("histidine_g")
    val histidine_g: Double?,
    @SerializedName("isoleucine_g")
    val isoleucine_g: Double?,
    @SerializedName("leucine_g")
    val leucine_g: Double?,
    @SerializedName("lysine_g")
    val lysine_g: Double?,
    @SerializedName("methionine_g")
    val methionine_g: Double?,
    @SerializedName("phenylalanine_g")
    val phenylalanine_g: Double?,
    @SerializedName("threonine_g")
    val threonine_g: Double?,
    @SerializedName("tryptophan_g")
    val tryptophan_g: Double?,
    @SerializedName("tyrosine_g")
    val tyrosine_g: Double?,
    @SerializedName("valine_g")
    val valine_g: Double?,
    @SerializedName("percent_fruit")
    val percent_fruit: Double?,
    @SerializedName("percent_vegetable")
    val percent_vegetable: Double?,
    @SerializedName("percent_legume_or_nuts")
    val percent_legume_or_nuts: Double?,
    @SerializedName("is_beverage")
    val is_beverage: Boolean?,
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
    val quantity: Double?,
    @SerializedName("unit")
    val unit: String
): Parcelable

