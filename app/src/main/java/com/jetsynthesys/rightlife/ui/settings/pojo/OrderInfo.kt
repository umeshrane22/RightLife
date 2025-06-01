package com.jetsynthesys.rightlife.ui.settings.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class OrderInfo {
    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("orderId")
    @Expose
    var orderId: String? = null

    @SerializedName("purchaseDate")
    @Expose
    var purchaseDate: String? = null

    @SerializedName("amountPaid")
    @Expose
    var amountPaid: Double? = null

    @SerializedName("paymentMethod")
    @Expose
    var paymentMethod: String? = null

    @SerializedName("Validity")
    @Expose
    var validity: String? = null
}