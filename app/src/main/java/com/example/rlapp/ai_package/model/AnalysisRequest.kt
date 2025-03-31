package com.example.rlapp.ai_package.model

data class AnalysisRequest(
    val key: String,
    val image: String // Base64-encoded image
)

data class AnalysisResponse(
    val result: Any? // Adjust based on actual response structure
)
