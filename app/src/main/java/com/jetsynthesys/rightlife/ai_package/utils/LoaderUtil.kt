package com.jetsynthesys.rightlife.ai_package.utils

import android.view.View
import android.widget.FrameLayout
import com.jetsynthesys.rightlife.R

class LoaderUtil {
    companion object {
        private var loadingOverlay : FrameLayout? = null

        fun showLoader(view: View) {
            loadingOverlay = view.findViewById(R.id.loading_overlay)
            loadingOverlay?.visibility = View.VISIBLE
        }

        fun dismissLoader(view: View) {
            loadingOverlay = view.findViewById(R.id.loading_overlay)
            loadingOverlay?.visibility = View.GONE
        }
    }
}
