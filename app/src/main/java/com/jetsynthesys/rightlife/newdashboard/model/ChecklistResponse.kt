package com.jetsynthesys.rightlife.newdashboard.model

data class ChecklistResponse(
    val success: Boolean,
    val statusCode: Int,
    val data: ChecklistData
)