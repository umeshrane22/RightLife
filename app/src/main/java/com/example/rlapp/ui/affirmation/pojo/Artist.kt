package com.example.rlapp.ui.affirmation.pojo

import com.example.rlapp.ui.therledit.SocialMediaLink
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Artist {
    @SerializedName("_id")
    @Expose
    var id: String? = null

    @SerializedName("firstName")
    @Expose
    var firstName: String? = null

    @SerializedName("lastName")
    @Expose
    var lastName: String? = null

    @SerializedName("nickName")
    @Expose
    var nickName: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("bio")
    @Expose
    var bio: String? = null

    @SerializedName("socialMediaLinks")
    @Expose
    var socialMediaLinks: List<SocialMediaLink>? = null

    @SerializedName("isInstructor")
    @Expose
    var isInstructor: Boolean? = null

    @SerializedName("createdAt")
    @Expose
    var createdAt: String? = null

    @SerializedName("updatedAt")
    @Expose
    var updatedAt: String? = null

    @SerializedName("__v")
    @Expose
    var v: Int? = null

    @SerializedName("profilePicture")
    @Expose
    var profilePicture: String? = null
}