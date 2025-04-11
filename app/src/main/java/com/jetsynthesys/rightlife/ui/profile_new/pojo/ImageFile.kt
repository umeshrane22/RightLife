package com.jetsynthesys.rightlife.ui.profile_new.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ImageFile {
    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("url")
    @Expose
    var url: String? = null

    @SerializedName("urlMedia")
    @Expose
    var urlMedia: String? = null
}