package com.example.rlapp.ui.new_design.pojo

import java.io.Serializable

data class LoggedInUser(
    var email: String,
    var isOnboardingComplete: Boolean = false
) : Serializable
