package com.jetsynthesys.rightlife.RetrofitData

import android.content.Context
import com.jetsynthesys.rightlife.isInternetAvailable
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class NetworkConnectionInterceptor(context: Context) : Interceptor {

    private val appContext = context.applicationContext

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!appContext.isInternetAvailable()) {
            throw IOException("No Internet Connection")
        }
        return chain.proceed(chain.request())
    }
}
