package com.jetsynthesys.rightlife.ai_package.model.response

sealed class MergedMealItem {
    data class SnapMeal(val data: SnapMealDetail) : MergedMealItem()
    data class SavedMeal(val data: MealDetails) : MergedMealItem()
}
