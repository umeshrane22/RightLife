package com.jetsynthesys.rightlife.subscriptions.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SdkDetail {
    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("price")
    @Expose
    var price: String? = null

    @SerializedName("environment")
    @Expose
    var environment: String? = null

    @SerializedName("orderId")
    @Expose
    var orderId: String? = null

    @SerializedName("currencyCode")
    @Expose
    var currencyCode: String? = null

    @SerializedName("currencySymbol")
    @Expose
    var currencySymbol: String? = null
}