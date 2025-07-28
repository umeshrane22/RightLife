package com.jetsynthesys.rightlife

import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment

fun Fragment.runWhenAttached(action: () -> Unit) {
    if (isAdded && activity != null) {
        action()
    } else {
        // Wait and retry in 50ms until attached
        Handler(Looper.getMainLooper()).postDelayed({
            runWhenAttached(action)
        }, 50)
    }
}