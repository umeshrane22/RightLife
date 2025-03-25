package com.example.rlapp.newdashboard



import java.io.Serializable

data class HeartRateData(
    val heartRate: Int,
    val date: String,
    val trendData: ArrayList<String>  // 7-day heart rate values
) : Serializable

