package com.example.rlapp.newdashboard.model

data class ChecklistResponse(
    val success: Boolean,
    val statusCode: Int,
    val data: ChecklistData
)