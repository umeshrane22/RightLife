package com.jetsynthesys.rightlife.ui.settings.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Entity {
    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("_id")
    @Expose
    var id: String? = null

    @SerializedName("entityType")
    @Expose
    var entityType: String? = null

    @SerializedName("createdAt")
    @Expose
    var createdAt: String? = null

    @SerializedName("updatedAt")
    @Expose
    var updatedAt: String? = null
}