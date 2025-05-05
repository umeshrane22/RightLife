package com.jetsynthesys.rightlife.ai_package.model.response

sealed class MergedLogsMealItem {
    data class RegularRecipeList(val entry: RegularRecipeEntry) : MergedLogsMealItem()
    data class SnapMealList(val meal: SnapMeal) : MergedLogsMealItem()
}
