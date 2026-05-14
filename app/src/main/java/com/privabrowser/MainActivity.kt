package com.privabrowser

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import android.view.View
// NAYE IMPORTS
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.ffmpeg_android.FFmpeg

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var urlBar: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var btnDownload: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Views
        webView = findViewById(R.id.webView)
        urlBar = findViewById(R.id.urlBar)
        progressBar = findViewById(R.id.progressBar)
        btnDownload = findViewById(R.id.btnDownload)

        val btnBack: ImageButton = findViewById(R.id.btnBack)
        val btnForward: ImageButton = findViewById(R.id.btnForward)
        val btnGo: ImageButton = findViewById(R.id.btnGo)
        val btnRefresh: ImageButton = findViewById(R.id.btnRefresh)
        val btnHome: ImageButton = findViewById(R.id.btnHome)
        val btnClear: ImageButton = findViewById(R.id.btnClear)
        val btnPlaylist: ImageButton = findViewById(R.id.btnPlaylist)

        // WebView Setup
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // FIX for line 162 error: Use .setText() or .text.toString()
                urlBar.setText(url)
                progressBar.visibility = View.GONE
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                progressBar.progress = newProgress
                if (newProgress < 100) {
                    progressBar.visibility = View.VISIBLE
                } else {
                    progressBar.visibility = View.GONE
                }
            }
        }

        // URL Bar Action
        urlBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                loadUrl()
                true
            } else {
                false
            }
        }

        btnGo.setOnClickListener { loadUrl() }

        // Navigation Controls
        btnBack.setOnClickListener { if (webView.canGoBack()) webView.goBack() }
        btnForward.setOnClickListener { if (webView.canGoForward()) webView.goForward() }
        btnRefresh.setOnClickListener { webView.reload() }
        btnHome.setOnClickListener { webView.loadUrl("https://www.google.com") }
        
        btnClear.setOnClickListener {
            webView.clearHistory()
            webView.clearCache(true)
            urlBar.setText("")
            webView.loadUrl("about:blank")
        }

        // Default Load
        webView.loadUrl("https://www.google.com")
    }

    private fun loadUrl() {
        var url = urlBar.text.toString().trim()
        if (url.isNotEmpty()) {
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "https://www.google.com/search?q=$url"
            }
            webView.loadUrl(url)
        }
    }
}

