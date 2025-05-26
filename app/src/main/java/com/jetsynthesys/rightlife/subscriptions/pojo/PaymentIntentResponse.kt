package com.jetsynthesys.rightlife.subscriptions.pojo

data class PaymentIntentResponse(
    val status: String,
    val clientSecret: String,
    val amount: Int,
    val currency: String
    // Add more fields as needed based on actual response
)
