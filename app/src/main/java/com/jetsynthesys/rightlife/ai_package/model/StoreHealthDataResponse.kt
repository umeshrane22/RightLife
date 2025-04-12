package com.jetsynthesys.rightlife.ai_package.model

import kotlinx.serialization.Serializable

@Serializable
data class StoreHealthDataResponse(
    val message: String,
    val inserted_id: String
)