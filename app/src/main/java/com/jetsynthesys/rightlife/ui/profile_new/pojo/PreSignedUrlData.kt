package com.jetsynthesys.rightlife.ui.profile_new.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PreSignedUrlData {
    @SerializedName("url")
    @Expose
    var url: String? = null

    @SerializedName("file")
    @Expose
    var file: ImageFile? = null

    @SerializedName("access")
    @Expose
    var access: String? = null
}