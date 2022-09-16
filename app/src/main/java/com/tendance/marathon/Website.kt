package com.tendance.marathon

import android.app.ProgressDialog
import android.content.DialogInterface
import android.net.http.SslError
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast

class Website : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_website)

        supportActionBar!!.hide()

        val web = findViewById<WebView>(R.id.webview)
        setContentView(web)


        web.clearSslPreferences()
        val webSetting = web.settings
        webSetting.javaScriptEnabled=true
        web.webViewClient= WebViewClient()
        web.settings.domStorageEnabled = true
        web.settings.javaScriptCanOpenWindowsAutomatically = true

        web.settings.loadsImagesAutomatically = true
        web.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY

        val alertDialog: android.app.AlertDialog? = android.app.AlertDialog.Builder(this).create()
        val progressBar: ProgressDialog? = ProgressDialog.show(this, "ouverture", "patientez...")

        web.webViewClient = object : WebViewClient() {

            override fun onReceivedSslError(
                view: WebView,
                handler: SslErrorHandler,
                error: SslError
            ) {
                // DO NOT CALL SUPER METHOD
                super.onReceivedSslError(view, handler, error)

            }

//            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
//                Log.i(TAG, "Processing webview url click...")
//                view.loadUrl(url)
//                return true
//            }

            override fun onPageFinished(view: WebView, url: String) {
                Log.i("TAG", "Finished loading URL: $url")
                if (progressBar!!.isShowing) {
                    progressBar.dismiss()
                }
            }

            override fun onReceivedError(
                view: WebView,
                errorCode: Int,
                description: String,
                failingUrl: String
            ) {
                Log.e("TAG", "Error: $description")
                Toast.makeText(applicationContext, "Oh no! $description", Toast.LENGTH_SHORT).show()
                alertDialog!!.setTitle("Error")
                alertDialog.setMessage(description)
                alertDialog.setButton("OK",
                    DialogInterface.OnClickListener { _, _ -> return@OnClickListener })
                alertDialog.show()
            }
        }
        web.loadUrl("http://marathondelapaixdelome.com")



    }
}