package com.jetsynthesys.rightlife.ui.aireport

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.WebSettings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jetsynthesys.rightlife.databinding.ActivityAireportWebViewBinding
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager

class AIReportWebViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAireportWebViewBinding

    companion object {
        const val EXTRA_REPORT_ID = "extra_report_id"
        const val BASE_URL = "https://ai-qa.rightlife.com/pdf/view-report/"
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAireportWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener { onBackPressed() }

        val reportId = intent.getStringExtra(EXTRA_REPORT_ID)
        if (reportId.isNullOrEmpty()) {
            Toast.makeText(this, "Report ID not provided.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        val fullUrl = "$BASE_URL$reportId"
        val webView = binding.webView
        val webSettings = webView.settings

        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true
        webSettings.builtInZoomControls = true
        webSettings.displayZoomControls = false
        webSettings.allowFileAccess = true
        webSettings.allowContentAccess = true

        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                super.onPageStarted(view, url, favicon)
                binding.progressBar.visibility = android.view.View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.progressBar.visibility = android.view.View.GONE
            }

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                view?.loadUrl(request?.url.toString())
                return true
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url ?: "")
                return true
            }

            override fun onReceivedError(
                view: WebView?, errorCode: Int, description: String?, failingUrl: String?
            ) {
                super.onReceivedError(view, errorCode, description, failingUrl)
                binding.progressBar.visibility = android.view.View.GONE
                Toast.makeText(this@AIReportWebViewActivity, "Error loading page: $description", Toast.LENGTH_LONG).show()
            }
        }

        webView.loadUrl(fullUrl)
        SharedPreferenceManager.getInstance(applicationContext).
        setAIReportGeneratedView(true)
    }

    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}