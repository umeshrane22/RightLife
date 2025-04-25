package com.jetsynthesys.rightlife.ui.settings.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Item {
    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("subTitle")
    @Expose
    var subTitle: String? = null

    @SerializedName("imageUrl")
    @Expose
    var imageUrl: String? = null

    @SerializedName("includes")
    @Expose
    var includes: List<Any>? = null
}
