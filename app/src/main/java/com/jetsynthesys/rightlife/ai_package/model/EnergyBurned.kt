package com.jetsynthesys.rightlife.ai_package.model

data  class EnergyBurned(
    val creation_datetime: String,
    val end_datetime: String,
    val source_version: String,
    val start_datetime: String,
    val record_type: String,
    val unit: String,
    val value: String,
    val _id: String? = null // Nullable, since some objects don't have _id
)
