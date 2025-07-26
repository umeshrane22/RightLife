package com.jetsynthesys.rightlife.ui.healthcam

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReportIdData {
    @SerializedName("_id")
    @Expose
    var id: String? = null

    @SerializedName("userId")
    @Expose
    var userId: String? = null

    @SerializedName("answerId")
    @Expose
    var answerId: String? = null

    @SerializedName("service")
    @Expose
    var service: Service? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("createdAt")
    @Expose
    var createdAt: String? = null

    @SerializedName("updatedAt")
    @Expose
    var updatedAt: String? = null

    @SerializedName("__v")
    @Expose
    var v: Int? = null

    @SerializedName("count")
    @Expose
    var count: Int? = null

    @SerializedName("healthCamReportByCategory")
    @Expose
    var healthCamReportByCategory: Any? = null

    @SerializedName("userAnswers")
    @Expose
    var userAnswers: UserAnswers? = null

    @SerializedName("scoreComponents")
    @Expose
    var scoreComponents: List<ScoreComponent>? = null

    @SerializedName("recommendation")
    @Expose
    var recommendation: List<Any>? = null
}
