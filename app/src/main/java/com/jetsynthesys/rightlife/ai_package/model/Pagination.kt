package com.jetsynthesys.rightlife.ai_package.model

import com.google.gson.annotations.SerializedName

data class Pagination(
    @SerializedName("page") val page: Int,
    @SerializedName("limit") val limit: Int,
    @SerializedName("total_results") val totalResults: Int
)

