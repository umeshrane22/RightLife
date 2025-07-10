package com.jetsynthesys.rightlife

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.jetsynthesys.rightlife.RetrofitData.ApiClient
import com.jetsynthesys.rightlife.RetrofitData.ApiService
import com.jetsynthesys.rightlife.databinding.ActivityBaseBinding
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager
import java.io.IOException

open class BaseActivity : AppCompatActivity() {

    private lateinit var baseBinding: ActivityBaseBinding
    lateinit var sharedPreferenceManager: SharedPreferenceManager
    lateinit var apiService: ApiService
     lateinit var apiServiceFastApi: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseBinding = ActivityBaseBinding.inflate(layoutInflater)

        setContentView(baseBinding.root)

        sharedPreferenceManager = SharedPreferenceManager.getInstance(this)
        apiService = ApiClient.getClient(this).create(ApiService::class.java)
        apiServiceFastApi = ApiClient.getAIClient().create(ApiService::class.java)
    }

    fun setChildContentView(layoutResId: Int) {
        val childView = layoutInflater.inflate(layoutResId, baseBinding.container, false)
        baseBinding.container.addView(childView)
    }

    fun setChildContentView(view: View) {
        baseBinding.container.addView(view)
    }

    fun handleNoInternetView(e: Throwable) {
        /*when (e) {
            is IOException ->
                baseBinding.noInternetView.visibility = View.VISIBLE

            else -> e.message?.let { showCustomToast(it) }
        }*/
        if (e is IOException)
            e.message?.let { showCustomToast(it) }
    }

    override fun attachBaseContext(newBase: Context) {
        val context = FontScaleContextWrapper.wrap(newBase)
        super.attachBaseContext(context)
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        if (overrideConfiguration != null) {
            overrideConfiguration.fontScale = 1.0f
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }
}