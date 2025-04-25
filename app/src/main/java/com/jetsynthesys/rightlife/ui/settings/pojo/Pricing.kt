package com.jetsynthesys.rightlife.ui.settings.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Pricing {
    @SerializedName("basePrice")
    @Expose
    var basePrice: Int? = null

    @SerializedName("promoPrice")
    @Expose
    var promoPrice: Int? = null

    @SerializedName("totalPrice")
    @Expose
    var totalPrice: Int? = null

    @SerializedName("gatewayPrice")
    @Expose
    var gatewayPrice: Int? = null

    @SerializedName("commissionPercentage")
    @Expose
    var commissionPercentage: Int? = null

    @SerializedName("commissionPrice")
    @Expose
    var commissionPrice: Int? = null

    @SerializedName("grandTotal")
    @Expose
    var grandTotal: Int? = null

    @SerializedName("hostTotal")
    @Expose
    var hostTotal: Int? = null

    @SerializedName("taxRate")
    @Expose
    var taxRate: TaxRate? = null
}