package com.jetsynthesys.rightlife.ai_package.model.response

data class SnapMealRecipeResponseModel(
    val success: Boolean,
    val statusCode: Int,
    val data: List<SnapRecipeList>
)

data class SnapRecipeList(
    val id: String,
    val name: String,
    val photo_url: String
)
