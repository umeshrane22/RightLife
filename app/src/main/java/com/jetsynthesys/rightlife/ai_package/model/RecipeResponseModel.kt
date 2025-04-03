package com.jetsynthesys.rightlife.ai_package.model

data class RecipeResponseModel(
    val success: Boolean,
    val statusCode: Int,
    val data: List<RecipeList>
)

data class RecipeList(
    val name: String,
    val image: String,
    val _id: String
)
