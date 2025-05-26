package com.jetsynthesys.rightlife.subscriptions.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlin.collections.List

class Result {
    @SerializedName("LIST")
    @Expose
    var list: List<PlanList>? = null
}