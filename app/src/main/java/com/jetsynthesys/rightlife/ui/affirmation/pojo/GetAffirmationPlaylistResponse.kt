package com.jetsynthesys.rightlife.ui.affirmation.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetAffirmationPlaylistResponse {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("statusCode")
    @Expose
    var statusCode: Int? = null

    @SerializedName("data")
    @Expose
    var data: List<AffirmationSelectedCategoryData>? = null
}