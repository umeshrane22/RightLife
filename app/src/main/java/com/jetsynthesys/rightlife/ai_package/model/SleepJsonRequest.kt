package com.jetsynthesys.rightlife.ai_package.model


@kotlinx.serialization.Serializable
data class SleepJsonRequest(
    val user_id: String,
    val source: String,
    val sleep_stage: List<SleepStageJson>?)

@kotlinx.serialization.Serializable
data class SleepStageJson(
    val start_datetime: String,
    val end_datetime: String,
    val record_type: String,
    val unit: String,
    val value: String,
    val source_name: String
)
