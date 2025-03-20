package com.example.rlapp.ui.settings.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GeneralInformationData {
    @SerializedName("_id")
    @Expose
    var id: String? = null

    @SerializedName("termsConditions")
    @Expose
    var termsConditions: String? = null

    @SerializedName("privacyPolicy")
    @Expose
    var privacyPolicy: String? = null

    @SerializedName("aboutus")
    @Expose
    var aboutus: String? = null

    @SerializedName("createdAt")
    @Expose
    var createdAt: String? = null

    @SerializedName("updatedAt")
    @Expose
    var updatedAt: String? = null

    @SerializedName("__v")
    @Expose
    var v: Int? = null
}