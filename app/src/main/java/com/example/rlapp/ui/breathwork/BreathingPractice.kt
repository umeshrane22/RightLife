package com.example.rlapp.ui.breathwork

enum class BreathingPractice(
    val inhaleDuration: Long,
    val holdDuration: Long,
    val exhaleDuration: Long
) {
    BOX_BREATHING(4000L, 4000L, 4000L), // 4-4-4-4
    FOUR_SEVEN_EIGHT(4000L, 7000L, 8000L), // 4-7-8
    EQUAL_BREATHING(4000L, 0L, 4000L), // 4-0-4
    ALTERNATE_NOSTRIL(4000L, 0L, 4000L) // 4-0-4
}
