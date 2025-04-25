package com.jetsynthesys.rightlife.ui.settings.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CardInfo {
    @SerializedName("_id")
    @Expose
    var id: String? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("subTitle")
    @Expose
    var subTitle: String? = null

    @SerializedName("promoText")
    @Expose
    var promoText: String? = null

    @SerializedName("desc")
    @Expose
    var desc: String? = null

    @SerializedName("colorCode")
    @Expose
    var colorCode: ColorCode? = null

    @SerializedName("imageUrl")
    @Expose
    var imageUrl: String? = null

    @SerializedName("items")
    @Expose
    var items: List<Item>? = null
}