package com.jetsynthesys.rightlife.ui.settings.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NotificationData {
    @SerializedName("pushNotification")
    @Expose
    var pushNotification: Boolean? = null

    @SerializedName("newsLetter")
    @Expose
    var newsLetter: Boolean? = null

    @SerializedName("promotionalOffers")
    @Expose
    var promotionalOffers: Boolean? = null
}