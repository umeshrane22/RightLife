package com.example.rlapp.ui.sdkpackage

import ai.nuralogix.anurasdk.error.AnuraError
import ai.nuralogix.anurasdk.network.DeepAffexDataSpec
import ai.nuralogix.anurasdk.network.DeepFXClient
import ai.nuralogix.anurasdk.network.RestClient
import ai.nuralogix.anurasdk.network.TokenStore
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.rlapp.BuildConfig
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class StartViewModel : ViewModel() {
    private val TAG = javaClass.simpleName

    /**
     * When a valid DeepAffex API device token and study configuration file are obtained,
     * [readyToMeasure] is set to `true`
     */
    val readyToMeasure: MutableLiveData<Boolean> = MutableLiveData(false)

    /**
     * Any errors encountered while communicating with DeepAffex Cloud API will be posted here
     */
    val error: MutableLiveData<String> = MutableLiveData()

    /**
     * A place to store the latest study configuration file to configure DeepAffex Extraction Library
     */
    private var savedStudyFile by SharedPreferencesHelper(KEY_DFX_EXTRACTION_LIBRARY_STUDY_CONFIG, "")

    /**
     * The hash of the currently saved study configuration file. This will be used to check if there
     * is a newer study configuration file available.
     */
    private var savedStudyHash by SharedPreferencesHelper(KEY_STUDY_HASH, "")

    /**
     * [TokenStore] handles storing DeepAffex Cloud API access tokens and refresh tokens
     */
    private var tokenStore: TokenStore

    init {
        DeepAffexDataSpec.REST_SERVER = BuildConfig.DFX_REST_URL
        DeepAffexDataSpec.WS_SERVER = BuildConfig.DFX_WS_URL

        tokenStore = TokenStore()
        RestClient.getInstance().setTokenStore(tokenStore)
    }

    /**
     * The function to call to perform the main steps to obtain a DeepAffex Cloud API access token
     * and download the latest study configuration file for DeepAffex Extraction Library
     */
    suspend fun verifyDeepAffexTokenAndStudyFile() {
        try {
            verifyToken(tokenStore.getToken())
            verifyStudyFileHash(savedStudyHash)
            DeepFXClient.getInstance().setTokenAuthorisation(tokenStore.getToken())
            readyToMeasure.postValue(true)
        } catch (e: Exception) {
            e.printStackTrace()
            error.postValue(e.message)
        }
    }

    /**
     * Verify if the current access token is still valid. If there's no token stored, call the
     * `registerLicense` DeepAffex Cloud API endpoint
     */
    private suspend fun verifyToken(unverifiedToken: String) {
        try {
            if (tokenStore.getToken().isEmpty()) {
                val str = registerLicense()
                Log.d("AAAAA",str)
                return
            }
            val statusResult = getLicenseStatus(unverifiedToken)
            val activeLicense = JSONObject(statusResult).optBoolean("ActiveLicense")
            if (!activeLicense) {
                throw Exception("Your DeepAffex license key has expired, please contact NuraLogix support")
            }
        } catch (e: HttpException) {
            e.printStackTrace()
            error.postValue(e.message)
        }
    }

    /**
     * Verify if a new study configuration file is available. If not, obtain one and store it in
     * SharedPreferences
     */
    private suspend fun verifyStudyFileHash(unverifiedStudyHash: String) {
        // Get the server study file hash
        val hashResult = getHash(BuildConfig.DFX_STUDY_ID)
        val serverHash = JSONObject(hashResult).optString("MD5Hash")
        if (serverHash != unverifiedStudyHash) {
            getNewStudyFileAndSave()
        }
    }

    /**
     * Obtain and study configuration file
     */
    private suspend fun getNewStudyFileAndSave() {
        Log.d(TAG, "start -----> getStudyFileAndSave")
        // If study file hash changes, we should get the newest study file from server
        val studyFileResult = getStudyFile(BuildConfig.DFX_STUDY_ID)
        val responseObject = JSONObject(studyFileResult)
        val studyFileHash = responseObject.optString("MD5Hash")
        val studyFile = responseObject.optString("ConfigFile")
        savedStudyHash = studyFileHash
        savedStudyFile = studyFile
    }

    /**
     * Call `GET verifyToken` DeepAffex Cloud API endpoint
     * Reference: https://dfxapiversion10.docs.apiary.io/#reference/0/general/verify-token
     *
     * @param [unverifiedToken] unverified access token
     */
    private suspend fun getLicenseStatus(unverifiedToken: String): String =
            suspendCancellableCoroutine { continuation ->
                changeListenerToContinuation(continuation)
                RestClient.getInstance().verifyToken(unverifiedToken)
            }

    /**
     * Call `POST registerLicense` DeepAffex Cloud API endpoint
     * Reference: https://dfxapiversion10.docs.apiary.io/#reference/0/organizations/register-license
     */
    private suspend fun registerLicense(): String =
            suspendCancellableCoroutine { continuation ->
                changeListenerToContinuation(continuation)
                RestClient.getInstance()
                        .registerLicense(
                                tokenStore.getDeviceName(),
                                tokenStore.getVersionName(),
                                tokenStore.getLicense()
                        )
            }

    /**
     * Call `POST retrieveSdkStudyConfigData` DeepAffex Cloud API endpoint
     * Reference: https://dfxapiversion10.docs.apiary.io/#reference/0/studies/retrieve-sdk-study-config-data
     *
     * @param [studyId] A DeepAffex Cloud Study ID
     */
    private suspend fun getStudyFile(studyId: String): String =
            suspendCancellableCoroutine { continuation ->
                changeListenerToContinuation(continuation)
                RestClient.getInstance().getStudyFile(
                        tokenStore.getToken(),
                        studyId,
                        "default"
                )
            }

    /**
     * Call `POST retrieveSdkStudyConfigDataHash` DeepAffex Cloud API endpoint
     *
     * @param [studyId] A DeepAffex Cloud Study ID
     */
    private suspend fun getHash(studyId: String): String =
            suspendCancellableCoroutine { continuation ->
                changeListenerToContinuation(continuation)
                RestClient.getInstance().getHash(
                        tokenStore.getToken(),
                        studyId,
                        "default"
                )
            }


    /**
     * Helpers
     */

    private fun changeListenerToContinuation(continuation: CancellableContinuation<String>) {
        RestClient.getInstance().setListener(object : RestClient.Listener {
            override fun onResult(action: Int, result: String) {
                if (continuation.isActive) continuation.resume(result)
            }

            override fun onError(error: AnuraError.NetworkErrorInfo?) {
                if (continuation.isCancelled) return
                continuation.resumeWithException(HttpException(error?.errorMessage))
            }
        })
    }
    internal data class HttpException(val errorMsg: String?): Exception(errorMsg)

    companion object{
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                StartViewModel()
            }
        }
    }
}