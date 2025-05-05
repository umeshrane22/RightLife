package com.jetsynthesys.rightlife.ai_package.ui.eatright.model

data class ActivityFactorResponse(
    val status_code: Int,
    val data: ActivityFactorData
)

data class ActivityFactorData(
    val user_id: String,
    val range: String,
    val start_date: String,
    val end_date: String,
    val average_activity_factor: Double,
    val activity_factor_trend: List<ActivityFactorTrend>,
    val trend: String
)

data class ActivityFactorTrend(
    val date: String,
    val activity_factor: Double
)