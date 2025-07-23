package com.jetsynthesys.rightlife.ai_package.model.request

data class AddWorkoutLogRequest(
    val user_id: String,
    val date: String,
    val routine_id: String)
