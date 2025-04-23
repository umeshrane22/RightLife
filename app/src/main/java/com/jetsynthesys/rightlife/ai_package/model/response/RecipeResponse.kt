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
    @SerializedName("trans_fat")
    val trans_fat: Double?,
    @SerializedName("cholesterol")
    val cholesterol: Double?,
    @SerializedName("sodium")
    val sodium: Double?,
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
    @SerializedName("vitaminD")
    val vitaminD: Double?,
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
    @SerializedName("zinc")
    val zinc: Double?,
    @SerializedName("omega3")
    val omega3: Double?,
    @SerializedName("phosphorus")
    val phosphorus: Double?,
): Parcelable

