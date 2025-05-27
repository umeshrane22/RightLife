package com.jetsynthesys.rightlife.ui.settings.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Pricing {
    @SerializedName("basePrice")
    @Expose
    var basePrice: Double? = null

    @SerializedName("promoPrice")
    @Expose
    var promoPrice: Double? = null

    @SerializedName("totalPrice")
    @Expose
    var totalPrice: Double? = null

    @SerializedName("gatewayPrice")
    @Expose
    var gatewayPrice: Double? = null

    @SerializedName("commissionPercentage")
    @Expose
    var commissionPercentage: Double? = null

    @SerializedName("commissionPrice")
    @Expose
    var commissionPrice: Double? = null

    @SerializedName("grandTotal")
    @Expose
    var grandTotal: Double? = null

    @SerializedName("hostTotal")
    @Expose
    var hostTotal: Double? = null

    @SerializedName("taxRate")
    @Expose
    var taxRate: TaxRate? = null
}