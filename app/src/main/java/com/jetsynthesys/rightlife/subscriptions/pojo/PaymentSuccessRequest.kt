package com.jetsynthesys.rightlife.subscriptions.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PaymentSuccessRequest {
    @SerializedName("planId")
    @Expose
    var planId: String? = null

    @SerializedName("planName")
    @Expose
    var planName: String? = null

    @SerializedName("couponId")
    @Expose
    var couponId: String? = null

    @SerializedName("paymentGateway")
    @Expose
    var paymentGateway: String? = null

    @SerializedName("notifyType")
    @Expose
    var notifyType: String? = null

    @SerializedName("obfuscatedExternalAccountId")
    @Expose
    var obfuscatedExternalAccountId: String? = null

    @SerializedName("orderId")
    @Expose
    var orderId: String? = null

    @SerializedName("environment")
    @Expose
    var environment: String? = null

    @SerializedName("sdkDetail")
    @Expose
    var sdkDetail: SdkDetail? = null
}