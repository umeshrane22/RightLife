package com.example.rlapp.ai_package.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class NutritionResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("nutrition")
    val nutrition: Nutrition,
    @SerializedName("category")
    val category: Category,
    @SerializedName("recipes")
    val recipes: List<RecipeLists>
): Parcelable

@Parcelize
data class Nutrition(
    @SerializedName("recipesUsed")
    val recipesUsed: Int,
    @SerializedName("calories")
    val calories: NutrientDetail,
    @SerializedName("fat")
    val fat: NutrientDetail,
    @SerializedName("protein")
    val protein: NutrientDetail,
    @SerializedName("carbs")
    val carbs: NutrientDetail
):Parcelable

@Parcelize
data class NutrientDetail(
    @SerializedName("value")
    val value: Double,
    @SerializedName("unit")
    val unit: String,
    @SerializedName("confidenceRange95Percent")
    val confidenceRange95Percent: ConfidenceRange,
    @SerializedName("standardDeviation")
    val standardDeviation: Double
):Parcelable

@Parcelize
data class ConfidenceRange(
    @SerializedName("min")
    val min: Double,
    @SerializedName("max")
    val max: Double
):Parcelable

@Parcelize
data class Category(
    @SerializedName("name")
    val name: String,
    @SerializedName("probability")
    val probability: Double
):Parcelable

@Parcelize
data class RecipeLists(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("imageType")
    val imageType: String,
    @SerializedName("url")
    val url: String
):Parcelable
