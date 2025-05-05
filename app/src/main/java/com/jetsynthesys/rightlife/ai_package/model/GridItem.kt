package com.jetsynthesys.rightlife.ai_package.model

data class GridItem(
    val name: String,
    val imageRes: Int,
    val additionalInfo: String,
    val fourthParameter: String,
    val dataPoints: FloatArray = FloatArray(7) { 0f } // Default to 7 zeros
)
