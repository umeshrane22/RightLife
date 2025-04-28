package com.jetsynthesys.rightlife

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

fun Context.isInternetAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: run {
        return false
    }
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: run {
        return false
    }
    val isConnected = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    /*if (!isConnected) {
        showCustomToast("No Internet Connection")
    }*/
    return isConnected
}
