package com.jetsynthesys.rightlife.ui.settings.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Subscription {
    @SerializedName("appId")
    @Expose
    var appId: String? = null

    @SerializedName("pricing")
    @Expose
    var pricing: Pricing? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("isCancelled")
    @Expose
    var isCancelled: Boolean? = null

    @SerializedName("cancellationReason")
    @Expose
    var cancellationReason: String? = null

    @SerializedName("_id")
    @Expose
    var id: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("type")
    @Expose
    var type: String? = null

    @SerializedName("entities")
    @Expose
    var entities: List<Entity>? = null

    @SerializedName("orderInfo")
    @Expose
    var orderInfo: OrderInfo? = null

    @SerializedName("renewalAt")
    @Expose
    var renewalAt: String? = null

    @SerializedName("createdAt")
    @Expose
    var createdAt: String? = null

    @SerializedName("updatedAt")
    @Expose
    var updatedAt: String? = null

    @SerializedName("startDateTime")
    @Expose
    var startDateTime: String? = null

    @SerializedName("endDateTime")
    @Expose
    var endDateTime: String? = null

    @SerializedName("bundles")
    @Expose
    var bundles: List<Bundle>? = null

    @SerializedName("invoice")
    @Expose
    var invoice: Invoice? = null

    @SerializedName("planInfo")
    @Expose
    var planInfo: String? = null

    @SerializedName("cardInfo")
    @Expose
    var cardInfo: CardInfo? = null

    @SerializedName("eventInfo")
    @Expose
    var eventInfo: Any? = null

    @SerializedName("paymentMethod")
    @Expose
    var paymentMethod: String? = null
}