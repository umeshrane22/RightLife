package com.example.rlapp

import android.app.Application
import android.content.Context
import android.util.Log
import com.sondeservices.edge.init.SondeEdgeSdk

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        sondeSdk(this)
    }
    private fun sondeSdk(context: Context) {
        SondeEdgeSdk.init(
            context,
            BuildConfig.CLIENT_ID_SONDE,
            BuildConfig.CLIENT_SECRET_SONDE,
            object : SondeEdgeSdk.SondeInitCallback {
                override fun onError(error: Exception) {
                    Log.d("onError","error")
                }
                override fun onSuccess() {
                    Log.d("SDK init onSuccess","onSuccess")
                }
            }
        )
    }
}
