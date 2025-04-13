package com.jetsynthesys.rightlife.ai_package.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateRoutineResponse(
    val message: String,
    val user_id: String,
    val routine_name: String
)