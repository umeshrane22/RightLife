package com.jetsynthesys.rightlife.ui.new_design.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class GoogleLoginTokenResponse {

    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("statusCode")
    @Expose
    var statusCode: Int? = null

    @SerializedName("accessToken")
    @Expose
    var accessToken: String? = null

    @SerializedName("refreshToken")
    @Expose
    var refreshToken: String? = null

    @SerializedName("isNewUser")
    @Expose
    var isNewUser: Boolean? = null

    @SerializedName("role")
    @Expose
    var role: String? = null

    @SerializedName("maxDeviceReached")
    @Expose
    var maxDeviceReached: Boolean? = null

    @SerializedName("devices")
    @Expose
    var devices: List<DeviceInfo>? = null
}


/*
class GoogleLoginTokenResponse {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("statusCode")
    @Expose
    var statusCode: Int? = null

    @SerializedName("accessToken")
    @Expose
    var accessToken: String? = null

    @SerializedName("refreshToken")
    @Expose
    var refreshToken: String? = null

    @SerializedName("role")
    @Expose
    var role: String? = null

    @SerializedName("isNewUser")
    @Expose
    var isNewUser: Boolean? = null

}*/
