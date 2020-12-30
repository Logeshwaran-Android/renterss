package com.rent.renters.app.ui.detailPage



import android.os.Bundle
import android.view.View

import com.rent.renters.R
import com.rent.renters.app.ui.base.BaseActivity
import com.rent.renters.app.ui.base.Iconstants
import kotlinx.android.synthetic.main.activity_space_rules.*
import kotlinx.android.synthetic.main.header_layout.*
import kotlinx.android.synthetic.main.no_data_layout.*

class SpaceRulesActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_space_rules)
        initView()
        customizeToolbar()
    }
    private fun initView() {
        if(intent!=null){
            if(intent.hasExtra(Iconstants.SPACE_RULES)) {
                val mText  = intent.getStringExtra(Iconstants.SPACE_RULES)
                if(mText!!.isNotEmpty())
                tvSpaceRulesDesc.text = mText
                else {
                    noDataLayout.visibility = View.VISIBLE
                    btnStartExploring.visibility = View.GONE
                }
            }
            if(intent.hasExtra(Iconstants.SPACE_TITLE))
            tvSpaceRules.text = intent.getStringExtra(Iconstants.SPACE_TITLE)
        }
        imgBtnBack.setOnClickListener { finish() }
    }

    private fun customizeToolbar() {
        imgBtnBack.setImageResource(R.drawable.ic_close)
    }
}
