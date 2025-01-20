package com.example.rlapp.ui.new_design.pojo

data class GoogleSignInRequest
    (
    private val code: String,
    private val deviceId: String,
    private val deviceName: String,
    private val deviceToken: String,

    )