package com.jetsynthesys.rightlife.ai_package.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecipeResponse(
    @SerializedName("status_code")
    val status_code: Int,
    @SerializedName("data")
    val data: SnapRecipeData
): Parcelable

@Parcelize
data class SnapRecipeData(
    @SerializedName("id")
    val id: String?,
    @SerializedName("recipe_name")
    val recipe_name: String?,
    @SerializedName("ingredients")
    val ingredients: List<String>?,
    @SerializedName("instructions")
    val instructions: List<String>?,
    @SerializedName("author")
    val author: String?,
    @SerializedName("total_time")
    val total_time: String?,
    @SerializedName("servings")
    val servings: Int?,
    @SerializedName("course")
    val course: String?,
    @SerializedName("tags")
    val tags: String?,
    @SerializedName("cuisine")
    val cuisine: String?,
    @SerializedName("photo_url")
    val photo_url: String,
    @SerializedName("serving_weight")
    val serving_weight: Double,
    @SerializedName("calories")
    val calories: Double?,
    @SerializedName("carbs")
    val carbs: Double?,
    @SerializedName("sugar")
    val sugar: Double?,
    @SerializedName("fiber")
    val fiber: Double?,
    @SerializedName("protein")
    val protein: Double?,
    @SerializedName("fat")
    val fat: Double?,
    @SerializedName("saturated_fat")
    val saturated_fat: Double?,
    @SerializedName("selenium_mcg")
    val selenium_mcg: Double?,
    @SerializedName("trans_fat")
    val trans_fat: Double?,
    @SerializedName("cholesterol")
    val cholesterol: Double?,
    @SerializedName("copper_mg")
    val copper_mg: Double?,
    @SerializedName("sodium")
    val sodium: Double?,
    @SerializedName("source_urls")
    val source_urls: List<String>?, // nullable for Apple Juice
    @SerializedName("potassium")
    val potassium: Double?,
    @SerializedName("recipe_id")
    val recipe_id: String?,
    @SerializedName("mealType")
    val mealType: String?,
    @SerializedName("mealQuantity")
    val mealQuantity: Double?,
    @SerializedName("cookingTime")
    val cookingTime: String?,
    @SerializedName("calcium")
    val calcium: Double?,
    @SerializedName("iron")
    val iron: Double?,
    @SerializedName("is_beverage")
    val is_beverage: Boolean?,
    @SerializedName("vitaminD")
    val vitaminD: Double?,
    @SerializedName("vitamin_e_mg")
    val vitamin_e_mg: Double?,
    @SerializedName("unit")
    val unit: String?,
    @SerializedName("isConsumed")
    val isConsumed: Boolean?,
    @SerializedName("isAteSomethingElse")
    val isAteSomethingElse: Boolean?,
    @SerializedName("isSkipped")
    val isSkipped: Boolean?,
    @SerializedName("isSwapped")
    val isSwapped: Boolean?,
    @SerializedName("isRepeat")
    val isRepeat: Boolean?,
    @SerializedName("isFavourite")
    val isFavourite: Boolean?,
    @SerializedName("notes")
    val notes: List<String>?,
    @SerializedName("b12")
    val b12: Double?,
    @SerializedName("b1_mg")
    val b1_mg: Double?,
    @SerializedName("b2_mg")
    val b2_mg: Double?,
    @SerializedName("b3_mg")
    val b3_mg: Double?,
    @SerializedName("b6_mg")
    val b6_mg: Double?,
    @SerializedName("folate")
    val folate: Double?,
    @SerializedName("vitaminC")
    val vitaminC: Double?,
    @SerializedName("vitaminA")
    val vitaminA: Double?,
    @SerializedName("vitaminK")
    val vitaminK: Double?,
    @SerializedName("magnesium")
    val magnesium: Double?,
    @SerializedName("mass_g")
    val mass_g: Double?,
    @SerializedName("monounsaturated_g")
    val monounsaturated_g: Double?,
    @SerializedName("zinc")
    val zinc: Double?,
    @SerializedName("omega3")
    val omega3: Double?,
    @SerializedName("omega_6_fatty_acids_g")
    val omega_6_fatty_acids_g: Double?,
    @SerializedName("percent_fruit")
    val percent_fruit: Double?,
    @SerializedName("percent_legume_or_nuts")
    val percent_legume_or_nuts: Double?,
    @SerializedName("percent_vegetable")
    val percent_vegetable: Double?,
    @SerializedName("phosphorus")
    val phosphorus: Double?,
    @SerializedName("polyunsaturated_g")
    val polyunsaturated_g: Double?,
): Parcelable

