package com.example.rlapp.ui.new_design.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class InterestDataList {
    @SerializedName("_id")
    @Expose
     val id: String? = null

    @SerializedName("moduleName")
    @Expose
     val moduleName: String? = null

    @SerializedName("topic")
    @Expose
     val topic: String? = null

    @SerializedName("createdAt")
    @Expose
     val createdAt: String? = null

    @SerializedName("updatedAt")
    @Expose
     val updatedAt: String? = null

    @SerializedName("__v")
    @Expose
     val v: Int? = null

    var isSelected: Boolean = false
}
