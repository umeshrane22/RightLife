package com.jetsynthesys.rightlife.ai_package.model.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class SnapMealRecipeResponseModel(
    val success: Boolean,
    val message: String,
    val status_code: Int,
    val data: List<SnapRecipeList>
)
@Parcelize
data class SnapRecipeList(
    val id: String,
    val name: String,
    val photo_url: String,
    val servings: Int,
    val cooking_time_in_seconds: Double,
    val calories: Double,
    val meal_type: String,
    val food_type: String,
    val cuisine: String
):Parcelable