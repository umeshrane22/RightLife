package com.jetsynthesys.rightlife.ui.NewSleepSounds.newsleepmodel

data class SleepCategoryResponse(
    val success: Boolean,
    val statusCode: Int,
    val data: List<SleepCategory>
)

data class SleepCategory(
    val _id: String,
    val title: String,
    val subtitle: String
)
