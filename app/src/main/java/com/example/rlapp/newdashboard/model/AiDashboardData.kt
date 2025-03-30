package com.example.rlapp.newdashboard.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import javax.annotation.processing.Generated



data class AiDashboardData(
    @SerializedName("facial_scan") val facialScan: ArrayList<FacialScan>?,
    //@SerializedName("userAnswers") val userAnswers: ArrayList<UserAnswer>?,
    @SerializedName("updatedModules") val updatedModules: ArrayList<UpdatedModule>?
)
