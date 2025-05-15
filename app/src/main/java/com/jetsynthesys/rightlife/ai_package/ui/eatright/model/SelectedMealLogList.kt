package com.jetsynthesys.rightlife.ai_package.ui.eatright.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SelectedMealLogList(
    val meal_name: String?,
    val meal_type: String,
    val meal_log: List<MealLogItems>
):Parcelable

@Parcelize
data class MealLogItems(
    val meal_id: String?,
    val recipe_name: String?,
    val meal_quantity: Int? = null,
    val unit: String? = null,
    val measure: String? = null
):Parcelable
