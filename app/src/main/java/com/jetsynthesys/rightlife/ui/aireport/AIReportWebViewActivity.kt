package com.jetsynthesys.rightlife.ui.aireport

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.jetsynthesys.rightlife.R
import com.jetsynthesys.rightlife.databinding.ActivityAireportWebViewBinding

class AIReportWebViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAireportWebViewBinding

    companion object {
        const val EXTRA_REPORT_ID = "extra_report_id" // Define a key for the ID
        const val BASE_URL = "https://ai-qa.rightlife.com/pdf/view-report/"
    }

    @SuppressLint("SetJavaScriptEnabled") // Suppress lint warning for JavaScript enabling
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAireportWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backButton.setOnClickListener {
            onBackPressed() // Handle back button click
        }

        // 1. Retrieve the dynamic report ID from the Intent extras
        val reportId = intent.getStringExtra(EXTRA_REPORT_ID)

        if (reportId.isNullOrEmpty()) {
            Toast.makeText(this, "Report ID not provided.", Toast.LENGTH_LONG).show()
            finish() // Close the activity if no ID is available
            return
        }

        // 2. Construct the full URL using the base URL and the dynamic ID
        val fullUrl = "$BASE_URL$reportId"

        val webView: WebView = binding.webView

        // 3. Configure WebView settings (crucial for most modern websites)
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true      // Enable JavaScript
        webSettings.domStorageEnabled = true      // Enable DOM storage (e.g., for local storage)
        webSettings.loadWithOverviewMode = true   // Load pages with overview mode
        webSettings.useWideViewPort = true        // Use wide viewport
        webSettings.builtInZoomControls = true    // Enable built-in zoom controls
        webSettings.displayZoomControls = false   // Hide zoom controls on screen
        webSettings.allowFileAccess = true        // Allow WebView to access local files
        webSettings.allowContentAccess = true     // Allow WebView to access content URIs

        // 4. Set a WebViewClient to handle page navigation within the WebView
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                // This tells the WebView to handle all URLs itself,
                // preventing them from opening in an external browser.
                view?.loadUrl(url ?: "")
                return true
            }

            override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
                super.onReceivedError(view, errorCode, description, failingUrl)
                Toast.makeText(this@AIReportWebViewActivity, "Error loading page: $description", Toast.LENGTH_LONG).show()
            }
        }

        // 5. Load the constructed URL
        webView.loadUrl(fullUrl)
    }

    // Handle back button presses within the WebView
    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack() // Go back in WebView history
        } else {
            super.onBackPressed() // If WebView can't go back, close the activity
        }
    }
}