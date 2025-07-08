package com.jetsynthesys.rightlife.ai_package.base

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jetsynthesys.rightlife.FontScaleContextWrapper

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
