package com.jetsynthesys.rightlife.ui.settings.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ColorCode {
    @SerializedName("card")
    @Expose
    var card: String? = null

    @SerializedName("promo")
    @Expose
    var promo: String? = null

    @SerializedName("item")
    @Expose
    var item: String? = null
}