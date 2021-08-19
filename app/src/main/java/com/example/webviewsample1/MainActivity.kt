package com.example.webviewsample1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import com.example.webviewsample1.util.Constants

class MainActivity : AppCompatActivity() {
    private lateinit var edtURL: AppCompatEditText
    private lateinit var btnUrl: Button
    private lateinit var btnAmazon: Button

    private lateinit var activity: Activity
    private var pressedTime: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activity = this
        initialiseViews()
        setOnClickListener()
    }

    private fun initialiseViews() {
        edtURL = findViewById(R.id.edtURL)
        btnUrl = findViewById(R.id.btnUrl)
        btnAmazon = findViewById(R.id.btnAmazon)
    }

    private fun setOnClickListener() {
        btnAmazon.setOnClickListener {
            edtURL.setText("")
            startWebviewActivity(Constants.AMAZON_URL)
        }

        
        btnUrl.setOnClickListener {
            if (edtURL.text.isNullOrBlank()) {
                Toast.makeText(activity, "Pls enter the valid text", Toast.LENGTH_SHORT).show();
            } else {
                var url = edtURL.text.toString()
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    if(!url.startsWith("www.",ignoreCase = true)){
                        url = "www.$url"
                    }
                    url = "https://$url"
                }
                startWebviewActivity(url)
            }
        }
    }

    private fun startWebviewActivity(url: String) {
        hideKeyboard()
        val intent = Intent(activity, WebViewActivity::class.java)
        val bundle = Bundle()
        bundle.putString(Constants.URL, url)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    private fun hideKeyboard() {
        val imm = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus ?: View(activity)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onBackPressed() {
        if (pressedTime + Constants.BACKPRESS_TIME > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(activity, "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }


}