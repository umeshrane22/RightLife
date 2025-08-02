package com.jetsynthesys.rightlife.ui.utility

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

object AnalyticsLogger {

    private var firebaseAnalytics: FirebaseAnalytics? = null

    fun init(context: Context) {
        if (firebaseAnalytics == null) {
            firebaseAnalytics = FirebaseAnalytics.getInstance(context.applicationContext)
        }
    }

    fun logEvent(eventName: String, params: Map<String, Any>? = null) {
        val bundle = Bundle()
        params?.forEach { (key, value) ->
            when (value) {
                is String -> bundle.putString(key, value)
                is Int -> bundle.putInt(key, value)
                is Long -> bundle.putLong(key, value)
                is Double -> bundle.putDouble(key, value)
                is Float -> bundle.putFloat(key, value)
                is Boolean -> bundle.putBoolean(key, value)
            }
        }
        firebaseAnalytics?.logEvent(eventName, bundle)
    }
}
