package com.jetsynthesys.rightlife.subscriptions.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SubscriptionPlanData {
    @SerializedName("result")
    @Expose
    var result: Result? = null
}