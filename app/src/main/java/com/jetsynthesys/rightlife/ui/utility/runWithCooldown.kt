package com.jetsynthesys.rightlife.ui.utility

fun <T> ((T) -> Unit).runWithCooldown(cooldownMillis: Long = 2000): (T) -> Unit {
    var lastRunTime = 0L

    return { input: T ->
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastRunTime >= cooldownMillis) {
            lastRunTime = currentTime
            this(input)
        }
    }
}
