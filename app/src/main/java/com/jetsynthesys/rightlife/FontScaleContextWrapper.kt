package com.jetsynthesys.rightlife

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration

class FontScaleContextWrapper(base: Context) : ContextWrapper(base) {
    companion object {
        fun wrap(context: Context): Context {
            val config = Configuration(context.resources.configuration)
            config.fontScale = 1.0f
            return context.createConfigurationContext(config)
        }
    }
}