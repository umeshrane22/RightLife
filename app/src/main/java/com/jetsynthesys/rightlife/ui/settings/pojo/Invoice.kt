package com.jetsynthesys.rightlife.ui.settings.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Invoice {
    @SerializedName("url")
    @Expose
    var url: String? = null

    @SerializedName("id")
    @Expose
    var id: String? = null
}