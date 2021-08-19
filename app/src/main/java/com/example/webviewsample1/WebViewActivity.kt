package com.example.webviewsample1

import android.app.Activity
import android.app.ProgressDialog
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.webviewsample1.util.Constants.Companion.AMAZON_URL
import com.example.webviewsample1.util.Constants.Companion.URL

class WebViewActivity : AppCompatActivity() {

    private lateinit var myWebView: WebView
    var activity: Activity? = null
    private lateinit var progDailog: ProgressDialog
    private var urlLink : String = AMAZON_URL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        supportActionBar?.hide()
        activity = this
        initialiseViews()
        intent.extras?.getString(URL)?.let {
            urlLink = it
            setupWebview()
        } ?: run{
            findViewById<TextView>(R.id.tvError).visibility = View.VISIBLE
        }

    }

    private fun initialiseViews() {
        progDailog = ProgressDialog.show(activity, "Loading Page", "Please wait...", true)
        progDailog.setCancelable(false)
        myWebView = findViewById<View>(R.id.webView) as WebView
    }

    private fun setupWebview() {
        with(myWebView) {
            settings.domStorageEnabled = true
            settings.javaScriptEnabled = true
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true
            webChromeClient = WebChromeClient()
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    view.loadUrl(url)
                    return true
                }

                override fun onPageFinished(view: WebView, url: String) {
                    if (progDailog.isShowing)
                        progDailog.dismiss()
                }

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    progDailog.show()
                }

                override fun onPageCommitVisible(view: WebView?, url: String?) {
                    super.onPageCommitVisible(view, url)
                    progDailog.dismiss()
                }

                override fun onReceivedSslError(
                    view: WebView?,
                    handler: SslErrorHandler,
                    error: SslError?
                ) {
                    //Toast.makeText(context, "Oops , some SSL Error !!", Toast.LENGTH_SHORT).show()
                    Log.e("SSl Error", error.toString())
                    handler.proceed()
                }

            }
            loadUrl(urlLink)
        }
    }

    override fun onBackPressed() {
        if (myWebView.canGoBack()) {
            myWebView.goBack()
        } else {
            super.onBackPressed();
        }
    }

}