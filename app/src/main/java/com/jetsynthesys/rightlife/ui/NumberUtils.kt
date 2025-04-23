package com.jetsynthesys.rightlife.ui

import kotlin.math.pow

object NumberUtils {

    fun smartRound(value: Double, decimalPlaces: Int): Double {
        require(decimalPlaces in 0..2)

        val multiplier = 10.0.pow((decimalPlaces + 1).toDouble()) // for checking next digit
        val temp = (value * multiplier).toInt()
        val mainPart = temp / 10
        val nextDigit = temp % 10

        return if (nextDigit >= 5) {
            (mainPart + 1) / 10.0.pow(decimalPlaces.toDouble())
        } else {
            mainPart / 10.0.pow(decimalPlaces.toDouble())
        }
    }
}