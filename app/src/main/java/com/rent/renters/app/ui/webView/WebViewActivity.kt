package com.rent.renters.app.ui.webView


import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.rent.renters.R
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import kotlinx.android.synthetic.main.activity_web_view.*
import kotlinx.android.synthetic.main.header_layout.*

class WebViewActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        initView()
    }

    private fun initView() {
        if(intent!=null){
            val url = intent.getStringExtra(Iconstants.URL)
            webView!!.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    view?.loadUrl(url)
                    return true
                }
            }
            webView!!.loadUrl(url)
        }

        imgBtnBack.setOnClickListener{finish()}
    }
}
