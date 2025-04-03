package com.jetsynthesys.rightlife.ui.new_design.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ModuleTopic : Serializable{
    @SerializedName("_id")
    @Expose
     val id: String? = null

    @SerializedName("moduleName")
    @Expose
     val moduleName: String? = null

    @SerializedName("moduleTopic")
    @Expose
     val moduleTopic: String? = null

    @SerializedName("moduleThumbnail")
    @Expose
     val moduleThumbnail: String? = null

    @SerializedName("moduleDarkThemeThumbnail")
    @Expose
     val moduleDarkThemeThumbnail: String? = null

    @SerializedName("order")
    @Expose
     val order: Int? = null

    @SerializedName("isActive")
    @Expose
     val isActive: Boolean? = null

    @SerializedName("createdAt")
    @Expose
     val createdAt: String? = null

    @SerializedName("updatedAt")
    @Expose
     val updatedAt: String? = null

    @SerializedName("__v")
    @Expose
     val v: Int? = null

    @SerializedName("isSelectedModule")
    @Expose
    var isSelected = false
}