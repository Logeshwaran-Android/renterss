package com.rent.renters.app.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.rent.renters.R
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import com.rent.renters.app.ui.webView.WebViewActivity
import kotlinx.android.synthetic.main.activity_about_us.*
import kotlinx.android.synthetic.main.header_layout.*
import android.text.SpannableString
import android.text.style.UnderlineSpan


class AboutUsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)
        initView()

    }
    fun initView(){

        tvTitle.text = getString(R.string.about)
        val content = SpannableString(getString(R.string.about_us))
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        tvAboutUs.text = content


        imgBtnBack.setOnClickListener{ finish()}
        tvAboutUs.setOnClickListener(View.OnClickListener {
            var intent = Intent(this,WebViewActivity::class.java)
            intent.putExtra(Iconstants.URL,Iconstants.about_us_url)
            startActivity(intent)
        })
    }
}
