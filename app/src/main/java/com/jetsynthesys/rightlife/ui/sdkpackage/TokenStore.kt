package com.jetsynthesys.rightlife.ui.sdkpackage

import ai.nuralogix.anurasdk.network.DeepFXClient
import ai.nuralogix.anurasdk.network.TokenStore
import android.os.Build
import android.util.Log
import com.jetsynthesys.rightlife.BuildConfig
import org.json.JSONException
import org.json.JSONObject
class TokenStore : TokenStore {

    /**
     * Applications that use Anura Core SDK only use device tokens and not user tokens
     */
    override var tokenUsed: TokenStore.TokenType = TokenStore.TokenType.Device

    /**
     * This can be used to set custom token and refresh token expiry time.
     * For example, use the follow to set 300 seconds token expiry time
     *  and 600 seconds refresh token expiry time.
     *
     *  TokenStore.TokenExpirySetting(tokenExpiresIn = 300, refreshTokenExpiresIn = 600)
     */
    override var tokenExpirySettings: TokenStore.TokenExpirySetting =
            TokenStore.TokenExpirySetting()

    private var savedDeviceToken by SharedPreferencesHelper(KEY_DEVICE_TOKEN, "")
    private var savedDeviceRefreshToken by SharedPreferencesHelper(KEY_DEVICE_REFRESH_TOKEN, "")

    /**
     * Clear device token and refresh token from SharedPreferences
     */
    override fun clearTokens(tokenType: TokenStore.TokenType) {
        savedDeviceToken = ""
        savedDeviceRefreshToken = ""
    }

    /**
     * Get basic device info (Manufacturer, Model, and App Version) that's needed when calling
     * `registerLicense` DeepAffex Cloud API endpoint.
     *
     * Reference: https://dfxapiversion10.docs.apiary.io/#reference/0/organizations/register-license
     */
    override fun getDeviceName(): String {
        return StringBuilder()
                .append(Build.MANUFACTURER)
                .append(" / ")
                .append(Build.MODEL)
                .append(" / ")
                .append(Build.VERSION.RELEASE)
                .toString()
    }

    /**
     * Retrieve your application's DeepAffex License Key
     */
    override fun getLicense(): String {
        return BuildConfig.DFX_LICENSE_KEY
    }


    /**
     * Retrieve the stored device token
     */
    override fun getToken(): String {
        return savedDeviceToken
    }

    /**
     * Retrieve the stored refresh token
     */
    override fun getRefreshToken(): String {
        return savedDeviceRefreshToken
    }

    /**
     * Parse the response from `registerLicense` to obtain and store the device and refresh tokens
     */
    override fun extractTokensAndSave(jsonResult: String, tokenType: TokenStore.TokenType) {
        try {
            val json = JSONObject(jsonResult)
            val token = json["Token"] as String
            val refreshToken = json["RefreshToken"] as String

            /**
             * Should disconnect the websocket and update the token before reconnecting,
             *  or else the websocket connection will continue to use the old token
             */
            DeepFXClient.getInstance().disconnect()
            DeepFXClient.getInstance().setTokenAuthorisation(token)
            DeepFXClient.getInstance().connect()
            savedDeviceToken = token
            savedDeviceRefreshToken = refreshToken
        } catch (e: JSONException) {
            Log.e("extractTokensAndSave", "error parsing tokens", e)
        }
    }

    /**
     * Retrieve the application's version name
     */
    override fun getVersionName(): String {
        return BuildConfig.VERSION_NAME
    }
}