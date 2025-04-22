package com.jetsynthesys.rightlife.ai_package.model

data class AnalysisRequest(
    val key: String,
    val image: String,
    val description : String// Base64-encoded image
)

data class AnalysisResponse(
    val result: Any? // Adjust based on actual response structure
)
