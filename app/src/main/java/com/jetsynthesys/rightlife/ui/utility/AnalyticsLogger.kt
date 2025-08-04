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

    fun logEvent(context: Context, eventName: String, params: Map<String, Any>? = null) {
        addParams(context, params)
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

    private fun addParams(context: Context, params: Map<String, Any>?): Map<String, Any> {
        val newParams = (params ?: emptyMap()).toMutableMap()
        val sharedPreferenceManager = SharedPreferenceManager.getInstance(context)
        var productId = ""
        sharedPreferenceManager.userProfile?.subscription?.forEach { subscription ->
            if (subscription.status) {
                productId = subscription.productId
            }
        }

        newParams[AnalyticsParam.USER_ID] = sharedPreferenceManager.userId
        newParams[AnalyticsParam.USER_TYPE] =
            if (sharedPreferenceManager.userProfile?.isSubscribed == true) "Paid User" else "free User"
        newParams[AnalyticsParam.GENDER] = sharedPreferenceManager.userProfile?.userdata?.gender!!
        newParams[AnalyticsParam.AGE] = sharedPreferenceManager.userProfile?.userdata?.age!!
        newParams[AnalyticsParam.GOAL] = sharedPreferenceManager.selectedOnboardingModule
        newParams[AnalyticsParam.SUB_GOAL] = sharedPreferenceManager.selectedOnboardingSubModule
        newParams[AnalyticsParam.USER_PLAN] = productId
        newParams[AnalyticsParam.TIMESTAMP] = System.currentTimeMillis()
        return newParams
    }
}
