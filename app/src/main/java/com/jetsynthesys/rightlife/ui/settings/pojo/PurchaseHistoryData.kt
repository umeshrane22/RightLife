package com.jetsynthesys.rightlife.ui.settings.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PurchaseHistoryData {
    @SerializedName("count")
    @Expose
    var count: Int? = null

    @SerializedName("currencyCode")
    @Expose
    var currencyCode: String? = null

    @SerializedName("subscriptions")
    @Expose
    var subscriptions: List<Subscription>? = null
}