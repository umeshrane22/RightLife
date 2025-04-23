package com.jetsynthesys.rightlife.ai_package.ui.eatright.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.jetsynthesys.rightlife.ai_package.model.MealDetails
import com.jetsynthesys.rightlife.ai_package.model.response.SnapRecipeData
import kotlinx.parcelize.Parcelize

@Parcelize
data class SnapDishLocalListModel(
    @SerializedName("data")
    var data: ArrayList<SnapRecipeData>
): Parcelable

//@Parcelize
//data class MealLists(
//    val _id: String,
//    val name: String,
//    val image: String,
//    val mealType: String,
//    val mealQuantity: Double,
//    val cookingTime: String,
//    val numOfServings: Int,
//    val calories: Double,
//    val protein: Double,
//    val fats: Double,
//    val fiber: Double,
//    val saturatedFat: Double,
//    val carbs: Double,
//    val sugar: Double,
//    val calcium: Double,
//    val iron: Double,
//    val potassium: Double,
//    val sodium: Double,
//    val vitaminD: Double,
//    val cholesterol: Double,
//    val transFat: Double,
//    val ingredients: List<IngredientList>,
//    val instructions: List<String>,
//    val unit: String,
//    val isConsumed: Boolean,
//    val isAteSomethingElse: Boolean,
//    val isSkipped: Boolean,
//    val isSwapped: Boolean,
//    val isRepeat: Boolean,
//    val isFavourite: Boolean,
//    val notes: List<String>
//) : Parcelable
//
//@Parcelize
//data class IngredientList(
//    val name: String,
//    val isChecked: Boolean,
//    val _id: String
//): Parcelable
