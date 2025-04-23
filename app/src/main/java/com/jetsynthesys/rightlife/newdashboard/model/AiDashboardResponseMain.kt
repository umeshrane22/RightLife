package com.jetsynthesys.rightlife.newdashboard.model

import com.google.gson.annotations.SerializedName


data class AiDashboardResponseMain(
    @SerializedName("success") val success: Boolean?,
    @SerializedName("statusCode") val statusCode: Int?,
    @SerializedName("data") val data: AiDashboardData?,


)
