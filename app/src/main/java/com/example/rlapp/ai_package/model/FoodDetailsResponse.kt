package com.example.rlapp.ai_package.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class FoodDetailsResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("statusCode")
    val statusCode: Int,
    @SerializedName("data")
    val data: MealDetails
): Parcelable

@Parcelize
data class MealDetails(
    @SerializedName("name")
    val name: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("mealType")
    val mealType: String,
    @SerializedName("mealQuantity")
    val mealQuantity: Double,
    @SerializedName("cookingTime")
    val cookingTime: String,
    @SerializedName("numOfServings")
    val numOfServings: Int,
    @SerializedName("calories")
    val calories: Double,
    @SerializedName("protein")
    val protein: Double,
    @SerializedName("fats")
    val fats: Double,
    @SerializedName("fiber")
    val fiber: Double,
    @SerializedName("saturatedFat")
    val saturatedFat: Double,
    @SerializedName("carbs")
    val carbs: Double,
    @SerializedName("sugar")
    val sugar: Double,
    @SerializedName("calcium")
    val calcium: Double,
    @SerializedName("iron")
    val iron: Double,
    @SerializedName("potassium")
    val potassium: Double,
    @SerializedName("sodium")
    val sodium: Double,
    @SerializedName("vitaminD")
    val vitaminD: Double,
    @SerializedName("cholesterol")
    val cholesterol: Double,
    @SerializedName("transFat")
    val transFat: Double,
    @SerializedName("ingredients")
    val ingredients: List<Ingredients>,
    @SerializedName("instructions")
    val instructions: List<String>,
    @SerializedName("unit")
    val unit: String,
    @SerializedName("isConsumed")
    val isConsumed: Boolean,
    @SerializedName("isAteSomethingElse")
    val isAteSomethingElse: Boolean,
    @SerializedName("isSkipped")
    val isSkipped: Boolean,
    @SerializedName("isSwapped")
    val isSwapped: Boolean,
    @SerializedName("isRepeat")
    val isRepeat: Boolean,
    @SerializedName("isFavourite")
    val isFavourite: Boolean,
    @SerializedName("_id")
    val _id: String,
    @SerializedName("notes")
    val notes: List<String>
): Parcelable

@Parcelize
data class Ingredients(
    @SerializedName("name")
    val name: String,
    @SerializedName("isChecked")
    val isChecked: Boolean,
    @SerializedName("_id")
    val _id: String
): Parcelable
