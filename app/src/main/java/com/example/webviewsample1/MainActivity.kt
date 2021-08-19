package com.example.webviewsample1

import android.app.Activity
import android.app.ProgressDialog
import android.graphics.Bitmap
import android.net.http.SslError
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private lateinit var myWebView: WebView
    var activity: Activity? = null
    private lateinit var progDailog: ProgressDialog
    private var pressedTime: Long = 0

    companion object{
        const val urlLink = "https://www.amazon.in/"
        const val BACKPRESS_TIME = 2000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        activity = this
        initialiseViews()
        setupWebview()
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
                    if(progDailog.isShowing)
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

                override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler, error: SslError?) {
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
            if (pressedTime + BACKPRESS_TIME > System.currentTimeMillis()) {
                super.onBackPressed();
            } else {
                Toast.makeText(activity, "Press back again to exit", Toast.LENGTH_SHORT).show();
            }
            pressedTime = System.currentTimeMillis();
        }
    }

}